package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.dao.UsuarioDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@WebServlet(name = "ActualizarPasswordServlet", value = "/ActualizarPasswordServlet")
public class ActualizarPasswordServlet extends HttpServlet {
    private final UsuarioDao usuarioDao = new UsuarioDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String correo = request.getParameter("correo");
        String token = request.getParameter("token");
        String contra1 = request.getParameter("contra1");
        String contra2 = request.getParameter("contra2");

        // 1. Validar que no falten datos
        if (correo == null || token == null || contra1 == null || contra2 == null || contra1.isEmpty()) {
            request.setAttribute("error", "Faltan datos para completar la operación.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // 2. Validar que las contraseñas coincidan
        if (!contra1.equals(contra2)) {
            request.setAttribute("error", "Las contraseñas no coinciden.");
            request.getRequestDispatcher("nueva-password.jsp?correo=" + correo + "&token=" + token).forward(request, response);
            return;
        }

        // 3. Volver a validar el token por seguridad extrema (evita que alguien manipule la URL)
        if (usuarioDao.validarToken(correo, token)) {
            // 4. Hashear la nueva contraseña
            String passwordHasheada = convertirSHA256(contra1);

            // 5. Actualizar en Base de Datos y limpiar el token
            if (usuarioDao.limpiarTokenYActualizarPassword(correo, passwordHasheada)) {
                request.setAttribute("mensaje", "¡Tu contraseña ha sido actualizada con éxito! Ya puedes iniciar sesión.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Ocurrió un error al intentar actualizar la contraseña en la base de datos.");
                request.getRequestDispatcher("nueva-password.jsp?correo=" + correo + "&token=" + token).forward(request, response);
            }
        } else {
            // Si el token caducó mientras el usuario escribía su nueva contraseña
            request.setAttribute("error", "El código de seguridad ha caducado o es inválido. Solicita uno nuevo.");
            request.getRequestDispatcher("recuperar-password.jsp").forward(request, response);
        }
    }

    private String convertirSHA256(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al hashear la contraseña", e);
        }
    }
}