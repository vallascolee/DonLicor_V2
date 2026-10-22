package dao;

import configuracion.Conexion;
import modelo.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import modelo.Categoria;
import modelo.Subcategoria;
import modelo.Proveedor;

public class ProductoDAO {

    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.id, p.nombre, p.id_categoria, p.id_subcategoria, p.id_variedad, p.id_proveedor, "
                   + "p.perfil_sabor, p.precio_compra, p.precio, p.stock_total, "
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
                lista.add(mapearProducto(rs));
            }
        } catch (Exception e) {
            System.out.println("Error en ProductoDAO.listarTodos: " + e.getMessage());
        }
        return lista;
    }

    // NUEVO MÉTODO: Filtrar productos por nombre/proveedor y/o categoría
    public List<Producto> filtrarProductos(String busqueda, Integer idCategoria) {
        List<Producto> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT p.id, p.nombre, p.id_categoria, p.id_subcategoria, p.id_variedad, p.id_proveedor, "
          + "p.perfil_sabor, p.precio_compra, p.precio, p.stock_total, "
          + "c.nombre AS nombre_categoria, "
          + "s.nombre AS nombre_subcategoria, "
          + "pr.nombre AS nombre_proveedor "
          + "FROM productos p "
          + "INNER JOIN categorias c ON p.id_categoria = c.id "
          + "INNER JOIN subcategorias s ON p.id_subcategoria = s.id "
          + "INNER JOIN proveedores pr ON p.id_proveedor = pr.id "
          + "WHERE 1=1 "
        );

        // Armamos la consulta dinámicamente si el usuario escribió algo
        if (busqueda != null && !busqueda.trim().isEmpty()) {
            sql.append(" AND (p.nombre LIKE ? OR pr.nombre LIKE ?) ");
        }
        // Agregamos la categoría si el usuario seleccionó una
        if (idCategoria != null && idCategoria > 0) {
            sql.append(" AND p.id_categoria = ? ");
        }

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (busqueda != null && !busqueda.trim().isEmpty()) {
                String parametroBusqueda = "%" + busqueda.trim() + "%";
                ps.setString(paramIndex++, parametroBusqueda);
                ps.setString(paramIndex++, parametroBusqueda);
            }
            if (idCategoria != null && idCategoria > 0) {
                ps.setInt(paramIndex++, idCategoria);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearProducto(rs));
                }
            }
        } catch (Exception e) {
            System.out.println("Error en ProductoDAO.filtrarProductos: " + e.getMessage());
        }
        return lista;
    }

    // Método auxiliar para no repetir la extracción de datos del ResultSet
    private Producto mapearProducto(ResultSet rs) throws Exception {
        Producto p = new Producto();
        p.setId(rs.getInt("id"));
        p.setNombre(rs.getString("nombre"));
        p.setIdCategoria(rs.getInt("id_categoria"));
        p.setIdSubcategoria(rs.getInt("id_subcategoria"));
        
        int idVar = rs.getInt("id_variedad");
        if (!rs.wasNull()) {
            p.setIdVariedad(idVar);
        }
        
        p.setIdProveedor(rs.getInt("id_proveedor"));
        p.setPerfilSabor(rs.getString("perfil_sabor"));
        p.setPrecioCompra(rs.getDouble("precio_compra"));
        p.setPrecio(rs.getDouble("precio"));
        p.setStockTotal(rs.getInt("stock_total"));
        
        p.setNombreCategoria(rs.getString("nombre_categoria"));
        p.setNombreSubcategoria(rs.getString("nombre_subcategoria"));
        p.setNombreProveedor(rs.getString("nombre_proveedor"));
        return p;
    }

    public void insertar(Producto p) {
        String sql = "INSERT INTO productos (nombre, id_categoria, id_subcategoria, id_variedad, id_proveedor, perfil_sabor, precio_compra, precio, stock_total) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setInt(2, p.getIdCategoria());
            ps.setInt(3, p.getIdSubcategoria());
            if (p.getIdVariedad() != null) { ps.setInt(4, p.getIdVariedad()); } else { ps.setNull(4, java.sql.Types.INTEGER); }
            ps.setInt(5, p.getIdProveedor());
            ps.setString(6, p.getPerfilSabor());
            ps.setDouble(7, p.getPrecioCompra());
            ps.setDouble(8, p.getPrecio());
            ps.setInt(9, p.getStockTotal());
            ps.executeUpdate();
        } catch (Exception e) { System.out.println("Error en insertar: " + e.getMessage()); }
    }

    public void actualizar(Producto p) {
        String sql = "UPDATE productos SET nombre=?, id_categoria=?, id_subcategoria=?, id_variedad=?, id_proveedor=?, perfil_sabor=?, precio_compra=?, precio=?, stock_total=? WHERE id=?";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setInt(2, p.getIdCategoria());
            ps.setInt(3, p.getIdSubcategoria());
            if (p.getIdVariedad() != null) { ps.setInt(4, p.getIdVariedad()); } else { ps.setNull(4, java.sql.Types.INTEGER); }
            ps.setInt(5, p.getIdProveedor());
            ps.setString(6, p.getPerfilSabor());
            ps.setDouble(7, p.getPrecioCompra());
            ps.setDouble(8, p.getPrecio());
            ps.setInt(9, p.getStockTotal());
            ps.setInt(10, p.getId());
            ps.executeUpdate();
        } catch (Exception e) { System.out.println("Error en actualizar: " + e.getMessage()); }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id=?";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) { System.out.println("Error en eliminar: " + e.getMessage()); }
    }
    
    public List<Categoria> listarCategorias() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT id, nombre FROM categorias ORDER BY id ASC";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) { lista.add(new Categoria(rs.getInt("id"), rs.getString("nombre"))); }
        } catch (Exception e) { System.out.println("Error en listarCategorias: " + e.getMessage()); }
        return lista;
    }

    public List<Subcategoria> listarSubcategoriasPorCategoria(int idCategoria) {
        List<Subcategoria> lista = new ArrayList<>();
        String sql = "SELECT id, id_categoria, nombre FROM subcategorias WHERE id_categoria = ? ORDER BY id ASC";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCategoria);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) { lista.add(new Subcategoria(rs.getInt("id"), rs.getInt("id_categoria"), rs.getString("nombre"))); }
            }
        } catch (Exception e) { System.out.println("Error en listarSubcategorias: " + e.getMessage()); }
        return lista;
    }

    public List<Proveedor> listarProveedores() {
        List<Proveedor> lista = new ArrayList<>();
        String sql = "SELECT id, nombre FROM proveedores ORDER BY nombre ASC";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) { lista.add(new Proveedor(rs.getInt("id"), rs.getString("nombre"))); }
        } catch (Exception e) { System.out.println("Error en listarProveedores: " + e.getMessage()); }
        return lista;
    }
}