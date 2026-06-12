package pe.edu.utp.matricula.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidacionesTest {

    @Test
    public void testNormalizar() {
        assertThat(Validaciones.normalizar(null)).isEqualTo("");
        assertThat(Validaciones.normalizar("")).isEqualTo("");
        assertThat(Validaciones.normalizar("  ")).isEqualTo("");
        assertThat(Validaciones.normalizar("  hola   mundo  ")).isEqualTo("HOLA MUNDO");
        assertThat(Validaciones.normalizar("ingenieria de sistemas")).isEqualTo("INGENIERIA DE SISTEMAS");
    }

    @Test
    public void testRequireValidEmail_Valido() {
        // No debe lanzar excepción
        Validaciones.requireValidEmail("estudiante@utp.edu.pe");
    }

    @Test
    public void testRequireValidEmail_Vacio() {
        assertThrows(NullPointerException.class, () -> {
            Validaciones.requireValidEmail(null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Validaciones.requireValidEmail("");
        });
    }

    @Test
    public void testRequireValidEmail_SinArroba() {
        assertThrows(IllegalArgumentException.class, () -> {
            Validaciones.requireValidEmail("estudianteutp.edu.pe");
        });
    }

    @Test
    public void testRequireValidEmail_NoInstitucional() {
        assertThrows(IllegalArgumentException.class, () -> {
            Validaciones.requireValidEmail("estudiante@gmail.com");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Validaciones.requireValidEmail("estudiante@utp.edu");
        });
    }
}
