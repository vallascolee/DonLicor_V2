package modelo;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class Venta {
    private int id;
    private Date fecha;
    private double total;
    private int idUsuario; // Quien realiza la venta
    private int idCliente;
    private int idMetodoPago;
    private String tipoComprobante;
    private String estado;
    
    // Lista para almacenar el carrito de compras
    private List<DetalleVenta> detalles;

    public Venta() {
        this.detalles = new ArrayList<>();
        this.estado = "Completada";
        this.tipoComprobante = "Boleta"; // Por defecto
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public int getIdMetodoPago() { return idMetodoPago; }
    public void setIdMetodoPago(int idMetodoPago) { this.idMetodoPago = idMetodoPago; }
    public String getTipoComprobante() { return tipoComprobante; }
    public void setTipoComprobante(String tipoComprobante) { this.tipoComprobante = tipoComprobante; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public List<DetalleVenta> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVenta> detalles) { this.detalles = detalles; }
}