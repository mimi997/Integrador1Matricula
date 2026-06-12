package pe.edu.utp.matricula.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.matricula.entity.Curso;
import pe.edu.utp.matricula.repository.CursoRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private pe.edu.utp.matricula.service.impl.CursoServiceImpl cursoService;

    @Test
    void listarCursosActivos() {
        Curso c1 = new Curso("C1", "C1", 4, 1, "S", 40, true);
        when(cursoRepository.findByActivoTrue()).thenReturn(List.of(c1));

        List<Curso> result = cursoService.listarCursosActivos();
        assertThat(result).hasSize(1);
    }
}
