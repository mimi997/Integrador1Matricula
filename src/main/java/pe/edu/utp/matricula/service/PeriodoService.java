package pe.edu.utp.matricula.service;

import org.springframework.stereotype.Service;

@Service
public class PeriodoService {

    private String periodoActual = "2026-1";
    private boolean activo = true;

    public String getPeriodoActual() {
        return periodoActual;
    }

    public void setPeriodoActual(String periodoActual) {
        this.periodoActual = periodoActual;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
