package pe.edu.utp.matricula.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ReporteService {
    long getCursosAlLimite();
    double getTasaAprobacion();
    long getEstudiantesRiesgo();
    List<Map<String, Object>> getCursosMasDemandados();
    byte[] exportarEstudiantes() throws IOException;
    byte[] exportarCursos() throws IOException;
    byte[] exportarMatriculas() throws IOException;
}
