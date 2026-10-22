package dao;

import configuracion.Conexion;
import modelo.Venta;
import modelo.DetalleVenta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class VentaDAO {

    public boolean registrarVenta(Venta venta) {
        boolean exito = false;
        String sqlVenta = "INSERT INTO ventas (total, id_usuario, id_cliente, id_metodo_pago, tipo_comprobante, estado) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_venta (id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        String sqlActualizarStock = "UPDATE productos SET stock_total = stock_total - ? WHERE id = ?";

        Connection con = null;
        try {
            con = Conexion.getConexion();
            // Desactivamos el autocommit para manejar la transacción manualmente
            con.setAutoCommit(false); 

            // 1. Insertar la Venta (Cabecera)
            try (PreparedStatement psVenta = con.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                psVenta.setDouble(1, venta.getTotal());
                psVenta.setInt(2, venta.getIdUsuario());
                psVenta.setInt(3, venta.getIdCliente());
                psVenta.setInt(4, venta.getIdMetodoPago());
                psVenta.setString(5, venta.getTipoComprobante());
                psVenta.setString(6, venta.getEstado());
                psVenta.executeUpdate();

                // Obtener el ID de la venta recién generada
                try (ResultSet rs = psVenta.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idVentaGenerado = rs.getInt(1);

                        // 2. Insertar los detalles y actualizar el stock
                        try (PreparedStatement psDetalle = con.prepareStatement(sqlDetalle);
                             PreparedStatement psStock = con.prepareStatement(sqlActualizarStock)) {
                            
                            for (DetalleVenta detalle : venta.getDetalles()) {
                                // Insertar en detalle_venta
                                psDetalle.setInt(1, idVentaGenerado);
                                psDetalle.setInt(2, detalle.getIdProducto());
                                psDetalle.setInt(3, detalle.getCantidad());
                                psDetalle.setDouble(4, detalle.getPrecioUnitario());
                                psDetalle.setDouble(5, detalle.getSubtotal());
                                psDetalle.addBatch(); // Lo agregamos a un lote para mayor velocidad

                                // Restar stock en productos
                                psStock.setInt(1, detalle.getCantidad());
                                psStock.setInt(2, detalle.getIdProducto());
                                psStock.addBatch();
                            }
                            // Ejecutar los lotes
                            psDetalle.executeBatch();
                            psStock.executeBatch();
                        }
                    }
                }
            }
            // Si todo salió bien, confirmamos los cambios en la Base de Datos
            con.commit();
            exito = true;
            System.out.println("Venta procesada con éxito y stock descontado.");

        } catch (Exception e) {
            System.out.println("Error Crítico en registrarVenta. Revirtiendo cambios: " + e.getMessage());
            try {
                if (con != null) {
                    con.rollback(); // Si algo falla, cancelamos toda la operación
                }
            } catch (Exception ex) {
                System.out.println("Error en el rollback: " + ex.getMessage());
            }
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (Exception e) {
                System.out.println("Error cerrando conexión: " + e.getMessage());
            }
        }
        return exito;
    }
}