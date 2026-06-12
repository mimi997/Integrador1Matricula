package pe.edu.utp.matricula.service;

import pe.edu.utp.matricula.entity.Horario;
import java.util.List;

public interface HorarioService {
    List<Horario> listarHorariosPorCurso(Long cursoId);
    List<Horario> listarTodos();
}
