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

@WebServlet(name = "HistorialActividadServlet", value = "/historial")
public class HistorialActividadServlet extends HttpServlet {

    @Override
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