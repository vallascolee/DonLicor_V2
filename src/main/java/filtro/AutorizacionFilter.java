package filtro;

import controlador.LoginBean;
import jakarta.inject.Inject;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

// Esta etiqueta le indica al servidor qué páginas debe proteger este filtro
@WebFilter(urlPatterns = {"/dashboard.xhtml", "/usuarios.xhtml", "/nuevo_usuario.xhtml", "/editar_usuario.xhtml"})
public class AutorizacionFilter implements Filter {

    @Inject
    private LoginBean loginBean;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Verificamos si existe un usuario en la memoria de la sesión
        if (loginBean != null && loginBean.getUsuarioLogueado() != null) {
            // El usuario es legítimo, le permitimos ver la página
            chain.doFilter(request, response);
        } else {
            // Es un intruso, lo redirigimos automáticamente a la pantalla de login
            res.sendRedirect(req.getContextPath() + "/login.xhtml");
        }
    }
}