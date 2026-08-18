package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.model.dao.UsuarioDao;
import com.unimarket.unimarketintegradora.utils.EmailSender;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "AdminActividadServlet", value = "/adminactividad")
public class AdminActividadServlet extends HttpServlet {

    private final UsuarioDao usuarioDao = new UsuarioDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtenemos solo los unverificados
        List<Usuario> peticiones = usuarioDao.obtenerPeticiones();
        request.setAttribute("listaPeticiones", peticiones);
        request.getRequestDispatcher("/admin/actividad.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        String matricula = request.getParameter("matricula");
        String correo = request.getParameter("correo");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        if ("aceptar".equals(accion)) {
            boolean verificado = usuarioDao.verificarUsuario(matricula);
            if (verificado) {
                // Nuevo correo de bienvenida
                String msjBienvenida = "<h2>¡Bienvenido a MUA!</h2>" +
                        "<p>Un moderador ha verificado tu identidad. Tu cuenta ha sido activada y ya puedes iniciar sesión.</p>" +
                        "<p>Recuerda respetar a los demás y leer nuestros términos y condiciones.</p>";
                EmailSender.sendMail(correo, "¡Cuenta Verificada en MUA!", msjBienvenida);

                out.print("{\"exito\": true, \"mensaje\": \"Usuario verificado y correo enviado.\"}");
            } else {
                out.print("{\"exito\": false, \"mensaje\": \"Hubo un problema al verificar en la base de datos.\"}");
            }
        } else if ("rechazar".equals(accion)) {
            boolean eliminado = usuarioDao.rechazarUsuario(matricula);
            if (eliminado) {
                out.print("{\"exito\": true, \"mensaje\": \"Petición rechazada permanentemente.\"}");
            } else {
                out.print("{\"exito\": false, \"mensaje\": \"Error al eliminar el usuario.\"}");
            }
        }
    }
}