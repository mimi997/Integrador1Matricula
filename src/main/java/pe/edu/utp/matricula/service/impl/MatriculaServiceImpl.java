package pe.edu.utp.matricula.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.matricula.entity.Curso;
import pe.edu.utp.matricula.entity.DetalleMatricula;
import pe.edu.utp.matricula.entity.Estudiante;
import pe.edu.utp.matricula.entity.Horario;
import pe.edu.utp.matricula.entity.Matricula;
import pe.edu.utp.matricula.exception.RecursoNoEncontradoException;
import pe.edu.utp.matricula.exception.ReglaNegocioException;
import pe.edu.utp.matricula.repository.CursoRepository;
import pe.edu.utp.matricula.repository.DetalleMatriculaRepository;
import pe.edu.utp.matricula.repository.EstudianteRepository;
import pe.edu.utp.matricula.repository.HorarioRepository;
import pe.edu.utp.matricula.repository.MatriculaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pe.edu.utp.matricula.service.MatriculaService;
import pe.edu.utp.matricula.service.ValidadorPrerrequisitos;
import pe.edu.utp.matricula.service.DetectorConflictoHorario;
import pe.edu.utp.matricula.service.PeriodoService;
import pe.edu.utp.matricula.util.Constantes;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatriculaServiceImpl implements MatriculaService {

    private static final Logger log = LoggerFactory.getLogger(MatriculaServiceImpl.class);
    private final MatriculaRepository matriculaRepository;
    private final EstudianteRepository estudianteRepository;
    private final HorarioRepository horarioRepository;
    private final CursoRepository cursoRepository;
    private final DetalleMatriculaRepository detalleMatriculaRepository;
    private final ValidadorPrerrequisitos validadorPrerrequisitos;
    private final DetectorConflictoHorario detectorConflictoHorario;
    private final PeriodoService periodoService;

    public MatriculaServiceImpl(MatriculaRepository matriculaRepository, EstudianteRepository estudianteRepository, HorarioRepository horarioRepository, CursoRepository cursoRepository, DetalleMatriculaRepository detalleMatriculaRepository, ValidadorPrerrequisitos validadorPrerrequisitos, DetectorConflictoHorario detectorConflictoHorario, PeriodoService periodoService) {
        this.matriculaRepository = matriculaRepository;
        this.estudianteRepository = estudianteRepository;
        this.horarioRepository = horarioRepository;
        this.cursoRepository = cursoRepository;
        this.detalleMatriculaRepository = detalleMatriculaRepository;
        this.validadorPrerrequisitos = validadorPrerrequisitos;
        this.detectorConflictoHorario = detectorConflictoHorario;
        this.periodoService = periodoService;
    }

    @Override
    @Transactional
    public Matricula matricular(Long estudianteId, List<Long> horarioIds, String periodo) {
        if (!periodoService.isActivo()) {
            throw new ReglaNegocioException("El período de matrícula está cerrado");
        }

        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado"));

        Matricula matricula = matriculaRepository.findByEstudianteIdAndPeriodo(estudianteId, periodo)
                .orElseGet(() -> matriculaRepository.save(new Matricula(estudiante, periodo, Constantes.ESTADO_MATRICULA_CONFIRMADA, null)));

        // Load existing details in database
        List<DetalleMatricula> detallesExistentes = detalleMatriculaRepository.findByMatriculaId(matricula.getId());

        // We need to keep track of details to delete and details to add
        List<DetalleMatricula> detallesAEliminar = new java.util.ArrayList<>();
        List<Long> nuevosHorariosIds = new java.util.ArrayList<>();

        // 1. Identify what to delete
        // If an existing detail's horario ID is not in the submitted list, we delete it
        for (DetalleMatricula det : detallesExistentes) {
            if (!horarioIds.contains(det.getHorario().getId())) {
                detallesAEliminar.add(det);
            }
        }

        // 2. Identify what to add
        // If a submitted horario ID is not in the existing details, we add it
        for (Long hId : horarioIds) {
            boolean yaExiste = detallesExistentes.stream().anyMatch(d -> d.getHorario().getId().equals(hId));
            if (!yaExiste) {
                nuevosHorariosIds.add(hId);
            }
        }

        // 3. Process deletions (release cupos and delete details)
        for (DetalleMatricula det : detallesAEliminar) {
            Curso curso = det.getHorario().getCurso();
            curso.setCupos(curso.getCupos() + 1);
            cursoRepository.save(curso);
            detalleMatriculaRepository.delete(det);
        }

        // Update the list of remaining active horarios to perform conflict detection against
        List<Horario> horariosActivos = new java.util.ArrayList<>();
        for (DetalleMatricula det : detallesExistentes) {
            if (!detallesAEliminar.contains(det)) {
                horariosActivos.add(det.getHorario());
            }
        }

        // Calculate current total credits from remaining active details
        int creditosTotales = horariosActivos.stream().mapToInt(h -> h.getCurso().getCreditos()).sum();

        // 4. Process additions (validate, reduce cupos, and add details)
        for (Long horId : nuevosHorariosIds) {
            Horario nuevoHorario = horarioRepository.findById(horId)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Horario no encontrado: " + horId));
            
            Curso curso = nuevoHorario.getCurso();

            // Validate that student is not already registered in another schedule of the same course
            boolean yaMatriculadoEnCurso = horariosActivos.stream().anyMatch(h -> h.getCurso().getId().equals(curso.getId()));
            if (yaMatriculadoEnCurso) {
                throw new ReglaNegocioException("Ya estás matriculado en el curso: " + curso.getNombre());
            }

            if (curso.getCupos() <= 0) {
                throw new ReglaNegocioException("No hay cupos disponibles para el curso: " + curso.getNombre());
            }

            if (detectorConflictoHorario.hayConflicto(nuevoHorario, horariosActivos)) {
                throw new ReglaNegocioException("Existe conflicto de horario con el curso: " + curso.getNombre());
            }

            validadorPrerrequisitos.validar(estudianteId, curso);

            creditosTotales += curso.getCreditos();
            if (creditosTotales > Constantes.MAX_CREDITOS_POR_PERIODO) {
                throw new ReglaNegocioException("Excede el máximo de créditos permitidos (" + Constantes.MAX_CREDITOS_POR_PERIODO + ")");
            }

            // Registrar detalle
            DetalleMatricula detalle = new DetalleMatricula(matricula, nuevoHorario, Constantes.ESTADO_PREREQ_CUMPLE);
            detalleMatriculaRepository.save(detalle);

            // Reducir cupo
            curso.setCupos(curso.getCupos() - 1);
            cursoRepository.save(curso);

            // Añadir a activos
            horariosActivos.add(nuevoHorario);
        }

        log.info("Matrícula confirmada para el estudiante ID: {}, Período: {}, Cursos matriculados: {}", estudianteId, periodo, horarioIds.size());
        return matricula;
    }

    @Override
    @Transactional
    public void cancelarDetalle(Long estudianteId, Long detalleId) {
        if (!periodoService.isActivo()) {
            throw new ReglaNegocioException("El período de matrícula está cerrado");
        }
        DetalleMatricula detalle = detalleMatriculaRepository.findById(detalleId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Detalle de matrícula no encontrado"));
        
        if (!detalle.getMatricula().getEstudiante().getId().equals(estudianteId)) {
            throw new ReglaNegocioException("No tienes permiso para cancelar este curso");
        }
        
        Horario horario = detalle.getHorario();
        Curso curso = horario.getCurso();
        
        // Reponer cupo
        curso.setCupos(curso.getCupos() + 1);
        cursoRepository.save(curso);
        
        // Eliminar detalle
        detalleMatriculaRepository.delete(detalle);
        log.info("Curso cancelado. Estudiante ID: {}, Detalle ID: {}, Curso: {}", estudianteId, detalleId, curso.getNombre());
    }
}
