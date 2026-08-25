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

@WebServlet(name = "NotificacionesServlet", urlPatterns = {"/api/notificaciones", "/api/notificaciones/marcar-leidas"})
/**
 * Controlador web de MUA. Gestiona la interacción HTTP correspondiente a Notificaciones Servlet.
 *
 * @author Equipo UniMarket
 */
public class NotificacionesServlet extends HttpServlet {
    private final NotificacionDao notificacionDao = new NotificacionDao();

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