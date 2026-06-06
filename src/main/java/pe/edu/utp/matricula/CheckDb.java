package pe.edu.utp.matricula;
import java.sql.*;
public class CheckDb {
    public static void main(String[] args) throws Exception {
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/matricula_utp", "root", "1234");
        ResultSet rs = c.createStatement().executeQuery("SELECT email, password_hash FROM usuario");
        while(rs.next()) {
            System.out.println(rs.getString(1) + " : " + rs.getString(2));
        }
        c.close();
    }
}
