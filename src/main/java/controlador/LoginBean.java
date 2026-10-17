package controlador;

import dao.UsuarioDAO;
import modelo.Usuario;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;

@Named
@SessionScoped
public class LoginBean implements Serializable {

    private String correo;
    private String password;
    private Usuario usuarioLogueado;

    public LoginBean() {
    }

    public String iniciarSesion() {
        UsuarioDAO dao = new UsuarioDAO();
        // Llamamos al método que creaste en el paso anterior
        usuarioLogueado = dao.autenticar(correo, password);

        if (usuarioLogueado != null) {
            // Login exitoso: Redireccionamos a una página de bienvenida (la crearemos luego)
            return "dashboard.xhtml?faces-redirect=true";
        } else {
            // Login fallido: Mostramos un mensaje de error en la pantalla
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Credenciales incorrectas o cuenta inactiva", null));
            return null; // Nos quedamos en la misma página
        }
    }

    public String cerrarSesion() {
        usuarioLogueado = null;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "login.xhtml?faces-redirect=true";
    }

    // Getters y Setters obligatorios para que la página web pueda leer y escribir estas variables
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

    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }
}
