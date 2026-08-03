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
public class ActualizarTelefonoServlet extends HttpServlet {
    private final NotificacionDao notificacionDao = new NotificacionDao();

    @Override
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
            boolean actualizado = usuarioDao.actualizarTelefono(usuario.getIdUsuario(), nuevoTelefono);

            if (actualizado) {
                usuario.setNumeroCelular(nuevoTelefono);
                session.setAttribute("usuario", usuario);

                // Notificar en la campana
                String mensaje = "Tu número de teléfono celular ha sido actualizado a " + nuevoTelefono + ".";
                notificacionDao.crearNotificacion(usuario.getIdUsuario(), mensaje, "SISTEMA");
            }
        }

        response.sendRedirect(request.getContextPath() + "/mi-perfil");
    }
}