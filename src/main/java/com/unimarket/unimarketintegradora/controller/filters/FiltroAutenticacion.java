package com.unimarket.unimarketintegradora.controller.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// El filtro se aplicará a todas las URLs de tu app
@WebFilter("/*")
public class FiltroAutenticacion extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession(false); // false = no crea una nueva sesión si no existe

        // 1. Verificar si el usuario ya está logueado
        // En tus vistas privadas validas la sesión buscando el atributo "usuario"[cite: 11, 12]
        boolean loggedIn = (session != null && session.getAttribute("usuario") != null);

        // 2. Definir las rutas JSPs y Servlets que son PÚBLICAS
        boolean loginRequest =
                requestURI.endsWith("login.jsp") ||
                requestURI.endsWith("/login") ||
                requestURI.endsWith("registro.jsp") ||
                requestURI.endsWith("/register") ||
                requestURI.endsWith("recuperar-password.jsp") ||
                requestURI.endsWith("/SolicitarRecuperacionServlet") ||
                requestURI.endsWith("verificar-token.jsp") ||
                requestURI.endsWith("/VerificarTokenServlet") ||
                requestURI.endsWith("nueva-password.jsp") ||
                requestURI.endsWith("/ActualizarPasswordServlet");

        // 3. Permitir el paso a los recursos estáticos (CSS, JS, imágenes)[cite: 9, 14]
        boolean isResource = requestURI.contains("/assets/") || requestURI.contains("/static/");

        if (loggedIn) {
            // SI TIENE SESIÓN:
            if (loginRequest) {
                // Si ya está logueado e intenta ir al login o registro, lo mandamos al catálogo (index)
                response.sendRedirect(request.getContextPath() + "/index.jsp");
            } else {
                // Si va a cualquier otra página (perfil, publicar, etc.), lo dejamos pasar
                chain.doFilter(request, response);
            }
        } else {
            // NO TIENE SESIÓN:
            if (loginRequest || isResource) {
                // Si no tiene sesión pero va a recuperar contraseña, login o está cargando CSS, lo dejamos pasar
                chain.doFilter(request, response);
            } else {
                // Si intenta entrar a una página protegida (como perfil-usuario.jsp) sin sesión, va para el login
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
        }
    }
}