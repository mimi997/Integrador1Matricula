package pe.edu.utp.matricula.service;

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
import pe.edu.utp.matricula.util.Constantes;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatriculaService {

    private static final Logger log = LoggerFactory.getLogger(MatriculaService.class);
    private final MatriculaRepository matriculaRepository;
    private final EstudianteRepository estudianteRepository;
    private final HorarioRepository horarioRepository;
    private final CursoRepository cursoRepository;
    private final DetalleMatriculaRepository detalleMatriculaRepository;
    private final ValidadorPrerrequisitos validadorPrerrequisitos;
    private final DetectorConflictoHorario detectorConflictoHorario;
    private final PeriodoService periodoService;

    public MatriculaService(MatriculaRepository matriculaRepository, EstudianteRepository estudianteRepository, HorarioRepository horarioRepository, CursoRepository cursoRepository, DetalleMatriculaRepository detalleMatriculaRepository, ValidadorPrerrequisitos validadorPrerrequisitos, DetectorConflictoHorario detectorConflictoHorario, PeriodoService periodoService) {
        this.matriculaRepository = matriculaRepository;
        this.estudianteRepository = estudianteRepository;
        this.horarioRepository = horarioRepository;
        this.cursoRepository = cursoRepository;
        this.detalleMatriculaRepository = detalleMatriculaRepository;
        this.validadorPrerrequisitos = validadorPrerrequisitos;
        this.detectorConflictoHorario = detectorConflictoHorario;
        this.periodoService = periodoService;
    }

    @Transactional
    public Matricula matricular(Long estudianteId, List<Long> horarioIds, String periodo) {
        if (!periodoService.isActivo()) {
            throw new ReglaNegocioException("El período de matrícula está cerrado");
        }

        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado"));

        List<Horario> horariosExistentes = horarioRepository.findHorariosByEstudianteAndPeriodo(estudianteId, periodo);

        Matricula matricula = matriculaRepository.findByEstudianteIdAndPeriodo(estudianteId, periodo)
                .orElseGet(() -> matriculaRepository.save(new Matricula(estudiante, periodo, Constantes.ESTADO_MATRICULA_CONFIRMADA, null)));

        int creditosTotales = 0;

        for (Long horId : horarioIds) {
            Horario nuevoHorario = horarioRepository.findById(horId)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Horario no encontrado: " + horId));
            
            Curso curso = nuevoHorario.getCurso();

            if (horariosExistentes.stream().anyMatch(h -> h.getId().equals(horId))) {
                throw new ReglaNegocioException("Ya estás matriculado en ese horario: " + curso.getNombre());
            }

            if (curso.getCupos() <= 0) {
                throw new ReglaNegocioException("No hay cupos disponibles para el curso: " + curso.getNombre());
            }

            if (detectorConflictoHorario.hayConflicto(nuevoHorario, horariosExistentes)) {
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

            // Añadir a lista de existentes para siguientes iteraciones
            List<Horario> nuevaLista = new ArrayList<>(horariosExistentes);
            nuevaLista.add(nuevoHorario);
            horariosExistentes = nuevaLista;
        }

        log.info("Matrícula confirmada para el estudiante ID: {}, Período: {}, Cursos matriculados: {}", estudianteId, periodo, horarioIds.size());
        return matricula;
    }

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
