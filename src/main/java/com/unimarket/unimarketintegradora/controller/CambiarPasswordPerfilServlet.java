package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.ContrasenaUsuario;
import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.model.dao.ContrasenaUsuarioDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@WebServlet(name = "CambiarPasswordPerfilServlet", value = "/CambiarPasswordPerfilServlet")
public class CambiarPasswordPerfilServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // 1. Validar que haya un usuario en sesión
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // 2. Extraer datos del formulario
        String passwordActual = request.getParameter("passwordActual");
        String passwordNueva = request.getParameter("passwordNueva");
        String passwordConfirmar = request.getParameter("passwordConfirmar");

        // 3. Validaciones básicas
        if (passwordActual == null || passwordNueva == null || passwordConfirmar == null ||
                passwordActual.isEmpty() || passwordNueva.isEmpty() || passwordConfirmar.isEmpty()) {
            enviarError(request, response, "Todos los campos son obligatorios.");
            return;
        }

        if (!passwordNueva.equals(passwordConfirmar)) {
            enviarError(request, response, "Las nuevas contraseñas no coinciden.");
            return;
        }

        // 4. Validaciones de expresiones regulares (Seguridad espejo al frontend)
        if (passwordNueva.length() < 8 || !passwordNueva.matches(".*[A-Z].*") ||
                !passwordNueva.matches(".*\\d.*") || !passwordNueva.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            enviarError(request, response, "La nueva contraseña no cumple con todos los requisitos de seguridad.");
            return;
        }

        try {
            // 5. Cifrar la contraseña actual que ingresó el usuario
            String hashActualIngresado = hashSHA256(passwordActual);

            // 6. Obtener el hash real guardado en la base de datos
            ContrasenaUsuarioDao passwordDao = new ContrasenaUsuarioDao();
            ContrasenaUsuario passGuardada = passwordDao.getById(usuario.getMatricula());

            // 7. Comparar si la contraseña ingresada es la correcta
            if (passGuardada == null || !passGuardada.getContrasenaHash().equals(hashActualIngresado)) {
                enviarError(request, response, "La contraseña actual es incorrecta.");
                return;
            }

            // 8. Cifrar la nueva contraseña
            String nuevoHash = hashSHA256(passwordNueva);

            // 9. Actualizar la contraseña en la base de datos
            passGuardada.setContrasenaHash(nuevoHash);
            boolean actualizada = passwordDao.update(passGuardada);

            if (actualizada) {
                // Si todo sale bien, mandamos un mensaje de éxito
                request.setAttribute("mensaje", "¡Tu contraseña se ha actualizado de forma segura!");
                request.getRequestDispatcher("/cambiar-password-perfil.jsp").forward(request, response);
            } else {
                enviarError(request, response, "Hubo un error en la base de datos al intentar actualizar. Inténtalo más tarde.");
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            enviarError(request, response, "Error interno del servidor al procesar la seguridad.");
        }
    }

    // Método auxiliar para despachar errores fácilmente
    private void enviarError(HttpServletRequest request, HttpServletResponse response, String mensajeError) throws ServletException, IOException {
        request.setAttribute("error", mensajeError);
        request.getRequestDispatcher("/cambiar-password-perfil.jsp").forward(request, response);
    }

    // Método para cifrar contraseñas con SHA-256
    private String hashSHA256(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}