package modelo;

public class Usuario {
    
    private int id;
    private String nombre;
    private String correo;
    private String password;
    private int idRol;
    private String estadoCuenta;

    // 1. Constructor vacío (Obligatorio para que JSF y Java trabajen bien con esta clase)
    public Usuario() {
    }

    // 2. Constructor con parámetros (Opcional, pero útil para crear usuarios rápidos en el código)
    public Usuario(int id, String nombre, String correo, String password, int idRol, String estadoCuenta) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.idRol = idRol;
        this.estadoCuenta = estadoCuenta;
    }

    // 3. Getters y Setters (Para leer y escribir los datos de forma segura)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getEstadoCuenta() {
        return estadoCuenta;
    }

    public void setEstadoCuenta(String estadoCuenta) {
        this.estadoCuenta = estadoCuenta;
    }
}