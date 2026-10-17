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
    public Usuario autenticar(String correo, String password) {
        Usuario usr = null;
        
        // Buscamos al usuario y validamos que su cuenta esté Activa
        String sql = "SELECT id, nombre, correo, password, id_rol, estado_cuenta "
                   + "FROM usuarios "
                   + "WHERE correo = ? AND password = ? AND estado_cuenta = 'Activo'";

        // El try-with-resources cierra automáticamente la conexión, el statement y el resultset
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            // Asignamos los valores que el usuario escribió en la pantalla
            ps.setString(1, correo);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                // Si la consulta devuelve una fila, las credenciales son correctas
                if (rs.next()) {
                    usr = new Usuario();
                    usr.setId(rs.getInt("id"));
                    usr.setNombre(rs.getString("nombre"));
                    usr.setCorreo(rs.getString("correo"));
                    usr.setPassword(rs.getString("password"));
                    usr.setIdRol(rs.getInt("id_rol"));
                    usr.setEstadoCuenta(rs.getString("estado_cuenta"));
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error en UsuarioDAO.autenticar: " + e.getMessage());
        }

        // Retorna el usuario lleno si tuvo éxito, o null si falló el login
        return usr;
    }
    // Método READ: Obtener todos los usuarios registrados
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, correo, password, id_rol, ultimo_acceso, intentos_fallidos, estado_cuenta FROM usuarios";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario usr = new Usuario();
                usr.setId(rs.getInt("id"));
                usr.setNombre(rs.getString("nombre"));
                usr.setCorreo(rs.getString("correo"));
                usr.setPassword(rs.getString("password"));
                usr.setIdRol(rs.getInt("id_rol"));
                usr.setUltimoAcceso(rs.getTimestamp("ultimo_acceso"));
                usr.setIntentosFallidos(rs.getInt("intentos_fallidos"));
                usr.setEstadoCuenta(rs.getString("estado_cuenta"));
                
                lista.add(usr);
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
}