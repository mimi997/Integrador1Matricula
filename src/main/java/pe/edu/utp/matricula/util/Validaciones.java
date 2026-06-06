package pe.edu.utp.matricula.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

public class Validaciones {

    public static String normalizar(String texto) {
        if (StringUtils.isBlank(texto)) {
            return "";
        }
        return StringUtils.normalizeSpace(texto).toUpperCase();
    }

    public static void requireValidEmail(String email) {
        Validate.notBlank(email, "El email no puede estar vacío");
        Validate.isTrue(email.contains("@") && email.endsWith(".edu.pe"), "Debe ser un correo institucional válido");
    }
}
