package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.ContrasenaUsuario;
import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.model.dao.ContrasenaUsuarioDao;
import com.unimarket.unimarketintegradora.model.dao.UsuarioDao;
import com.unimarket.unimarketintegradora.utils.EmailSender;
import com.unimarket.unimarketintegradora.utils.HashUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Date;
import java.text.MessageFormat;

@WebServlet(name = "RegisterServlet", value = "/register")
public class RegisterServlet extends HttpServlet {
    private final UsuarioDao usuarioDao = new UsuarioDao();
    private final ContrasenaUsuarioDao contrasenaDao = new ContrasenaUsuarioDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String matricula = limpiar(request.getParameter("matricula")).toLowerCase();
        String nombre = limpiar(request.getParameter("nombre"));
        String apellidoPaterno = limpiar(request.getParameter("apellidoPaterno"));
        String apellidoMaterno = limpiar(request.getParameter("apellidoMaterno"));
        String telefono = limpiar(request.getParameter("telefono")).replaceAll("[^0-9]", "");
        int idDivision = 1;
        try { idDivision = Integer.parseInt(request.getParameter("idDivision")); } catch (Exception ignored) {}
        String correo = matricula + "@utez.edu.mx";
        String contra1 = request.getParameter("contra1") == null ? "" : request.getParameter("contra1");
        String contra2 = request.getParameter("contra2") == null ? "" : request.getParameter("contra2");

        if (matricula.isEmpty() || nombre.isEmpty() || apellidoPaterno.isEmpty() || apellidoMaterno.isEmpty() || contra1.isEmpty()) {
            enviarError(request, response, "Completa todos los campos obligatorios.");
            return;
        }

        if (!correo.matches("^[A-Za-z0-9._%+-]+@utez\\.edu\\.mx$")) {
            enviarError(request, response, "Usa tu correo institucional de UTEZ.");
            return;
        }

        if (!contra1.equals(contra2)) {
            enviarError(request, response, "Las contraseñas no coinciden.");
            return;
        }

        try {
            if (usuarioDao.existeCorreo(correo)) {
                enviarError(request, response, "Ese correo ya está registrado.");
                return;
            }

            Usuario usuario = new Usuario(nombre, apellidoPaterno, apellidoMaterno, telefono, idDivision, new Date(System.currentTimeMillis()), correo, 2, "unverificado");
            usuario.setIdUsuario(matricula);
            boolean registrado = usuarioDao.create(usuario);

            if (registrado) {
                Usuario usuarioCreado = usuarioDao.buscarPorCorreo(correo);
                String hash = HashUtils.convertirSHA256(contra1);
                ContrasenaUsuario passModel = new ContrasenaUsuario(usuarioCreado.getIdUsuario(), hash);
                contrasenaDao.create(passModel);

                String urlConfirmacion = request.getScheme() + "://" +
                        request.getServerName() + ":" +
                        request.getServerPort() +
                        request.getContextPath() +
                        "/verificar-cuenta?correo=" + correo;

                String plantillaHtml = """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                </head>
                <body style="margin: 0; padding: 0; background-color: #F7EDE7; font-family: Arial, Helvetica, sans-serif;">
                    <div style="background-color: #3E2723; padding: 20px; text-align: center;">
                        <img src="https://PON_AQUI_EL_ENLACE_PUBLICO_DE_TU_LOGO.png" alt="MUA Logo" style="height: 70px; border-radius: 50%;">
                    </div>
                    <div style="padding: 40px 20px;">
                        <div style="background-color: #ffffff; max-width: 600px; margin: 0 auto; padding: 40px 30px; border-radius: 12px; box-shadow: 0 4px 10px rgba(0,0,0,0.05); text-align: center;">
                            <h1 style="color: #111111; font-size: 26px; margin-top: 0;">¡Hola! {0}</h1>
                            <p style="color: #333333; font-size: 16px; line-height: 1.5; text-align: left; margin-bottom: 40px;">
                                Tu cuenta está casi lista. Por favor, confirma tu correo electrónico para finalizar el registro en el Marketplace Universitario de Artículos.
                            </p>
                            <a href="{1}" style="display: inline-block; background-color: #753618; color: #ffffff; text-decoration: none; padding: 16px 40px; font-size: 20px; font-weight: bold; border-radius: 8px; box-shadow: 3px 5px 10px rgba(0, 0, 0, 0.2);">Confirmar mi correo</a>
                        </div>
                    </div>
                    <div style="background-color: #E3D3C8; padding: 20px; text-align: center; color: #333333; font-size: 12px;">
                        © 2024 MUA - Marketplace Universitario de Artículos
                    </div>
                </body>
                </html>
                """;
                String cuerpoCorreo = MessageFormat.format(plantillaHtml, usuario.getNombre(), urlConfirmacion);

                try {
                    // Enviar el correo
                    EmailSender.sendMail(usuario.getCorreoInstitucional(), "Confirma tu cuenta en UniMarket", cuerpoCorreo);

                    // Redirigir al login avisando que revise su correo
                    request.setAttribute("mensaje", "¡Cuenta creada con éxito! Revisa tu correo institucional para verificarla antes de iniciar sesión.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);

                } catch (Exception e) {
                    usuarioDao.eliminarPorCorreo(usuario.getCorreoInstitucional());
                    enviarError(request, response, "No pudimos enviar el correo de confirmación. Intenta de nuevo.");
                }
            } else {
                enviarError(request, response, "Ocurrió un problema al crear tu cuenta. Intenta de nuevo.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            enviarError(request, response, "Error interno del servidor. Intenta más tarde.");
        }
    }

    // Ahora estos métodos sí son reconocidos por Java
    private String limpiar(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private void enviarError(HttpServletRequest request, HttpServletResponse response, String mensaje) throws ServletException, IOException {
        request.setAttribute("error", mensaje);
        request.getRequestDispatcher("registro.jsp").forward(request, response);
    }
}