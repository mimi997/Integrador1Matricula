package pe.edu.utp.matricula.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pe.edu.utp.matricula.entity.Docente;
import pe.edu.utp.matricula.entity.RolUsuario;
import pe.edu.utp.matricula.entity.Usuario;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DocenteRepositoryTest {

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void testFindByCodigoDocente() {
        Usuario user = new Usuario("doc1@utp.edu.pe", "hash", RolUsuario.DOCENTE, true);
        usuarioRepository.save(user);

        Docente doc = new Docente(user, "DOC001", "Sistemas");
        docenteRepository.save(doc);

        Optional<Docente> found = docenteRepository.findByCodigoDocente("DOC001");
        assertThat(found).isPresent();
        assertThat(found.get().getCodigoDocente()).isEqualTo("DOC001");
    }
}
