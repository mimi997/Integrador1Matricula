package pe.edu.utp.matricula.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pe.edu.utp.matricula.entity.RolUsuario;
import pe.edu.utp.matricula.entity.Usuario;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void testFindByEmail() {
        Usuario user = new Usuario("test@utp.edu.pe", "hash", RolUsuario.ESTUDIANTE, true);
        usuarioRepository.save(user);

        Optional<Usuario> found = usuarioRepository.findByEmail("test@utp.edu.pe");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@utp.edu.pe");
    }

    @Test
    void testFindByEmailAndActivoTrue() {
        Usuario user = new Usuario("activo@utp.edu.pe", "hash", RolUsuario.DOCENTE, true);
        usuarioRepository.save(user);

        Usuario userInactivo = new Usuario("inactivo@utp.edu.pe", "hash", RolUsuario.ESTUDIANTE, false);
        usuarioRepository.save(userInactivo);

        assertThat(usuarioRepository.findByEmailAndActivoTrue("activo@utp.edu.pe")).isPresent();
        assertThat(usuarioRepository.findByEmailAndActivoTrue("inactivo@utp.edu.pe")).isEmpty();
    }
}
