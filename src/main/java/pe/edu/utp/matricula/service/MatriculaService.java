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
import pe.edu.utp.matricula.util.Constantes;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final EstudianteRepository estudianteRepository;
    private final HorarioRepository horarioRepository;
    private final CursoRepository cursoRepository;
    private final DetalleMatriculaRepository detalleMatriculaRepository;
    private final ValidadorPrerrequisitos validadorPrerrequisitos;
    private final DetectorConflictoHorario detectorConflictoHorario;

    public MatriculaService(MatriculaRepository matriculaRepository, EstudianteRepository estudianteRepository, HorarioRepository horarioRepository, CursoRepository cursoRepository, DetalleMatriculaRepository detalleMatriculaRepository, ValidadorPrerrequisitos validadorPrerrequisitos, DetectorConflictoHorario detectorConflictoHorario) {
        this.matriculaRepository = matriculaRepository;
        this.estudianteRepository = estudianteRepository;
        this.horarioRepository = horarioRepository;
        this.cursoRepository = cursoRepository;
        this.detalleMatriculaRepository = detalleMatriculaRepository;
        this.validadorPrerrequisitos = validadorPrerrequisitos;
        this.detectorConflictoHorario = detectorConflictoHorario;
    }

    @Transactional
    public Matricula matricular(Long estudianteId, List<Long> horarioIds, String periodo) {
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

        return matricula;
    }
}
