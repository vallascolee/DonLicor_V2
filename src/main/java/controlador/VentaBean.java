package controlador;

import dao.ProductoDAO;
import dao.VentaDAO;
import modelo.DetalleVenta;
import modelo.Producto;
import modelo.Venta;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class VentaBean implements Serializable {

    private Venta ventaActual;
    private ProductoDAO productoDAO;
    private VentaDAO ventaDAO;
    
    private List<Producto> listaProductosBusqueda;
    private String textoBusqueda;

    public VentaBean() {
        productoDAO = new ProductoDAO();
        ventaDAO = new VentaDAO();
        iniciarNuevaVenta();
    }

    public void iniciarNuevaVenta() {
    ventaActual = new Venta();
    // VITAL: Revisa tu base de datos. ¿Tu usuario "Lee Vallasco" tiene ID 1?
    ventaActual.setIdUsuario(1); 
    // ¿El cliente que insertaste ("Público General") tiene ID 1? 
    // Si tu tabla de clientes estaba vacía cuando insertaste, el ID será 1. Si no, busca el ID correcto en MySQL.
    ventaActual.setIdCliente(1); 
    ventaActual.setIdMetodoPago(1); // "Efectivo"
    
    textoBusqueda = "";
    buscarProducto(); 
}

    public void buscarProducto() {
        // Reutilizamos tu excelente método del ProductoDAO
        listaProductosBusqueda = productoDAO.filtrarProductos(textoBusqueda, 0);
    }

    public void agregarAlCarrito(Producto p) {
        // 1. Validar que haya stock físico
        if (p.getStockTotal() <= 0) {
            return; // No hace nada si no hay stock (más adelante le pondremos una alerta visual)
        }

        // 2. Verificar si el producto ya está en el carrito para solo sumarle 1 a la cantidad
        for (DetalleVenta d : ventaActual.getDetalles()) {
            if (d.getIdProducto() == p.getId()) {
                if(d.getCantidad() < p.getStockTotal()){
                    d.setCantidad(d.getCantidad() + 1);
                    d.setSubtotal(d.getCantidad() * d.getPrecioUnitario());
                    calcularTotal();
                }
                return;
            }
        }

        // 3. Si es la primera vez que se agrega al carrito
        DetalleVenta nuevoDetalle = new DetalleVenta();
        nuevoDetalle.setIdProducto(p.getId());
        nuevoDetalle.setNombreProducto(p.getNombre());
        nuevoDetalle.setCantidad(1);
        nuevoDetalle.setPrecioUnitario(p.getPrecio());
        nuevoDetalle.setSubtotal(p.getPrecio());
        
        ventaActual.getDetalles().add(nuevoDetalle);
        calcularTotal();
    }

    public void removerDelCarrito(DetalleVenta detalle) {
        ventaActual.getDetalles().remove(detalle);
        calcularTotal();
    }

    private void calcularTotal() {
        double total = 0;
        for (DetalleVenta d : ventaActual.getDetalles()) {
            total += d.getSubtotal();
        }
        ventaActual.setTotal(total);
    }

    public String procesarVenta() {
        if (ventaActual.getDetalles().isEmpty()) {
            return null; // No podemos vender un carrito vacío
        }
        
        boolean exito = ventaDAO.registrarVenta(ventaActual);
        if (exito) {
            iniciarNuevaVenta(); // Limpiamos la pantalla para atender al siguiente cliente
            buscarProducto(); // Refrescamos la lista para ver el stock actualizado
            return "caja.xhtml?faces-redirect=true";
        }
        return null;
    }

    // Getters y Setters
    public Venta getVentaActual() { return ventaActual; }
    public void setVentaActual(Venta ventaActual) { this.ventaActual = ventaActual; }
    public List<Producto> getListaProductosBusqueda() { return listaProductosBusqueda; }
    public void setListaProductosBusqueda(List<Producto> listaProductosBusqueda) { this.listaProductosBusqueda = listaProductosBusqueda; }
    public String getTextoBusqueda() { return textoBusqueda; }
    public void setTextoBusqueda(String textoBusqueda) { this.textoBusqueda = textoBusqueda; }
}