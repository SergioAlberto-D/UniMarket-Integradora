package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.Notificacion;
import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.model.dao.NotificacionDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Servlet encargado de gestionar las notificaciones del usuario autenticado vía API,
 * permitiendo consultar las notificaciones no leídas en formato JSON y marcar todas como leídas.
 *
 * @author Luis Fernando Rodriguez Rayo
 * @date 2026-06-06
 */
@WebServlet(name = "NotificacionesServlet", urlPatterns = {"/api/notificaciones", "/api/notificaciones/marcar-leidas"})

public class NotificacionesServlet extends HttpServlet {
    private final NotificacionDao notificacionDao = new NotificacionDao();

    /**
     * Maneja las peticiones GET para validar la sesión del usuario, consultar sus notificaciones no leídas
     * mediante la matrícula y construir una respuesta JSON con la lista de notificaciones.
     *
     * @param request  Objeto HttpServletRequest para gestionar la sesión y los parámetros de la petición.
     * @param response Objeto HttpServletResponse para enviar la respuesta JSON con la codificación adecuada.
     * @throws ServletException Si ocurre un error específico del servlet.
     * @throws IOException      Si ocurre un error de E/S.
     * @author Luis Fernando Rodriguez Rayo
     * @date 2026-06-06
     */
    @Override
/**
 * Procesa una solicitud HTTP GET y prepara la respuesta correspondiente.
 * @param request Parámetro de entrada de la operación.
 * @param response Parámetro de entrada de la operación.
 * @throws ServletException Excepción declarada por la operación.
 * @throws IOException Excepción declarada por la operación.
 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            out.print("[]");
            return;
        }

        Usuario u = (Usuario) session.getAttribute("usuario");
        List<Notificacion> lista = notificacionDao.obtenerNoLeidas(u.getMatricula());

        // Construir JSON manual para no depender de librerías externas
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < lista.size(); i++) {
            Notificacion n = lista.get(i);
            json.append("{\"id\":").append(n.getIdNotificacion())
                    .append(",\"mensaje\":\"").append(n.getMensaje().replace("\"", "\\\"")).append("\"")
                    .append(",\"tipo\":\"").append(n.getTipo()).append("\"")
                    .append(",\"tiempo\":\"").append(n.getTiempoTranscurrido()).append("\"}");
            if (i < lista.size() - 1) json.append(",");
        }
        json.append("]");
        out.print(json.toString());
    }

    /**
     * Maneja las peticiones POST para validar la sesión del usuario y marcar todas sus notificaciones pendientes como leídas.
     *
     * @param request  Objeto HttpServletRequest para gestionar la sesión del usuario.
     * @param response Objeto HttpServletResponse para enviar la respuesta JSON confirmando el éxito de la operación.
     * @throws ServletException Si ocurre un error específico del servlet.
     * @throws IOException      Si ocurre un error de E/S.
     * @author Luis Fernando Rodriguez Rayo
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
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuario") != null) {
            Usuario u = (Usuario) session.getAttribute("usuario");
            notificacionDao.marcarTodasComoLeidas(u.getMatricula());
        }
        response.getWriter().print("{\"exito\":true}");
    }
}