package pe.edu.utp.matricula.service;

import pe.edu.utp.matricula.entity.Nota;
import java.math.BigDecimal;
import java.util.List;

public interface NotaService {
    Nota registrarNota(Long detalleMatriculaId, Long docenteId, BigDecimal valor);
    List<Nota> buscarPorEstudiante(Long estudianteId);
}
