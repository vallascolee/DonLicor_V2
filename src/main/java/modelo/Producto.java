package modelo;

public class Producto {
    
    private int id;
    private String nombre;
    // Usamos Integer para todos los IDs relacionales para que JSF valide correctamente los nulos
    private Integer idCategoria;
    private Integer idSubcategoria;
    private Integer idVariedad; 
    private Integer idProveedor;
    private String perfilSabor;
    private double precioCompra; // Nuevo campo
    private double precio;
    private int stockTotal;

    private String nombreCategoria;
    private String nombreSubcategoria;
    private String nombreProveedor;

    public Producto() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Integer idCategoria) { this.idCategoria = idCategoria; }

    public Integer getIdSubcategoria() { return idSubcategoria; }
    public void setIdSubcategoria(Integer idSubcategoria) { this.idSubcategoria = idSubcategoria; }

    public Integer getIdVariedad() { return idVariedad; }
    public void setIdVariedad(Integer idVariedad) { this.idVariedad = idVariedad; }

    public Integer getIdProveedor() { return idProveedor; }
    public void setIdProveedor(Integer idProveedor) { this.idProveedor = idProveedor; }

    public String getPerfilSabor() { return perfilSabor; }
    public void setPerfilSabor(String perfilSabor) { this.perfilSabor = perfilSabor; }

    public double getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(double precioCompra) { this.precioCompra = precioCompra; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStockTotal() { return stockTotal; }
    public void setStockTotal(int stockTotal) { this.stockTotal = stockTotal; }

    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }

    public String getNombreSubcategoria() { return nombreSubcategoria; }
    public void setNombreSubcategoria(String nombreSubcategoria) { this.nombreSubcategoria = nombreSubcategoria; }

    public String getNombreProveedor() { return nombreProveedor; }
    public void setNombreProveedor(String nombreProveedor) { this.nombreProveedor = nombreProveedor; }
}