package pe.edu.utp.matricula.service.impl;

import org.springframework.stereotype.Service;
import pe.edu.utp.matricula.entity.Horario;
import pe.edu.utp.matricula.repository.HorarioRepository;
import pe.edu.utp.matricula.service.HorarioService;

import java.util.List;

@Service
public class HorarioServiceImpl implements HorarioService {

    private final HorarioRepository horarioRepository;

    public HorarioServiceImpl(HorarioRepository horarioRepository) {
        this.horarioRepository = horarioRepository;
    }

    @Override
    public List<Horario> listarHorariosPorCurso(Long cursoId) {
        return horarioRepository.findByCursoId(cursoId);
    }

    @Override
    public List<Horario> listarTodos() {
        return horarioRepository.findAll();
    }
}
