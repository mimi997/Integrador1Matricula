package pe.edu.utp.matricula.util;

public final class Constantes {

    private Constantes() {
        // Prevent instantiation
    }

    public static final String ESTADO_MATRICULA_PENDIENTE = "PENDIENTE";
    public static final String ESTADO_MATRICULA_CONFIRMADA = "CONFIRMADA";
    public static final String ESTADO_MATRICULA_ANULADA = "ANULADA";

    public static final String ROL_ADMIN = "ADMIN";
    public static final String ROL_ESTUDIANTE = "ESTUDIANTE";
    public static final String ROL_DOCENTE = "DOCENTE";

    public static final String ESTADO_PREREQ_CUMPLE = "CUMPLE";
    public static final String ESTADO_PREREQ_NO_CUMPLE = "NO_CUMPLE";

    public static final int MAX_CREDITOS_POR_PERIODO = 24;
}
