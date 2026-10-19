package dao;

import configuracion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import modelo.Usuario;

public class UsuarioDAO {

    // Método para validar las credenciales del usuario
    // 1. MÉTODO DE AUTENTICACIÓN MEJORADO
    public Usuario autenticar(String correo, String password) {
        Usuario usr = null;
        // Buscamos SOLO por correo primero
        String sql = "SELECT id, nombre, correo, password, id_rol, ultimo_acceso, intentos_fallidos, estado_cuenta FROM usuarios WHERE correo = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                
                if (rs.next()) {
                    String estado = rs.getString("estado_cuenta");
                    
                    // A. Verificar si la cuenta ya está bloqueada o inactiva
                    if ("Bloqueado".equals(estado) || "Inactivo".equals(estado)) {
                        System.out.println("Acceso denegado: Cuenta " + correo + " está " + estado);
                        return null; // Rechazar acceso inmediatamente
                    }

                    String dbPassword = rs.getString("password");
                    int intentosActuales = rs.getInt("intentos_fallidos");

                    // B. Validar la contraseña
                    if (dbPassword.equals(password)) {
                        // EXITO: Reseteamos intentos a 0 y guardamos la fecha de acceso
                        registrarAccesoExitoso(correo);
                        
                        usr = new Usuario();
                        usr.setId(rs.getInt("id"));
                        usr.setNombre(rs.getString("nombre"));
                        usr.setCorreo(rs.getString("correo"));
                        usr.setIdRol(rs.getInt("id_rol"));
                        usr.setEstadoCuenta(rs.getString("estado_cuenta"));
                    } else {
                        // ERROR: Contraseña incorrecta. Aumentamos los intentos fallidos
                        aumentarIntentoFallido(correo, intentosActuales + 1);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error en UsuarioDAO.autenticar: " + e.getMessage());
        }

        return usr;
    }

    // 2. MÉTODO AUXILIAR: Éxito (Resetea a 0 y actualiza 'ultimo_acceso')
    private void registrarAccesoExitoso(String correo) {
        // NOW() es una función de MySQL que obtiene la fecha y hora actual automáticamente
        String sql = "UPDATE usuarios SET intentos_fallidos = 0, ultimo_acceso = NOW() WHERE correo = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error reseteando intentos: " + e.getMessage());
        }
    }

    // 3. MÉTODO AUXILIAR: Fallo (Suma intentos y bloquea si llega a 3)
   
    private void aumentarIntentoFallido(String correo, int nuevosIntentos) {
        // Operador ternario: si llega a 3, cambia a 'Bloqueado', si no, se queda 'Activo'
        String estado = (nuevosIntentos >= 3) ? "Bloqueado" : "Activo";
        String sql = "UPDATE usuarios SET intentos_fallidos = ?, estado_cuenta = ? WHERE correo = ?";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, nuevosIntentos);
            ps.setString(2, estado);
            ps.setString(3, correo); // <-- ¡ESTA ES LA LÍNEA VITAL QUE FALTABA!
            
            ps.executeUpdate();
            
            if (nuevosIntentos >= 3) {
                System.out.println("¡ALERTA DE SEGURIDAD! Cuenta " + correo + " bloqueada por exceder 3 intentos.");
            }
        } catch (Exception e) {
            System.out.println("Error aumentando intentos: " + e.getMessage());
        }
    }
    // Método READ: Obtener todos los usuarios registrados
public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        
        // Unimos usuarios (u) con roles (r) y mantenemos tu formato de fecha
        String sql = "SELECT u.id, u.nombre, u.correo, u.id_rol, u.estado_cuenta, "
                   + "DATE_FORMAT(u.ultimo_acceso, '%d/%m/%Y %H:%i:%s') AS ultimo_acceso_fmt, "
                   + "r.nombre AS nombre_rol "
                   + "FROM usuarios u "
                   + "INNER JOIN roles r ON u.id_rol = r.id";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNombre(rs.getString("nombre"));
                u.setCorreo(rs.getString("correo"));
                u.setIdRol(rs.getInt("id_rol"));
                u.setEstadoCuenta(rs.getString("estado_cuenta"));
                
                // Capturamos el texto del cargo gracias al INNER JOIN
                u.setNombreRol(rs.getString("nombre_rol"));
                
                // Capturamos la fecha formateada. Si es null, mostramos un mensaje
                String fecha = rs.getString("ultimo_acceso_fmt");
                u.setUltimoAcceso(fecha != null ? fecha : "Nunca ha ingresado");
                
                lista.add(u);
            }
        } catch (Exception e) {
            System.out.println("Error en UsuarioDAO.listarTodos: " + e.getMessage());
        }
        return lista;
    }
    // Método CREATE: Registrar un nuevo usuario en la base de datos
    public void registrar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, correo, password, id_rol, intentos_fallidos, estado_cuenta) VALUES (?, ?, ?, ?, 0, 'Activo')";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, usuario.getPassword());
            ps.setInt(4, usuario.getIdRol()); // 1=Admin, 2=Vendedor, etc.
            
            ps.executeUpdate();
            System.out.println("Usuario registrado con éxito.");
            
        } catch (Exception e) {
            System.out.println("Error en UsuarioDAO.registrar: " + e.getMessage());
        }
    }
    // Método DELETE: Eliminar un usuario de la base de datos por su ID
    public void eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Usuario eliminado con éxito.");
            
        } catch (Exception e) {
            System.out.println("Error en UsuarioDAO.eliminar: " + e.getMessage());
        }
    }
    // Método UPDATE: Actualizar los datos de un usuario existente
    public void actualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre=?, correo=?, password=?, id_rol=?, estado_cuenta=? WHERE id=?";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, usuario.getPassword());
            ps.setInt(4, usuario.getIdRol());
            ps.setString(5, usuario.getEstadoCuenta());
            ps.setInt(6, usuario.getId()); // El ID para saber a quién actualizar
            
            ps.executeUpdate();
            System.out.println("Usuario actualizado con éxito.");
            
        } catch (Exception e) {
            System.out.println("Error en UsuarioDAO.actualizar: " + e.getMessage());
        }
    }
}