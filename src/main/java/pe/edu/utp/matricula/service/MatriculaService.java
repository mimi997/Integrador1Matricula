package pe.edu.utp.matricula.service;

import pe.edu.utp.matricula.entity.Matricula;
import java.util.List;

public interface MatriculaService {
    Matricula matricular(Long estudianteId, List<Long> horarioIds, String periodo);
    void cancelarDetalle(Long estudianteId, Long detalleId);
}
