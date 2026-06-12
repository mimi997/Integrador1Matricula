package pe.edu.utp.matricula.service;

import pe.edu.utp.matricula.entity.Curso;
import java.util.List;
import java.util.Optional;

public interface CursoService {
    List<Curso> listarCursosActivos();
    List<Curso> findAll();
    Optional<Curso> findById(Long id);
    Curso guardarCurso(Curso curso);
    void eliminarCurso(Long id);
}
