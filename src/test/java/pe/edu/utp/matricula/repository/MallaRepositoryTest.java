package pe.edu.utp.matricula.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pe.edu.utp.matricula.entity.Curso;
import pe.edu.utp.matricula.entity.MallaCurricular;
import pe.edu.utp.matricula.entity.MallaCurso;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MallaRepositoryTest {

    @Autowired
    private MallaCurricularRepository mallaCurricularRepository;

    @Autowired
    private MallaCursoRepository mallaCursoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Test
    void testFindCursosByMalla() {
        MallaCurricular malla = new MallaCurricular("Sistemas", 2021);
        mallaCurricularRepository.save(malla);

        Curso c = new Curso("C1", "C1", 4, 1, "Sistemas", 40, true);
        cursoRepository.save(c);

        MallaCurso mc = new MallaCurso(malla, c, 1);
        mallaCursoRepository.save(mc);

        List<MallaCurso> cursosMalla = mallaCursoRepository.findByMallaId(malla.getId());
        assertThat(cursosMalla).hasSize(1);
        assertThat(cursosMalla.get(0).getCurso().getCodigo()).isEqualTo("C1");
    }
}
