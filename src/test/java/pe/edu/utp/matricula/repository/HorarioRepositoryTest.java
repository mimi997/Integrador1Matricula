package pe.edu.utp.matricula.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pe.edu.utp.matricula.entity.Curso;
import pe.edu.utp.matricula.entity.Docente;
import pe.edu.utp.matricula.entity.Horario;
import pe.edu.utp.matricula.entity.RolUsuario;
import pe.edu.utp.matricula.entity.Usuario;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class HorarioRepositoryTest {

    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void testFindByCursoId() {
        Usuario u = new Usuario("doc@utp.edu.pe", "hash", RolUsuario.DOCENTE, true);
        usuarioRepository.save(u);
        Docente d = new Docente(u, "D1", "Sistemas");
        docenteRepository.save(d);

        Curso c = new Curso("C1", "Curso 1", 4, 1, "Sistemas", 40, true);
        cursoRepository.save(c);

        Horario h = new Horario(c, d, "Lunes", "08:00-10:00", "A1");
        horarioRepository.save(h);

        List<Horario> horarios = horarioRepository.findByCursoId(c.getId());
        assertThat(horarios).hasSize(1);
    }
}
