package dao;

import configuracion.Conexion;
import modelo.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    // 1. LISTAR TODOS (Con INNER JOIN para cruzar tablas y obtener nombres reales)
    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.id, p.nombre, p.id_categoria, p.id_subcategoria, p.id_variedad, p.id_proveedor, "
                   + "p.perfil_sabor, p.precio, p.stock_total, "
                   + "c.nombre AS nombre_categoria, "
                   + "s.nombre AS nombre_subcategoria, "
                   + "pr.nombre AS nombre_proveedor "
                   + "FROM productos p "
                   + "INNER JOIN categorias c ON p.id_categoria = c.id "
                   + "INNER JOIN subcategorias s ON p.id_subcategoria = s.id "
                   + "INNER JOIN proveedores pr ON p.id_proveedor = pr.id";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setIdCategoria(rs.getInt("id_categoria"));
                p.setIdSubcategoria(rs.getInt("id_subcategoria"));
                
                // Manejo especial de prevención de errores porque id_variedad puede estar vacío en tu BD
                int idVar = rs.getInt("id_variedad");
                if (!rs.wasNull()) {
                    p.setIdVariedad(idVar);
                }
                
                p.setIdProveedor(rs.getInt("id_proveedor"));
                p.setPerfilSabor(rs.getString("perfil_sabor"));
                p.setPrecio(rs.getDouble("precio"));
                p.setStockTotal(rs.getInt("stock_total"));
                
                // Nombres capturados por las relaciones JOIN para usarlos en el .xhtml
                p.setNombreCategoria(rs.getString("nombre_categoria"));
                p.setNombreSubcategoria(rs.getString("nombre_subcategoria"));
                p.setNombreProveedor(rs.getString("nombre_proveedor"));

                lista.add(p);
            }
        } catch (Exception e) {
            System.out.println("Error en ProductoDAO.listarTodos: " + e.getMessage());
        }
        return lista;
    }

    // 2. INSERTAR NUEVO PRODUCTO
    public void insertar(Producto p) {
        String sql = "INSERT INTO productos (nombre, id_categoria, id_subcategoria, id_variedad, id_proveedor, perfil_sabor, precio, stock_total) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, p.getNombre());
            ps.setInt(2, p.getIdCategoria());
            ps.setInt(3, p.getIdSubcategoria());
            
            // Si el admin no eligió variedad, enviamos un NULL puro a MySQL
            if (p.getIdVariedad() != null) {
                ps.setInt(4, p.getIdVariedad());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            
            ps.setInt(5, p.getIdProveedor());
            ps.setString(6, p.getPerfilSabor());
            ps.setDouble(7, p.getPrecio());
            ps.setInt(8, p.getStockTotal());
            
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error en ProductoDAO.insertar: " + e.getMessage());
        }
    }

    // 3. ACTUALIZAR PRODUCTO EXISTENTE
    public void actualizar(Producto p) {
        String sql = "UPDATE productos SET nombre=?, id_categoria=?, id_subcategoria=?, id_variedad=?, id_proveedor=?, perfil_sabor=?, precio=?, stock_total=? WHERE id=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, p.getNombre());
            ps.setInt(2, p.getIdCategoria());
            ps.setInt(3, p.getIdSubcategoria());
            
            if (p.getIdVariedad() != null) {
                ps.setInt(4, p.getIdVariedad());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            
            ps.setInt(5, p.getIdProveedor());
            ps.setString(6, p.getPerfilSabor());
            ps.setDouble(7, p.getPrecio());
            ps.setInt(8, p.getStockTotal());
            ps.setInt(9, p.getId());
            
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error en ProductoDAO.actualizar: " + e.getMessage());
        }
    }

    // 4. ELIMINAR PRODUCTO
    public void eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error en ProductoDAO.eliminar: " + e.getMessage());
        }
    }
}