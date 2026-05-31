package pe.edu.utp.matricula.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pe.edu.utp.matricula.entity.Curso;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CursoRepositoryTest {

    @Autowired
    private CursoRepository cursoRepository;

    @Test
    void testFindByCarreraAndCiclo() {
        Curso c1 = new Curso("MAT1", "Mate I", 4, 1, "Sistemas", 40, true);
        Curso c2 = new Curso("FIS1", "Física I", 4, 1, "Sistemas", 40, true);
        cursoRepository.save(c1);
        cursoRepository.save(c2);

        List<Curso> cursos = cursoRepository.findByCarreraAndCiclo("Sistemas", 1);
        assertThat(cursos).hasSize(2);
    }

    @Test
    void testFindByActivoTrue() {
        Curso c1 = new Curso("MAT1", "Mate I", 4, 1, "Sistemas", 40, true);
        Curso c2 = new Curso("FIS1", "Física I", 4, 1, "Sistemas", 40, false);
        cursoRepository.save(c1);
        cursoRepository.save(c2);

        List<Curso> cursos = cursoRepository.findByActivoTrue();
        assertThat(cursos).hasSize(1);
        assertThat(cursos.get(0).getCodigo()).isEqualTo("MAT1");
    }
}
