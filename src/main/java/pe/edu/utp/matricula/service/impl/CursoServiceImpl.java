package pe.edu.utp.matricula.service.impl;

import org.springframework.stereotype.Service;
import pe.edu.utp.matricula.entity.Curso;
import pe.edu.utp.matricula.repository.CursoRepository;
import pe.edu.utp.matricula.service.CursoService;

import java.util.List;
import java.util.Optional;

@Service
public class CursoServiceImpl implements CursoService {

    private final CursoRepository cursoRepository;

    public CursoServiceImpl(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    @Override
    public List<Curso> listarCursosActivos() {
        return cursoRepository.findByActivoTrue();
    }

    @Override
    public List<Curso> findAll() {
        return cursoRepository.findAll();
    }

    @Override
    public Optional<Curso> findById(Long id) {
        return cursoRepository.findById(id);
    }

    @Override
    public Curso guardarCurso(Curso curso) {
        return cursoRepository.save(curso);
    }

    @Override
    public void eliminarCurso(Long id) {
        cursoRepository.findById(id).ifPresent(c -> {
            c.setActivo(false);
            cursoRepository.save(c);
        });
    }
}
