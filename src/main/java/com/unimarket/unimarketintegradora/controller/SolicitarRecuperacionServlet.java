package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.dao.UsuarioDao;
import com.unimarket.unimarketintegradora.utils.EmailSender;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.SecureRandom;

@WebServlet(name = "SolicitarRecuperacionServlet", value = "/SolicitarRecuperacionServlet")
public class SolicitarRecuperacionServlet extends HttpServlet {
    private final UsuarioDao usuarioDao = new UsuarioDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String correo = request.getParameter("correo");
        
        // 1. Definimos el mensaje estándar que SIEMPRE se mostrará
        String mensajeSeguridad = "Si el correo está registrado, recibirás un código y un enlace para recuperar tu cuenta.";

        try {
            // 2. Verificamos si el correo existe (solo el backend sabrá esto)
            if (correo != null && !correo.trim().isEmpty() && usuarioDao.existeCorreo(correo)) {
                String token = generarToken();

                if (usuarioDao.guardarTokenRecuperacion(correo, token)) {
                    
                    // Construimos la URL dinámica incluyendo el correo y el token automáticamente
                    String urlVerificacion = request.getScheme() + "://" + request.getServerName() + ":" + 
                                             request.getServerPort() + request.getContextPath() + 
                                             "/verificar-token.jsp?correo=" + correo + "&token=" + token;
                    
                    String cuerpo = "<h2 style='color: #753618;'>Recuperación de contraseña</h2>" +
                                    "<p>Tu código de seguridad es: <strong>" + token + "</strong></p>" +
                                    "<p>Ingresa este código en el siguiente enlace: <br><a href='" + urlVerificacion + "'>Ir a verificar código</a></p>" +
                                    "<p>Este código caduca en 15 minutos.</p>";
                    
                    // Se envía el correo de forma asíncrona o protegida
                    EmailSender.sendMail(correo, "Código de Recuperación - MUA", cuerpo);
                }
            }
            
            // 3. LA CLAVE DE SEGURIDAD: 
            // Sin importar si el correo existía o no, o si el if anterior se ejecutó, 
            // SIEMPRE redirigimos al login con el mensaje de "éxito".
            request.setAttribute("mensaje", mensajeSeguridad);
            request.getRequestDispatcher("login.jsp").forward(request, response);

        } catch (Exception e) {
            // Incluso en caso de error interno, evitamos dar detalles técnicos
            request.setAttribute("error", "Ocurrió un error al procesar la solicitud. Intenta más tarde.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    private String generarToken() {
        SecureRandom random = new SecureRandom();
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder token = new StringBuilder(9);
        
        for (int i = 0; i < 8; i++) {
            if (i == 4) token.append("-");
            token.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return token.toString();
    }
}