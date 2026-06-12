package pe.edu.utp.matricula.util;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.utp.matricula.entity.Prerrequisito;
import pe.edu.utp.matricula.repository.PrerrequisitoRepository;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CachePrerrequisitosTest {

    @Mock
    private PrerrequisitoRepository prerrequisitoRepository;

    @Test
    public void testGetPrerrequisitosCachesValue() {
        when(prerrequisitoRepository.findByCursoId(1L)).thenReturn(Collections.emptyList());

        CachePrerrequisitos cache = new CachePrerrequisitos(prerrequisitoRepository);

        // Primera llamada - golpea el repo
        ImmutableList<Prerrequisito> list1 = cache.getPrerrequisitos(1L);
        // Segunda llamada - golpea la caché
        ImmutableList<Prerrequisito> list2 = cache.getPrerrequisitos(1L);

        assertThat(list1).isEmpty();
        assertThat(list2).isEmpty();

        // Verificar que findByCursoId sólo se llamó 1 vez
        verify(prerrequisitoRepository, times(1)).findByCursoId(1L);
    }
}
