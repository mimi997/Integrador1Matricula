package pe.edu.utp.matricula.service;

import org.springframework.stereotype.Service;
import pe.edu.utp.matricula.entity.Curso;
import pe.edu.utp.matricula.repository.CursoRepository;

import java.util.List;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public List<Curso> listarCursosActivos() {
        return cursoRepository.findByActivoTrue();
    }

    public List<Curso> findAll() {
        return cursoRepository.findAll();
    }

    public java.util.Optional<Curso> findById(Long id) {
        return cursoRepository.findById(id);
    }

    public Curso guardarCurso(Curso curso) {
        return cursoRepository.save(curso);
    }

    public void eliminarCurso(Long id) {
        cursoRepository.findById(id).ifPresent(c -> {
            c.setActivo(false);
            cursoRepository.save(c);
        });
    }
}
