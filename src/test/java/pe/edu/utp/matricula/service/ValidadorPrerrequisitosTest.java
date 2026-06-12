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
    private pe.edu.utp.matricula.util.CachePrerrequisitos cachePrerrequisitos;

    @Mock
    private EstudianteRepository estudianteRepository;

    @InjectMocks
    private pe.edu.utp.matricula.service.impl.ValidadorPrerrequisitosImpl validadorPrerrequisitos;

    @Test
    void validar_SinPrerrequisitos_Pasa() {
        Curso c = new Curso();
        c.setId(1L);

        when(cachePrerrequisitos.getPrerrequisitos(1L)).thenReturn(com.google.common.collect.ImmutableList.of());

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

        when(cachePrerrequisitos.getPrerrequisitos(2L)).thenReturn(com.google.common.collect.ImmutableList.of(p));
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

        when(cachePrerrequisitos.getPrerrequisitos(2L)).thenReturn(com.google.common.collect.ImmutableList.of(p));
        when(estudianteRepository.findCursosAprobados(1L)).thenReturn(Collections.emptyList());

        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, () -> {
            validadorPrerrequisitos.validar(1L, c);
        });

        assertThat(ex.getMessage()).contains("No cumple con el prerrequisito");
    }

    @Test
    void validar_ConPrerrequisitoDesaprobado_LanzaExcepcion() {
        Curso c = new Curso();
        c.setId(2L);
        c.setNombre("Mate II");

        Curso pre = new Curso();
        pre.setId(1L);
        pre.setNombre("Mate I");

        Prerrequisito p = new Prerrequisito(c, pre);

        when(cachePrerrequisitos.getPrerrequisitos(2L)).thenReturn(com.google.common.collect.ImmutableList.of(p));
        when(estudianteRepository.findCursosAprobados(1L)).thenReturn(Collections.emptyList());

        ReglaNegocioException ex = assertThrows(ReglaNegocioException.class, () -> {
            validadorPrerrequisitos.validar(1L, c);
        });

        assertThat(ex.getMessage()).contains("No cumple con el prerrequisito");
    }

    @Test
    void cumplePrerrequisitos_SinPrerrequisitos_RetornaTrue() {
        Curso c = new Curso();
        c.setId(1L);
        when(cachePrerrequisitos.getPrerrequisitos(1L)).thenReturn(com.google.common.collect.ImmutableList.of());

        boolean result = validadorPrerrequisitos.cumplePrerrequisitos(1L, c);
        assertThat(result).isTrue();
    }

    @Test
    void cumplePrerrequisitos_ConPrerrequisitoAprobado_RetornaTrue() {
        Curso c = new Curso();
        c.setId(2L);
        Curso pre = new Curso();
        pre.setId(1L);
        Prerrequisito p = new Prerrequisito(c, pre);

        when(cachePrerrequisitos.getPrerrequisitos(2L)).thenReturn(com.google.common.collect.ImmutableList.of(p));
        when(estudianteRepository.findCursosAprobados(1L)).thenReturn(List.of(pre));

        boolean result = validadorPrerrequisitos.cumplePrerrequisitos(1L, c);
        assertThat(result).isTrue();
    }

    @Test
    void cumplePrerrequisitos_ConPrerrequisitoFaltante_RetornaFalse() {
        Curso c = new Curso();
        c.setId(2L);
        Curso pre = new Curso();
        pre.setId(1L);
        Prerrequisito p = new Prerrequisito(c, pre);

        when(cachePrerrequisitos.getPrerrequisitos(2L)).thenReturn(com.google.common.collect.ImmutableList.of(p));
        when(estudianteRepository.findCursosAprobados(1L)).thenReturn(Collections.emptyList());

        boolean result = validadorPrerrequisitos.cumplePrerrequisitos(1L, c);
        assertThat(result).isFalse();
    }
}
