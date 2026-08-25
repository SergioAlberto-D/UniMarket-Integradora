package com.unimarket.unimarketintegradora.controller.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {
        // 1. Bloquear acceso a las rutas de los Servlets
        "/inicio",
        "/mi-perfil",
        "/mis-articulos",
        "/publicar-articulo",
        "/perfil-vendedor",
        "/detalles-articulo",
        "/historial",
        "/editar-articulo",
        "/comprar-articulo",
        "/comentar-vendedor",
        "/ofertar-articulo",
        "/responder-oferta",
        "/actualizar-transaccion",
        "/actualizar-foto-perfil",
        "/eliminar-articulo",
        "/eliminar-imagen-articulo",

        "/index.jsp",
        "/mi-perfil.jsp",
        "/mis-articulos.jsp",
        "/publicar-articulo.jsp",
        "/perfil-vendedor.jsp",
        "/detalles-articulo.jsp",
        "/historial-actividad.jsp",
        "/editar-articulo.jsp",
        "/cambiar-password-perfil.jsp",
        "/articulos-fragment.jsp"
})
/**
 * Controlador web de MUA. Gestiona la interacción HTTP correspondiente a Usuario Filter.
 *
 * @author Equipo UniMarket
 */
public class UsuarioFilter implements Filter {

    @Override
/**
 * Intercepta y procesa una solicitud antes de continuar con la cadena del filtro.
 * @param request Parámetro de entrada de la operación.
 * @param response Parámetro de entrada de la operación.
 * @param chain Parámetro de entrada de la operación.
 * @throws IOException Excepción declarada por la operación.
 * @throws ServletException Excepción declarada por la operación.
 */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        // Si existe una sesión y tiene la etiqueta "admin", es el administrador intentando husmear
        if (session != null && session.getAttribute("admin") != null) {
            // Lo regresamos a su panel de administración
            res.sendRedirect(req.getContextPath() + "/adminactividad");
        } else {
            // Si es un usuario normal (o no ha iniciado sesión), se le permite cargar la página
            chain.doFilter(request, response);
        }
    }
}