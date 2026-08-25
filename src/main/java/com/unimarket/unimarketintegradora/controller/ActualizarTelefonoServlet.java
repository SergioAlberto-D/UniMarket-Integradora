package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.model.dao.NotificacionDao;
import com.unimarket.unimarketintegradora.model.dao.UsuarioDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "ActualizarTelefonoServlet", value = "/ActualizarTelefonoServlet")
/**
 * Controlador web de MUA. Gestiona la interacción HTTP correspondiente a Actualizar Telefono Servlet.
 *
 * @author Equipo UniMarket
 */
public class ActualizarTelefonoServlet extends HttpServlet {
    private final NotificacionDao notificacionDao = new NotificacionDao();

    @Override
/**
 * Procesa una solicitud HTTP POST y ejecuta la operación solicitada.
 * @param request Parámetro de entrada de la operación.
 * @param response Parámetro de entrada de la operación.
 * @throws ServletException Excepción declarada por la operación.
 * @throws IOException Excepción declarada por la operación.
 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String nuevoTelefono = request.getParameter("telefono");

        if (nuevoTelefono != null && !nuevoTelefono.trim().isEmpty()) {
            UsuarioDao usuarioDao = new UsuarioDao();
            boolean actualizado = usuarioDao.actualizarTelefono(usuario.getMatricula(), nuevoTelefono);

            if (actualizado) {
                usuario.setNumeroCelular(nuevoTelefono);
                session.setAttribute("usuario", usuario);

                // Notificar en la campana
                String mensaje = "Tu número de teléfono celular ha sido actualizado a " + nuevoTelefono + ".";
                notificacionDao.crearNotificacion(usuario.getMatricula(), mensaje, "SISTEMA");
            }
        }

        response.sendRedirect(request.getContextPath() + "/mi-perfil");
    }
}