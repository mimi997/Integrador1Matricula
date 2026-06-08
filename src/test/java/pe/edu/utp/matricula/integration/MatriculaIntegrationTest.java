package pe.edu.utp.matricula.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pe.edu.utp.matricula.entity.Curso;
import pe.edu.utp.matricula.entity.Usuario;
import pe.edu.utp.matricula.entity.Estudiante;
import pe.edu.utp.matricula.repository.CursoRepository;
import pe.edu.utp.matricula.repository.UsuarioRepository;
import pe.edu.utp.matricula.repository.EstudianteRepository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test") // Para usar application-test.properties (H2 DB)
public class MatriculaIntegrationTest {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Test
    public void contextLoads() {
        assertNotNull(cursoRepository);
        assertNotNull(usuarioRepository);
        assertNotNull(estudianteRepository);
    }
}
