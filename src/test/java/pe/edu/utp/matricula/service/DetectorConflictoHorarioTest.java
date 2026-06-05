package pe.edu.utp.matricula.service;

import org.junit.jupiter.api.Test;
import pe.edu.utp.matricula.entity.Horario;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DetectorConflictoHorarioTest {

    private final DetectorConflictoHorario detector = new DetectorConflictoHorario();

    @Test
    void hayConflicto_SinConflictoAdyacente() {
        Horario h1 = new Horario(null, null, "Lunes", "08:00-10:00", "A");
        Horario h2 = new Horario(null, null, "Lunes", "10:00-12:00", "B");

        boolean result = detector.hayConflicto(h1, List.of(h2));
        assertThat(result).isFalse();
    }

    @Test
    void hayConflicto_DiasDiferentes() {
        Horario h1 = new Horario(null, null, "Lunes", "08:00-10:00", "A");
        Horario h2 = new Horario(null, null, "Martes", "08:00-10:00", "B");

        boolean result = detector.hayConflicto(h1, List.of(h2));
        assertThat(result).isFalse();
    }

    @Test
    void hayConflicto_SolapamientoTotal() {
        Horario h1 = new Horario(null, null, "Lunes", "08:00-10:00", "A");
        Horario h2 = new Horario(null, null, "Lunes", "08:00-10:00", "B");

        boolean result = detector.hayConflicto(h1, List.of(h2));
        assertThat(result).isTrue();
    }

    @Test
    void hayConflicto_SolapamientoParcial() {
        Horario h1 = new Horario(null, null, "Lunes", "08:00-10:00", "A");
        Horario h2 = new Horario(null, null, "Lunes", "09:00-11:00", "B");

        boolean result = detector.hayConflicto(h1, List.of(h2));
        assertThat(result).isTrue();
    }
}
