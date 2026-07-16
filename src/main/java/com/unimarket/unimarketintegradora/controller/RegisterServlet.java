package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.model.dao.UsuarioDao;
// IMPORTANTE: Asegúrate de importar la clase EmailSender desde el paquete donde la tengas guardada, por ejemplo:
// import com.unimarket.unimarketintegradora.util.EmailSender; 

import com.unimarket.unimarketintegradora.utils.EmailSender;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;

@WebServlet(name = "RegisterServlet", value = "/register")
public class RegisterServlet extends HttpServlet {
    private final UsuarioDao usuarioDao = new UsuarioDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String nombre = limpiar(request.getParameter("nombre"));
        String apellidoPaterno = limpiar(request.getParameter("apellidoPaterno"));
        String apellidoMaterno = limpiar(request.getParameter("apellidoMaterno"));
        String telefonoBruto = limpiar(request.getParameter("telefono"));
        String telefono = telefonoBruto.replaceAll("[^0-9]", "");
        String carrera = limpiar(request.getParameter("carrera"));
        String correo = limpiar(request.getParameter("email1")).toLowerCase();
        String contra1 = request.getParameter("contra1") == null ? "" : request.getParameter("contra1");
        String contra2 = request.getParameter("contra2") == null ? "" : request.getParameter("contra2");

        if (nombre.isEmpty() || apellidoPaterno.isEmpty() || apellidoMaterno.isEmpty() || carrera.isEmpty() || correo.isEmpty() || contra1.isEmpty() || contra2.isEmpty()) {
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

        if (!contra1.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$")) {
            enviarError(request, response, "La contraseña debe tener mínimo 8 caracteres, una mayúscula, un número y un carácter especial.");
            return;
        }

        try {
            if (usuarioDao.existeCorreo(correo)) {
                enviarError(request, response, "Ese correo ya está registrado.");
                return;
            }

            Usuario usuario = new Usuario(nombre, apellidoPaterno, apellidoMaterno, telefono, carrera, correo, contra1);
            boolean registrado = usuarioDao.create(usuario);

            if (registrado) {
                String plantillaHtml = """
                    <html>
                        <body style="font-family: Arial, sans-serif; color: #333333;">
                            <h2 style="color: #753618;">¡Holas, {0} {1}!</h2>
                            <p>Gracias por registrarte en la plataforma de UniMarket.</p>
                            <p style="font-size: 12px; color: #777777;">Si no te registraste en la plataforma, puedes ignorarlo.</p>
                        </body>
                    </html>
                    """;

                String cuerpoCorreo = MessageFormat.format(
                        plantillaHtml,
                        usuario.getNombres(),
                        usuario.getApellidoPaterno() + " " + usuario.getApellidoMaterno()
                );

                try {
                    // Intentamos enviar el correo
                    EmailSender.sendMail(
                            usuario.getCorreoInstitucional(),
                            "Bienvenido a UniMarket",
                            cuerpoCorreo
                    );
                    
                    // Si el código llega aquí, el correo se envió con éxito
                    request.setAttribute("mensaje", "¡Cuenta creada con éxito! Revisa tu correo y luego inicia sesión.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);

                } catch (Exception e) {
                    System.err.println("Fallo al enviar correo. Ejecutando ROLLBACK para: " + usuario.getCorreoInstitucional());
                    
                    // ROLLBACK: Eliminamos la cuenta que acabamos de crear porque el correo falló
                    usuarioDao.eliminarPorCorreo(usuario.getCorreoInstitucional());
                    
                    // Le avisamos al usuario que algo salió mal
                    enviarError(request, response, "No pudimos enviar el correo de confirmación. Revisa que tu dirección sea correcta e intenta de nuevo.");
                }

            } else {
                enviarError(request, response, "No se pudo crear la cuenta.");
            }
        } catch (SQLException e) {
            enviarError(request, response, "Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    private String limpiar(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private void enviarError(HttpServletRequest request, HttpServletResponse response, String mensaje) throws ServletException, IOException {
        request.setAttribute("error", mensaje);
        request.getRequestDispatcher("registro.jsp").forward(request, response);
    }
}