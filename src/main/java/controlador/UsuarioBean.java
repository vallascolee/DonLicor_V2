package controlador;

import dao.UsuarioDAO;
import modelo.Usuario;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.annotation.PostConstruct;
import java.util.List;

@Named
@RequestScoped
public class UsuarioBean {

    private List<Usuario> listaUsuarios;
    private Usuario nuevoUsuario; // <- Variable para almacenar los datos del formulario

    public UsuarioBean() {
        nuevoUsuario = new Usuario(); // Inicializamos el usuario vacío
    }

    @PostConstruct
    public void init() {
        UsuarioDAO dao = new UsuarioDAO();
        listaUsuarios = dao.listarTodos();
    }

    // --- Acción para guardar el formulario ---
    public String guardarUsuario() {
        UsuarioDAO dao = new UsuarioDAO();
        dao.registrar(nuevoUsuario);
        return "usuarios.xhtml?faces-redirect=true"; // Redirige de vuelta a la tabla
    }

    // --- Getters y Setters ---
    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public Usuario getNuevoUsuario() {
        return nuevoUsuario;
    }

    public void setNuevoUsuario(Usuario nuevoUsuario) {
        this.nuevoUsuario = nuevoUsuario;
    }
}