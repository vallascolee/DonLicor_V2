package modelo;

public class Producto {
    
    // Variables exactas de la tabla 'productos' de tu base de datos
    private int id;
    private String nombre;
    private int idCategoria;
    private int idSubcategoria;
    private Integer idVariedad; // Usamos Integer (con mayúscula) porque en tu BD permite valores NULL
    private int idProveedor;
    private String perfilSabor;
    private double precio;
    private int stockTotal;

    // Variables adicionales (transitorias) para mostrar texto en el HTML en vez de puros números de ID
    private String nombreCategoria;
    private String nombreSubcategoria;
    private String nombreProveedor;

    public Producto() {
    }

    // --- GETTERS Y SETTERS COMPLETO ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public int getIdSubcategoria() { return idSubcategoria; }
    public void setIdSubcategoria(int idSubcategoria) { this.idSubcategoria = idSubcategoria; }

    public Integer getIdVariedad() { return idVariedad; }
    public void setIdVariedad(Integer idVariedad) { this.idVariedad = idVariedad; }

    public int getIdProveedor() { return idProveedor; }
    public void setIdProveedor(int idProveedor) { this.idProveedor = idProveedor; }

    public String getPerfilSabor() { return perfilSabor; }
    public void setPerfilSabor(String perfilSabor) { this.perfilSabor = perfilSabor; }

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