package pe.edu.utp.matricula.service;

import org.springframework.stereotype.Service;
import pe.edu.utp.matricula.entity.Horario;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class DetectorConflictoHorario {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public boolean hayConflicto(Horario nuevo, List<Horario> existentes) {
        String[] nuevoHoras = nuevo.getHoras().split("-");
        LocalTime nuevoInicio = LocalTime.parse(nuevoHoras[0], FORMATTER);
        LocalTime nuevoFin = LocalTime.parse(nuevoHoras[1], FORMATTER);

        for (Horario ext : existentes) {
            if (ext.getDia().equalsIgnoreCase(nuevo.getDia())) {
                String[] extHoras = ext.getHoras().split("-");
                LocalTime extInicio = LocalTime.parse(extHoras[0], FORMATTER);
                LocalTime extFin = LocalTime.parse(extHoras[1], FORMATTER);

                // Solapamiento: inicio1 < fin2 && inicio2 < fin1
                if (nuevoInicio.isBefore(extFin) && extInicio.isBefore(nuevoFin)) {
                    return true;
                }
            }
        }
        return false;
    }
}
