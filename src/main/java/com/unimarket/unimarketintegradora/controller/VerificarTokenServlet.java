package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.dao.UsuarioDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet encargado de procesar y validar el token de recuperación de contraseña ingresado por el usuario,
 * verificando su autenticidad y vigencia a través del DAO, redirigiendo a la vista para establecer una nueva contraseña
 * o retornando un mensaje de error en caso de que el código sea inválido o haya caducado.
 *
 * @author Luis Fernando Rodriguez Rayo
 * @date 2026-06-06
 */
@WebServlet(name = "VerificarTokenServlet", value = "/VerificarTokenServlet")
/**
 * Controlador web de MUA. Gestiona la interacción HTTP correspondiente a Verificar Token Servlet.
 *
 * @author Equipo UniMarket
 */
public class VerificarTokenServlet extends HttpServlet {
    private final UsuarioDao usuarioDao = new UsuarioDao();

    /**
     * Maneja las peticiones POST para recibir el correo y el token de recuperación, validar su validez mediante el DAO,
     * redirigir a la página de cambio de contraseña o reenviar al formulario de verificación con el mensaje de error correspondiente.
     *
     * @param request  Objeto HttpServletRequest con los parámetros de correo y token de recuperación.
     * @param response Objeto HttpServletResponse para redirigir al cambio de contraseña o reenviar al JSP de verificación.
     * @throws ServletException Si ocurre un error específico del servlet.
     * @throws IOException      Si ocurre un error de E/S.
     * @author Luis Fernando Rodriguez Rayo
     * @date 2026-06-06
     */
    @Override
/**
 * Procesa una solicitud HTTP POST y ejecuta la operación solicitada.
 * @param request Parámetro de entrada de la operación.
 * @param response Parámetro de entrada de la operación.
 * @throws ServletException Excepción declarada por la operación.
 * @throws IOException Excepción declarada por la operación.
 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String correo = request.getParameter("correo");
        String token = request.getParameter("token");

        if (correo != null && token != null && usuarioDao.validarToken(correo, token)) {
            // El token es válido, lo mandamos a crear su nueva contraseña
            // Pasamos el correo y el token verificado por URL para la última validación
            response.sendRedirect("nueva-password.jsp?correo=" + correo + "&token=" + token);
        } else {
            request.setAttribute("error", "Código inválido o caducado.");
            // Regresamos a la misma página manteniendo el correo en el input
            request.getRequestDispatcher("verificar-token.jsp?correo=" + correo).forward(request, response);
        }
    }
}