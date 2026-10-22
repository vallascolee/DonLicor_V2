package modelo;

public class Subcategoria {
    private int id;
    private int idCategoria;
    private String nombre;

    public Subcategoria() {}

    public Subcategoria(int id, int idCategoria, String nombre) {
        this.id = id;
        this.idCategoria = idCategoria;
        this.nombre = nombre;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}