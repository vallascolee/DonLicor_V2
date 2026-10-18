package controlador;

import dao.UsuarioDAO;
import modelo.Usuario;
import jakarta.enterprise.context.SessionScoped; // <- Cambio a Session
import jakarta.inject.Named;
import jakarta.annotation.PostConstruct;
import java.io.Serializable; // <- Necesario para las sesiones
import java.util.List;

@Named
@SessionScoped // Mantiene vivos los datos al cambiar de pantalla
public class UsuarioBean implements Serializable {

    private List<Usuario> listaUsuarios;
    private Usuario nuevoUsuario;
    private Usuario usuarioSeleccionado; // <- Variable para guardar al usuario que vamos a editar

    public UsuarioBean() {
        nuevoUsuario = new Usuario();
        usuarioSeleccionado = new Usuario();
    }

    @PostConstruct
    public void init() {
        cargarUsuarios();
    }

    // Método extraído para reutilizar la recarga de la lista
    public void cargarUsuarios() {
        UsuarioDAO dao = new UsuarioDAO();
        listaUsuarios = dao.listarTodos();
    }

    public String guardarUsuario() {
        UsuarioDAO dao = new UsuarioDAO();
        dao.registrar(nuevoUsuario);
        nuevoUsuario = new Usuario(); // Limpiamos el formulario
        cargarUsuarios(); // Recargamos la tabla
        return "usuarios.xhtml?faces-redirect=true";
    }

    public String eliminarUsuario(int id) {
        UsuarioDAO dao = new UsuarioDAO();
        dao.eliminar(id);
        cargarUsuarios();
        return "usuarios.xhtml?faces-redirect=true";
    }

    // --- NUEVOS MÉTODOS PARA EDITAR ---
    
    // 1. Captura al usuario de la tabla y nos manda a la pantalla de edición
    public String prepararEdicion(Usuario user) {
        this.usuarioSeleccionado = user; 
        return "editar_usuario.xhtml?faces-redirect=true";
    }

    // 2. Guarda los cambios en la BD y nos devuelve a la tabla
    public String actualizarUsuario() {
        UsuarioDAO dao = new UsuarioDAO();
        dao.actualizar(usuarioSeleccionado);
        cargarUsuarios();
        return "usuarios.xhtml?faces-redirect=true";
    }

    // --- GETTERS Y SETTERS ---
    public List<Usuario> getListaUsuarios() { return listaUsuarios; }
    public Usuario getNuevoUsuario() { return nuevoUsuario; }
    public void setNuevoUsuario(Usuario nuevoUsuario) { this.nuevoUsuario = nuevoUsuario; }
    public Usuario getUsuarioSeleccionado() { return usuarioSeleccionado; }
    public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) { this.usuarioSeleccionado = usuarioSeleccionado; }
}