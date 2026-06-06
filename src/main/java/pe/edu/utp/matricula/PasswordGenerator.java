package pe.edu.utp.matricula;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("HASH FOR 123456: " + encoder.encode("123456"));
        System.out.println("MATCHES 123456: " + encoder.matches("123456", "$2a$10$KQh86gXxTSVRDXsvxgPOC.u2bOnt5tCzUVn4ESlAtHkHHln92bbd6"));
        System.out.println("MATCHES password: " + encoder.matches("password", "$2a$10$KQh86gXxTSVRDXsvxgPOC.u2bOnt5tCzUVn4ESlAtHkHHln92bbd6"));
        System.out.println("MATCHES 1234: " + encoder.matches("1234", "$2a$10$KQh86gXxTSVRDXsvxgPOC.u2bOnt5tCzUVn4ESlAtHkHHln92bbd6"));
        System.out.println("MATCHES admin: " + encoder.matches("admin", "$2a$10$KQh86gXxTSVRDXsvxgPOC.u2bOnt5tCzUVn4ESlAtHkHHln92bbd6"));
    }
}
