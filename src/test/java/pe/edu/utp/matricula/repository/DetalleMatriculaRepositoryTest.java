package pe.edu.utp.matricula.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pe.edu.utp.matricula.entity.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DetalleMatriculaRepositoryTest {

    @Autowired
    private DetalleMatriculaRepository detalleMatriculaRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void testFindByMatriculaId() {
        Usuario ue = new Usuario("est@utp.edu.pe", "hash", RolUsuario.ESTUDIANTE, true);
        usuarioRepository.save(ue);
        Estudiante est = new Estudiante(ue, "U1", "Sistemas", 1, 0);
        estudianteRepository.save(est);

        Usuario ud = new Usuario("doc@utp.edu.pe", "hash", RolUsuario.DOCENTE, true);
        usuarioRepository.save(ud);
        Docente doc = new Docente(ud, "D1", "Sistemas");
        docenteRepository.save(doc);

        Curso c = new Curso("C1", "C1", 4, 1, "Sistemas", 40, true);
        cursoRepository.save(c);

        Horario h = new Horario(c, doc, "L", "8-10", "A1");
        horarioRepository.save(h);

        Matricula m = new Matricula(est, "2023-1", "CONFIRMADA", null);
        matriculaRepository.save(m);

        DetalleMatricula dm = new DetalleMatricula(m, h, "CUMPLE");
        detalleMatriculaRepository.save(dm);

        List<DetalleMatricula> detalles = detalleMatriculaRepository.findByMatriculaId(m.getId());
        assertThat(detalles).hasSize(1);
    }
}
