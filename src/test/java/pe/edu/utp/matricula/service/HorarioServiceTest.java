package pe.edu.utp.matricula.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.matricula.entity.Horario;
import pe.edu.utp.matricula.repository.HorarioRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HorarioServiceTest {

    @Mock
    private HorarioRepository horarioRepository;

    @InjectMocks
    private pe.edu.utp.matricula.service.impl.HorarioServiceImpl horarioService;

    @Test
    void listarHorariosPorCurso() {
        Horario h = new Horario();
        h.setId(1L);
        when(horarioRepository.findByCursoId(1L)).thenReturn(List.of(h));

        List<Horario> result = horarioService.listarHorariosPorCurso(1L);
        assertThat(result).hasSize(1);
    }
}
