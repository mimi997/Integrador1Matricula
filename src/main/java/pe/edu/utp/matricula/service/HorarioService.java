package pe.edu.utp.matricula.service;

import org.springframework.stereotype.Service;
import pe.edu.utp.matricula.entity.Horario;
import pe.edu.utp.matricula.repository.HorarioRepository;

import java.util.List;

@Service
public class HorarioService {

    private final HorarioRepository horarioRepository;

    public HorarioService(HorarioRepository horarioRepository) {
        this.horarioRepository = horarioRepository;
    }

    public List<Horario> listarHorariosPorCurso(Long cursoId) {
        return horarioRepository.findByCursoId(cursoId);
    }
}
