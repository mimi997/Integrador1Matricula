package pe.edu.utp.matricula;
import java.sql.*;
public class WipeDb {
    public static void main(String[] args) throws Exception {
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/matricula_utp", "root", "1234");
        c.createStatement().execute("DROP DATABASE matricula_utp");
        c.createStatement().execute("CREATE DATABASE matricula_utp");
        c.close();
    }
}
