package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.dao.UsuarioDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "VerificarTokenServlet", value = "/VerificarTokenServlet")
public class VerificarTokenServlet extends HttpServlet {
    private final UsuarioDao usuarioDao = new UsuarioDao();

    @Override
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