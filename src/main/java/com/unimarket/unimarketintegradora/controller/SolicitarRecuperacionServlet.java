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
import java.text.MessageFormat;

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
                    
                    String plantillaHtml = """
                    <!DOCTYPE html>
                    <html lang="es">
                    <head>
                        <meta charset="UTF-8">
                    </head>
                    <body style="margin: 0; padding: 0; background-color: #F7EDE7; font-family: Arial, Helvetica, sans-serif;">
                        
                        <!-- Franja superior oscura -->
                        <div style="background-color: #3E2723; padding: 15px 30px; text-align: left;">
                            <h2 style="color: #D7CCC8; margin: 0; font-size: 24px;">MUA</h2>
                        </div>

                        <!-- Contenedor principal -->
                        <div style="padding: 50px 20px; max-width: 650px; margin: 0 auto; text-align: center;">
                            
                            <h1 style="color: #3E2723; font-size: 28px; margin-top: 0;">¿Olvidaste tu contraseña?</h1>
                            
                            <p style="color: #333333; font-size: 16px; line-height: 1.5; margin-bottom: 40px; text-align: justify;">
                                Recibimos una solicitud para restablecer la contraseña de tu cuenta MUA. Si no fuiste tú, ignora este correo. Para continuar, utiliza el siguiente código de seguridad:
                            </p>
                            
                            <!-- Caja del Código -->
                            <div style="margin: 0 auto 40px; display: inline-block; padding: 15px 40px; border: 3px solid #3E2723; border-radius: 12px; font-size: 36px; font-weight: bold; color: #3E2723; letter-spacing: 4px;">
                                {0}
                            </div>
                            
                            <br>
                            
                            <!-- Botón -->
                            <a href="{1}" style="display: inline-block; width: 100%; max-width: 500px; background-color: #b58150; color: #ffffff; text-decoration: none; padding: 16px 0; font-size: 18px; font-weight: bold; border-radius: 8px; margin-bottom: 25px;">Restablecer contraseña</a>
                            
                            <!-- Enlace alternativo de seguridad -->
                            <p style="color: #333333; font-size: 13px;">
                                Si el botón no funciona, copia y pega el siguiente enlace en tu navegador:<br>
                                <a href="{1}" style="color: #3E2723; word-break: break-all;">{1}</a>
                            </p>

                        </div>

                        <!-- Pie de página oscuro -->
                        <div style="background-color: #C2A792; padding: 25px; text-align: center; color: #ffffff; font-size: 13px;">
                            <p style="margin: 0 0 15px;">
                                <a href="#" style="color: #ffffff; text-decoration: none; margin: 0 10px;">Contacto</a>
                                <a href="#" style="color: #ffffff; text-decoration: none; margin: 0 10px;">Política de Privacidad</a>
                                <a href="#" style="color: #ffffff; text-decoration: none; margin: 0 10px;">Términos y Condiciones</a>
                            </p>
                            <p style="margin: 0;">© 2024 MUA University Marketplace</p>
                        </div>

                    </body>
                    </html>
                    """;
                    String cuerpo = MessageFormat.format(plantillaHtml, token, urlVerificacion);
                    
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