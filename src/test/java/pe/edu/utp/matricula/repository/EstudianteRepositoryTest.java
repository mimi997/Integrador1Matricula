package pe.edu.utp.matricula.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pe.edu.utp.matricula.entity.Estudiante;
import pe.edu.utp.matricula.entity.RolUsuario;
import pe.edu.utp.matricula.entity.Usuario;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EstudianteRepositoryTest {

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void testFindByCodigoEstudiante() {
        Usuario user = new Usuario("est1@utp.edu.pe", "hash", RolUsuario.ESTUDIANTE, true);
        usuarioRepository.save(user);

        Estudiante est = new Estudiante(user, "U20210001", "Sistemas", 1, 0);
        estudianteRepository.save(est);

        Optional<Estudiante> found = estudianteRepository.findByCodigoEstudiante("U20210001");
        assertThat(found).isPresent();
        assertThat(found.get().getCodigoEstudiante()).isEqualTo("U20210001");
    }

    @Test
    void testFindCursosAprobados() {
        // La consulta de cursos aprobados requiere Notas, DetalleMatricula, Matricula, Horario y Curso.
        // Simularemos guardando un estudiante y probando que la query funciona (aunque devuelva vacío inicialmente).
        Usuario user = new Usuario("est2@utp.edu.pe", "hash", RolUsuario.ESTUDIANTE, true);
        usuarioRepository.save(user);
        Estudiante est = new Estudiante(user, "U20210002", "Sistemas", 1, 0);
        estudianteRepository.save(est);

        assertThat(estudianteRepository.findCursosAprobados(est.getId())).isEmpty();
    }
}
