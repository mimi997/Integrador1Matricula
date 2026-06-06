package pe.edu.utp.matricula;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("Matches 'password': " + encoder.matches("password", "$2a$10$wY1tvvqKkI6M7y9M.T0eN.92G6r/1.C1K7tZ9j2B6mQ4uH1H1x98i"));
        System.out.println("Matches '123456': " + encoder.matches("123456", "$2a$10$wY1tvvqKkI6M7y9M.T0eN.92G6r/1.C1K7tZ9j2B6mQ4uH1H1x98i"));
        System.out.println("Matches 'admin': " + encoder.matches("admin", "$2a$10$wY1tvvqKkI6M7y9M.T0eN.92G6r/1.C1K7tZ9j2B6mQ4uH1H1x98i"));
        System.out.println("Matches '1234': " + encoder.matches("1234", "$2a$10$wY1tvvqKkI6M7y9M.T0eN.92G6r/1.C1K7tZ9j2B6mQ4uH1H1x98i"));
        
        System.out.println("New hash for 123456: " + encoder.encode("123456"));
    }
}
