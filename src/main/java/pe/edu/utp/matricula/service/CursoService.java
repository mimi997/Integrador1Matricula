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
}
