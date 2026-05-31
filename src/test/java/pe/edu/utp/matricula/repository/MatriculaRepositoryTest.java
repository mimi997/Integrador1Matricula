package pe.edu.utp.matricula.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pe.edu.utp.matricula.entity.Estudiante;
import pe.edu.utp.matricula.entity.Matricula;
import pe.edu.utp.matricula.entity.RolUsuario;
import pe.edu.utp.matricula.entity.Usuario;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MatriculaRepositoryTest {

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void testFindByEstudianteIdAndPeriodo() {
        Usuario user = new Usuario("est@utp.edu.pe", "hash", RolUsuario.ESTUDIANTE, true);
        usuarioRepository.save(user);

        Estudiante est = new Estudiante(user, "U20210001", "Sistemas", 1, 0);
        estudianteRepository.save(est);

        Matricula mat = new Matricula(est, "2023-1", "CONFIRMADA", null);
        matriculaRepository.save(mat);

        Optional<Matricula> found = matriculaRepository.findByEstudianteIdAndPeriodo(est.getId(), "2023-1");
        assertThat(found).isPresent();
    }

    @Test
    void testExistsByEstudianteIdAndPeriodo() {
        Usuario user = new Usuario("est2@utp.edu.pe", "hash", RolUsuario.ESTUDIANTE, true);
        usuarioRepository.save(user);

        Estudiante est = new Estudiante(user, "U20210002", "Sistemas", 1, 0);
        estudianteRepository.save(est);

        Matricula mat = new Matricula(est, "2023-1", "CONFIRMADA", null);
        matriculaRepository.save(mat);

        boolean exists = matriculaRepository.existsByEstudianteIdAndPeriodo(est.getId(), "2023-1");
        assertThat(exists).isTrue();

        boolean notExists = matriculaRepository.existsByEstudianteIdAndPeriodo(est.getId(), "2023-2");
        assertThat(notExists).isFalse();
    }
}
