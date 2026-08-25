package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.TransaccionDTO;
import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.model.dao.TransaccionDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Servlet encargado de obtener y gestionar el historial de actividad y transacciones del usuario logueado,
 * consultando los datos correspondientes y reenviándolos a la vista para su visualización.
 *
 * @author Luis Fernando Rodriguez Rayo
 * @date 2026-06-06
 */
@WebServlet(name = "HistorialActividadServlet", value = "/historial")

public class HistorialActividadServlet extends HttpServlet {

    /**
     * Maneja las peticiones GET para validar la sesión del usuario, consultar su historial de transacciones
     * mediante la matrícula y reenviar los datos al JSP correspondiente.
     *
     * @param request  Objeto HttpServletRequest para gestionar la sesión y atributos de la vista.
     * @param response Objeto HttpServletResponse para reenviar al JSP o redirigir al login en caso de no haber sesión.
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
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        TransaccionDao transaccionDao = new TransaccionDao();

        // Ejecutamos la consulta dinámica con la matrícula del usuario logueado
        List<TransaccionDTO> historial = transaccionDao.obtenerHistorialActividad(usuario.getMatricula());

        // Enviamos la data a la vista
        request.setAttribute("historial", historial);
        request.getRequestDispatcher("/historial-actividad.jsp").forward(request, response);
    }
}