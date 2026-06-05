package pe.edu.utp.matricula.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.matricula.entity.Curso;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatriculaServiceTest {

    @Mock private MatriculaRepository matriculaRepository;
    @Mock private EstudianteRepository estudianteRepository;
    @Mock private HorarioRepository horarioRepository;
    @Mock private CursoRepository cursoRepository;
    @Mock private DetalleMatriculaRepository detalleMatriculaRepository;
    @Mock private ValidadorPrerrequisitos validadorPrerrequisitos;
    @Mock private DetectorConflictoHorario detectorConflictoHorario;

    @InjectMocks
    private MatriculaService matriculaService;

    @Test
    void matricular_ConExito() {
        Estudiante est = new Estudiante(null, "U1", "Sistemas", 1, 0);
        est.setId(1L);

        Curso c = new Curso("C1", "Curso1", 4, 1, "Sistemas", 10, true);
        c.setId(1L);

        Horario h = new Horario(c, null, "L", "8-10", "A");
        h.setId(1L);

        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(est));
        when(horarioRepository.findById(1L)).thenReturn(Optional.of(h));
        when(horarioRepository.findHorariosByEstudianteAndPeriodo(1L, "2023-1")).thenReturn(Collections.emptyList());
        when(detectorConflictoHorario.hayConflicto(any(), any())).thenReturn(false);
        doNothing().when(validadorPrerrequisitos).validar(anyLong(), any());

        Matricula mGuardada = new Matricula(est, "2023-1", Constantes.ESTADO_MATRICULA_CONFIRMADA, null);
        mGuardada.setId(1L);
        when(matriculaRepository.findByEstudianteIdAndPeriodo(1L, "2023-1")).thenReturn(Optional.empty());
        when(matriculaRepository.save(any(Matricula.class))).thenReturn(mGuardada);

        Matricula result = matriculaService.matricular(1L, List.of(1L), "2023-1");

        assertThat(result).isNotNull();
        assertThat(c.getCupos()).isEqualTo(9); // Se redujo el cupo
        verify(detalleMatriculaRepository, times(1)).save(any());
        verify(cursoRepository, times(1)).save(c);
    }

    @Test
    void matricular_SinCupos_LanzaExcepcion() {
        Estudiante est = new Estudiante(null, "U1", "Sistemas", 1, 0);
        est.setId(1L);

        Curso c = new Curso("C1", "Curso1", 4, 1, "Sistemas", 0, true);
        c.setId(1L);

        Horario h = new Horario(c, null, "L", "8-10", "A");
        h.setId(1L);

        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(est));
        when(horarioRepository.findById(1L)).thenReturn(Optional.of(h));

        assertThrows(ReglaNegocioException.class, () -> {
            matriculaService.matricular(1L, List.of(1L), "2023-1");
        });
    }

    @Test
    void matricular_ConConflictoHorario_LanzaExcepcion() {
        Estudiante est = new Estudiante(null, "U1", "Sistemas", 1, 0);
        est.setId(1L);

        Curso c = new Curso("C1", "Curso1", 4, 1, "Sistemas", 10, true);
        c.setId(1L);

        Horario h = new Horario(c, null, "L", "8-10", "A");
        h.setId(1L);

        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(est));
        when(horarioRepository.findById(1L)).thenReturn(Optional.of(h));
        when(horarioRepository.findHorariosByEstudianteAndPeriodo(1L, "2023-1")).thenReturn(List.of(h));
        when(detectorConflictoHorario.hayConflicto(any(), any())).thenReturn(true);

        assertThrows(ReglaNegocioException.class, () -> {
            matriculaService.matricular(1L, List.of(1L), "2023-1");
        });
    }
}
