package pe.edu.utp.matricula.service;

import pe.edu.utp.matricula.entity.Horario;
import java.util.List;

public interface DetectorConflictoHorario {
    boolean hayConflicto(Horario nuevo, List<Horario> existentes);
}
