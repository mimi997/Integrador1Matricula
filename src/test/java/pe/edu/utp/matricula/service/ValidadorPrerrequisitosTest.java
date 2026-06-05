package pe.edu.utp.matricula.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.matricula.entity.Curso;
import pe.edu.utp.matricula.entity.Prerrequisito;
import pe.edu.utp.matricula.exception.ReglaNegocioException;
import pe.edu.utp.matricula.repository.EstudianteRepository;
import pe.edu.utp.matricula.repository.PrerrequisitoRepository;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidadorPrerrequisitosTest {

    @Mock
    private PrerrequisitoRepository prerrequisitoRepository;

    @Mock
    private EstudianteRepository estudianteRepository;

    @InjectMocks
    private ValidadorPrerrequisitos validadorPrerrequisitos;

    @Test
    void validar_SinPrerrequisitos_Pasa() {
        Curso c = new Curso();
        c.setId(1L);

        when(prerrequisitoRepository.findByCursoId(1L)).thenReturn(Collections.emptyList());

        validadorPrerrequisitos.validar(1L, c);
        // no exception thrown
    }

    @Test
    void validar_ConPrerrequisitoAprobado_Pasa() {
        Curso c = new Curso();
        c.setId(2L);
        c.setNombre("Mate II");

        Curso pre = new Curso();
        pre.setId(1L);
        pre.setNombre("Mate I");

        Prerrequisito p = new Prerrequisito(c, pre);

        when(prerrequisitoRepository.findByCursoId(2L)).thenReturn(List.of(p));
        when(estudianteRepository.findCursosAprobados(1L)).thenReturn(List.of(pre));

        validadorPrerrequisitos.validar(1L, c);
    }

    @Test
    void validar_ConPrerrequisitoNoAprobado_LanzaExcepcion() {
        Curso c = new Curso();
        c.setId(2L);
        c.setNombre("Mate II");

        Curso pre = new Curso();
        pre.setId(1L);
        pre.setNombre("Mate I");

        Prerrequisito p = new Prerrequisito(c, pre);

        when(prerrequisitoRepository.findByCursoId(2L)).thenReturn(List.of(p));
        when(estudianteRepository.findCursosAprobados(1L)).thenReturn(Collections.emptyList());

        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, () -> {
            validadorPrerrequisitos.validar(1L, c);
        });

        assertThat(ex.getMessage()).contains("No cumple con el prerrequisito");
    }
}
