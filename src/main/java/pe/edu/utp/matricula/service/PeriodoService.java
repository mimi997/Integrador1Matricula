package pe.edu.utp.matricula.service;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PeriodoService {

    private static final Logger log = LoggerFactory.getLogger(PeriodoService.class);
    private String periodoActual = "2026-I";
    private boolean activo = true;

    public String getPeriodoActual() {
        return periodoActual;
    }

    public void setPeriodoActual(String periodoActual) {
        log.info("Período académico cambiado a: {}", periodoActual);
        this.periodoActual = periodoActual;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        log.info("Estado del período académico cambiado a: {}", activo ? "ACTIVO" : "CERRADO");
        this.activo = activo;
    }
}
