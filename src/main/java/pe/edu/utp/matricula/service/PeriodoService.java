package pe.edu.utp.matricula.service;

public interface PeriodoService {
    String getPeriodoActual();
    void setPeriodoActual(String periodoActual);
    boolean isActivo();
    void setActivo(boolean activo);
}
