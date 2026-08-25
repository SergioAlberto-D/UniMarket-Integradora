package com.unimarket.unimarketintegradora.controller.filters;

import com.unimarket.unimarketintegradora.model.Usuario;
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
/**
 * Controlador web de MUA. Gestiona la interacción HTTP correspondiente a Filtro Autenticacion.
 *
 * @author Equipo UniMarket
 */
public class FiltroAutenticacion extends HttpFilter {

    @Override
/**
 * Intercepta y procesa una solicitud antes de continuar con la cadena del filtro.
 * @param request Parámetro de entrada de la operación.
 * @param response Parámetro de entrada de la operación.
 * @param chain Parámetro de entrada de la operación.
 * @throws IOException Excepción declarada por la operación.
 * @throws ServletException Excepción declarada por la operación.
 */
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession(false);

        // 1. Identificar QUÉ tipo de sesión existe
        boolean isUsuario = (session != null && session.getAttribute("usuario") != null);
        boolean isAdmin = (session != null && session.getAttribute("admin") != null);
        boolean loggedIn = isUsuario || isAdmin; // Está logueado si es cualquiera de los dos

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
                        requestURI.endsWith("/ActualizarPasswordServlet") ||
                        requestURI.endsWith("/verificar-cuenta");

        // 3. Permitir el paso a los recursos estáticos (CSS, JS, imágenes)
        boolean isResource = requestURI.contains("/assets/") || requestURI.contains("/static/");

        if (loggedIn) {

            // Si es un Usuario normal, calculamos las cosas del Header
            if (isUsuario) {
                Usuario usuario = (Usuario) session.getAttribute("usuario");
                String iniciales = "";
                if (usuario.getNombre() != null && !usuario.getNombre().isEmpty()) {
                    iniciales += usuario.getNombre().substring(0, 1).toUpperCase();
                }
                if (usuario.getApellidoPaterno() != null && !usuario.getApellidoPaterno().isEmpty()) {
                    iniciales += usuario.getApellidoPaterno().substring(0, 1).toUpperCase();
                }
                if (iniciales.isEmpty()) {
                    iniciales = "U";
                }
                request.setAttribute("iniciales", iniciales);
                request.setAttribute("esVendedor", usuario.getIdRolFk() == 3);
            }

            // SI TIENE SESIÓN:
            if (loginRequest) {
                // Si ya está logueado e intenta ir al login o registro, lo mandamos a su respectivo panel
                if (isAdmin) {
                    response.sendRedirect(request.getContextPath() + "/adminactividad");
                } else {
                    response.sendRedirect(request.getContextPath() + "/inicio");
                }
            } else {
                // Si va a cualquier otra página lo dejamos pasar (los otros filtros se encargarán de bloquear si cruzan rutas)
                chain.doFilter(request, response);
            }
        } else {
            // NO TIENE SESIÓN:
            if (loginRequest || isResource) {
                // Si no tiene sesión pero va a recuperar contraseña, login o está cargando CSS, lo dejamos pasar
                chain.doFilter(request, response);
            } else {
                // Si intenta entrar a una página protegida sin sesión, va para el login
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
        }
    }
}