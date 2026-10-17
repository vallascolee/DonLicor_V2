package controlador;

import configuracion.Conexion;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.sql.Connection;

@Named
@RequestScoped
public class TestBean {

    public String getEstadoConexion() {
        try {
            Connection con = Conexion.getConexion();
            if (con != null) {
                con.close(); // Cerramos la conexión de prueba para no saturar la BD
                return "¡Éxito! Payara Server está conectado a la BD don_licor_v2 🚀";
            }
        } catch (Exception e) {
            return "Fallo en la conexión: " + e.getMessage();
        }
        return "No se pudo establecer la conexión. Revisa las credenciales.";
    }
}