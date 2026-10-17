package modelo;

import java.sql.Timestamp;

public class Usuario {

    private int id;
    private String nombre;
    private String correo;
    private String password;
    private int idRol;
    private Timestamp ultimoAcceso;
    private int intentosFallidos;
    private String estadoCuenta;

    public Usuario() {
    }

    // Getters y Setters completos
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

    public Timestamp getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(Timestamp ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    public int getIntentosFallidos() {
        return intentosFallidos;
    }

    public void setIntentosFallidos(int intentosFallidos) {
        this.intentosFallidos = intentosFallidos;
    }

    public String getEstadoCuenta() {
        return estadoCuenta;
    }

    public void setEstadoCuenta(String estadoCuenta) {
        this.estadoCuenta = estadoCuenta;
    }
}
