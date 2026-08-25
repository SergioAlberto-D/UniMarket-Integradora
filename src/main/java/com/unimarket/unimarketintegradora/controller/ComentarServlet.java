package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.Comentario;
import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.model.dao.ComentarioDao;
import com.unimarket.unimarketintegradora.model.dao.NotificacionDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet encargado de procesar la creación de comentarios y calificaciones para los perfiles de los vendedores,
 * así como de generar las notificaciones correspondientes.
 *
 * @author Dulce Yazmin Canseco Juárez
 * @date 2026-06-06
 */
@WebServlet(name = "ComentarServlet", value = "/comentar-vendedor")

public class ComentarServlet extends HttpServlet {
    private final ComentarioDao comentarioDao = new ComentarioDao();
    private final NotificacionDao notificacionDao = new NotificacionDao();

    /**
     * Maneja las peticiones POST para validar la sesión del usuario, verificar los datos del comentario,
     * guardarlo en la base de datos y enviar una notificación al vendedor receptor.
     *
     * @param request  Objeto HttpServletRequest con los parámetros del receptor, comentario y calificación.
     * @param response Objeto HttpServletResponse para enviar la respuesta en formato JSON.
     * @throws ServletException Si ocurre un error específico del servlet.
     * @throws IOException      Si ocurre un error de E/S.
     * @author Dulce Yazmin Canseco Juárez
     * @date 2026-06-06
     */
    @Override
/**
 * Procesa una solicitud HTTP POST y ejecuta la operación solicitada.
 * @param request Parámetro de entrada de la operación.
 * @param response Parámetro de entrada de la operación.
 * @throws ServletException Excepción declarada por la operación.
 * @throws IOException Excepción declarada por la operación.
 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            out.print("{\"exito\": false, \"mensaje\": \"Debes iniciar sesión para comentar.\"}");
            return;
        }

        Usuario remitente = (Usuario) session.getAttribute("usuario");
        String matriculaRemitente = remitente.getMatricula();
        String matriculaReceptor = request.getParameter("matriculaReceptor");
        String textoComentario = request.getParameter("comentario");
        String califStr = request.getParameter("calificacion");

        if (matriculaRemitente.equalsIgnoreCase(matriculaReceptor)) {
            out.print("{\"exito\": false, \"mensaje\": \"No puedes calificar tu propio perfil.\"}");
            return;
        }

        if (textoComentario == null || textoComentario.trim().isEmpty() || califStr == null) {
            out.print("{\"exito\": false, \"mensaje\": \"Completa el comentario y elige una calificación.\"}");
            return;
        }

        try {
            int calificacion = Integer.parseInt(califStr);
            if (calificacion < 1 || calificacion > 5) calificacion = 5;

            Comentario comentarioObj = new Comentario(
                    textoComentario.trim(),
                    calificacion,
                    matriculaRemitente,
                    matriculaReceptor
            );

            boolean guardado = comentarioDao.create(comentarioObj);
            if (guardado) {
                // MODIFICACIÓN: Mensaje personalizado con el nombre de quien comenta y el texto del comentario
                String msjVendedor = remitente.getNombre() + " comentó: \"" + textoComentario.trim() + "\"";
                notificacionDao.crearNotificacion(matriculaReceptor, msjVendedor, "RESEÑA");

                out.print("{\"exito\": true, \"mensaje\": \"¡Comentario publicado correctamente!\"}");
            } else {
                out.print("{\"exito\": false, \"mensaje\": \"Ocurrió un error al guardar tu reseña en la base de datos.\"}");
            }
        } catch (NumberFormatException e) {
            out.print("{\"exito\": false, \"mensaje\": \"La calificación de estrellas no es válida.\"}");
        }
    }
}