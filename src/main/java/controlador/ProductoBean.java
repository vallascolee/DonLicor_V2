package controlador;

import dao.ProductoDAO;
import modelo.Producto;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class ProductoBean implements Serializable {

    private ProductoDAO dao;
    private Producto productoSeleccionado;
    private List<Producto> listaProductos;
    private boolean modoEdicion;

    public ProductoBean() {
        dao = new ProductoDAO();
        productoSeleccionado = new Producto();
        modoEdicion = false;
    }

    // 1. OBTENER LISTA DE PRODUCTOS (Para la tabla principal)
    public List<Producto> getListaProductos() {
        listaProductos = dao.listarTodos();
        return listaProductos;
    }

    // 2. PREPARAR CREACIÓN (Limpia el formulario y redirige)
    public String prepararNuevo() {
        productoSeleccionado = new Producto();
        modoEdicion = false;
        return "nuevo_producto.xhtml?faces-redirect=true";
    }

    // 3. PREPARAR EDICIÓN (Carga los datos del producto seleccionado y redirige)
    public String prepararEdicion(Producto p) {
        this.productoSeleccionado = p;
        this.modoEdicion = true;
        return "editar_producto.xhtml?faces-redirect=true";
    }

    // 4. GUARDAR PRODUCTO (Sirve tanto para Insertar como para Actualizar)
    public String guardarProducto() {
        if (modoEdicion) {
            dao.actualizar(productoSeleccionado);
        } else {
            dao.insertar(productoSeleccionado);
        }
        return "productos.xhtml?faces-redirect=true";
    }

    // 5. ELIMINAR PRODUCTO (Ejecuta el borrado y se queda en la misma página)
    public String eliminarProducto(int id) {
        dao.eliminar(id);
        return "productos.xhtml?faces-redirect=true";
    }

    // --- GETTERS Y SETTERS ---
    public Producto getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public void setProductoSeleccionado(Producto productoSeleccionado) {
        this.productoSeleccionado = productoSeleccionado;
    }

    public boolean isModoEdicion() {
        return modoEdicion;
    }

    public void setModoEdicion(boolean modoEdicion) {
        this.modoEdicion = modoEdicion;
    }
}