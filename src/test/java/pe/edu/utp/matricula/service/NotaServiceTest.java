package pe.edu.utp.matricula.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.matricula.entity.DetalleMatricula;
import pe.edu.utp.matricula.entity.Docente;
import pe.edu.utp.matricula.entity.Nota;
import pe.edu.utp.matricula.exception.RecursoNoEncontradoException;
import pe.edu.utp.matricula.exception.ReglaNegocioException;
import pe.edu.utp.matricula.repository.DetalleMatriculaRepository;
import pe.edu.utp.matricula.repository.DocenteRepository;
import pe.edu.utp.matricula.repository.NotaRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotaServiceTest {

    @Mock private NotaRepository notaRepository;
    @Mock private DetalleMatriculaRepository detalleMatriculaRepository;
    @Mock private DocenteRepository docenteRepository;

    @InjectMocks
    private pe.edu.utp.matricula.service.impl.NotaServiceImpl notaService;

    @Test
    void registrarNota_ConExito() {
        DetalleMatricula dm = new DetalleMatricula();
        dm.setId(1L);

        Docente doc = new Docente();
        doc.setId(1L);

        when(detalleMatriculaRepository.findById(1L)).thenReturn(Optional.of(dm));
        when(docenteRepository.findById(1L)).thenReturn(Optional.of(doc));
        when(notaRepository.save(any(Nota.class))).thenAnswer(i -> i.getArguments()[0]);

        Nota result = notaService.registrarNota(1L, 1L, new BigDecimal("15.5"));

        assertThat(result.getValor()).isEqualByComparingTo(new BigDecimal("15.5"));
        assertThat(result.getAprobado()).isTrue();
    }

    @Test
    void registrarNota_FueraDeRango_LanzaExcepcion() {
        DetalleMatricula dm = new DetalleMatricula();
        dm.setId(1L);
        Docente doc = new Docente();
        doc.setId(1L);

        when(detalleMatriculaRepository.findById(1L)).thenReturn(Optional.of(dm));
        when(docenteRepository.findById(1L)).thenReturn(Optional.of(doc));

        assertThrows(ReglaNegocioException.class, () -> {
            notaService.registrarNota(1L, 1L, new BigDecimal("-1.0"));
        });

        assertThrows(ReglaNegocioException.class, () -> {
            notaService.registrarNota(1L, 1L, new BigDecimal("21.0"));
        });
    }
}
