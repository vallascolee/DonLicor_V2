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
        Usuario usr = dao.autenticar(correo, password);

        if (usr != null) {
            // 1. Llenamos la variable interna del Bean para que el Dashboard pueda mostrar el nombre
            this.usuarioLogueado = usr;
            
            // 2. Guardamos la sesión para que el Filtro de Seguridad te deje pasar
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", usr);
            
            return "dashboard.xhtml?faces-redirect=true";
        } else {
            // Error: Credenciales incorrectas o cuenta bloqueada
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Acceso denegado", "Correo/contraseña incorrectos o cuenta bloqueada."));
            return null; // Retorna null para quedarse en la misma página de login
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
