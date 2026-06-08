package pe.edu.utp.matricula.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PeriodoServiceTest {

    @Test
    public void testPeriodoInicial() {
        PeriodoService periodoService = new PeriodoService();
        assertEquals("2026-1", periodoService.getPeriodoActual());
        assertTrue(periodoService.isActivo());
    }

    @Test
    public void testCambioPeriodo() {
        PeriodoService periodoService = new PeriodoService();
        periodoService.setPeriodoActual("2026-2");
        periodoService.setActivo(false);
        
        assertEquals("2026-2", periodoService.getPeriodoActual());
        assertFalse(periodoService.isActivo());
    }
}
