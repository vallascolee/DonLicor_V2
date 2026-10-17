package configuracion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    
    // Recuerda cambiar "tu_contraseña_aqui" por la clave real de tu MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/don_licor_v2?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "12345678";

    public static Connection getConexion() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("¡Conexión exitosa a don_licor_v2!");
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Driver MySQL no encontrado - " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error: Fallo en la conexión a la Base de Datos - " + e.getMessage());
        }
        return con;
    }
}