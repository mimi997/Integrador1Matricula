package pe.edu.utp.matricula.service.impl;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pe.edu.utp.matricula.service.PeriodoService;

@Service
public class PeriodoServiceImpl implements PeriodoService {

    private static final Logger log = LoggerFactory.getLogger(PeriodoServiceImpl.class);
    private String periodoActual = "2026-1";
    private boolean activo = true;

    @Override
    public String getPeriodoActual() {
        return periodoActual;
    }

    @Override
    public void setPeriodoActual(String periodoActual) {
        log.info("Período académico cambiado a: {}", periodoActual);
        this.periodoActual = periodoActual;
    }

    @Override
    public boolean isActivo() {
        return activo;
    }

    @Override
    public void setActivo(boolean activo) {
        log.info("Estado del período académico cambiado a: {}", activo ? "ACTIVO" : "CERRADO");
        this.activo = activo;
    }
}
