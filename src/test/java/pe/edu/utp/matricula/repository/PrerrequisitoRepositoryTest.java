package pe.edu.utp.matricula.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pe.edu.utp.matricula.entity.Curso;
import pe.edu.utp.matricula.entity.Prerrequisito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PrerrequisitoRepositoryTest {

    @Autowired
    private PrerrequisitoRepository prerrequisitoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Test
    void testFindByCursoId() {
        Curso mate1 = new Curso("MAT1", "Mate I", 4, 1, "Sistemas", 40, true);
        Curso mate2 = new Curso("MAT2", "Mate II", 4, 2, "Sistemas", 40, true);
        cursoRepository.save(mate1);
        cursoRepository.save(mate2);

        Prerrequisito pre = new Prerrequisito(mate2, mate1);
        prerrequisitoRepository.save(pre);

        List<Prerrequisito> found = prerrequisitoRepository.findByCursoId(mate2.getId());
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getCursoPrereq().getCodigo()).isEqualTo("MAT1");
    }
}
