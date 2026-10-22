package controlador;

import dao.ProductoDAO;
import modelo.Producto;
import modelo.Categoria;
import modelo.Subcategoria;
import modelo.Proveedor;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
public class ProductoBean implements Serializable {

    private ProductoDAO dao;
    private Producto productoSeleccionado;
    private List<Producto> listaProductos;
    private boolean modoEdicion;

    // Variables para combos
    private List<Categoria> listaCategorias;
    private List<Subcategoria> listaSubcategorias;
    private List<Proveedor> listaProveedores;

    // Nuevas variables para el Filtro
    private String terminoBusqueda;
    private Integer categoriaFiltro;

    public ProductoBean() {
        dao = new ProductoDAO();
        productoSeleccionado = new Producto();
        modoEdicion = false;
        listaSubcategorias = new ArrayList<>();
        terminoBusqueda = "";
        categoriaFiltro = 0;
    }

    // Corregido: Solo consulta la BD si la lista está nula. Esto evita que los filtros se borren solos.
    public List<Producto> getListaProductos() {
        if (listaProductos == null) {
            listaProductos = dao.listarTodos();
        }
        return listaProductos;
    }

    // NUEVO: Método para ejecutar la búsqueda
    public void buscar() {
        listaProductos = dao.filtrarProductos(terminoBusqueda, categoriaFiltro);
    }

    // NUEVO: Método para limpiar filtros y recargar todo
    public void limpiarFiltros() {
        terminoBusqueda = "";
        categoriaFiltro = 0;
        listaProductos = dao.listarTodos();
    }

    public String prepararNuevo() {
        productoSeleccionado = new Producto();
        modoEdicion = false;
        listaCategorias = dao.listarCategorias();
        listaProveedores = dao.listarProveedores();
        listaSubcategorias = new ArrayList<>(); 
        return "nuevo_producto.xhtml?faces-redirect=true";
    }

    public void cargarSubcategorias(AjaxBehaviorEvent event) {
        if (productoSeleccionado != null && productoSeleccionado.getIdCategoria() != null && productoSeleccionado.getIdCategoria() > 0) {
            listaSubcategorias = dao.listarSubcategoriasPorCategoria(productoSeleccionado.getIdCategoria());
        } else {
            listaSubcategorias = new ArrayList<>();
        }
    }

    public String prepararEdicion(Producto p) {
        this.productoSeleccionado = p;
        this.modoEdicion = true;
        listaCategorias = dao.listarCategorias();
        listaProveedores = dao.listarProveedores();
        if (p.getIdCategoria() != null && p.getIdCategoria() > 0) {
            listaSubcategorias = dao.listarSubcategoriasPorCategoria(p.getIdCategoria());
        } else {
            listaSubcategorias = new ArrayList<>();
        }
        return "editar_producto.xhtml?faces-redirect=true";
    }

    public String guardarProducto() {
        if (modoEdicion) {
            dao.actualizar(productoSeleccionado);
        } else {
            dao.insertar(productoSeleccionado);
        }
        listaProductos = dao.listarTodos(); // Actualizar lista tras guardar
        return "productos.xhtml?faces-redirect=true";
    }

    public String eliminarProducto(int id) {
        dao.eliminar(id);
        listaProductos = dao.listarTodos(); // Actualizar lista tras eliminar
        return "productos.xhtml?faces-redirect=true";
    }

    // Corregido: Autocargar categorías si están nulas para que el filtro funcione apenas abres la vista
    public List<Categoria> getListaCategorias() {
        if (listaCategorias == null || listaCategorias.isEmpty()) {
            listaCategorias = dao.listarCategorias();
        }
        return listaCategorias;
    }

    public Producto getProductoSeleccionado() { return productoSeleccionado; }
    public void setProductoSeleccionado(Producto productoSeleccionado) { this.productoSeleccionado = productoSeleccionado; }
    public boolean isModoEdicion() { return modoEdicion; }
    public void setModoEdicion(boolean modoEdicion) { this.modoEdicion = modoEdicion; }
    public List<Subcategoria> getListaSubcategorias() { return listaSubcategorias; }
    public List<Proveedor> getListaProveedores() { return listaProveedores; }

    // Getters y Setters de los filtros
    public String getTerminoBusqueda() { return terminoBusqueda; }
    public void setTerminoBusqueda(String terminoBusqueda) { this.terminoBusqueda = terminoBusqueda; }
    public Integer getCategoriaFiltro() { return categoriaFiltro; }
    public void setCategoriaFiltro(Integer categoriaFiltro) { this.categoriaFiltro = categoriaFiltro; }
}