package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.Actividad;
import com.unimarket.unimarketintegradora.model.dao.ActividadDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/adminactividad")
public class AdminActividadServlet extends HttpServlet {

    private final ActividadDao actividadDao = new ActividadDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int totalPublicaciones = actividadDao.contarTotalPublicaciones();
        int totalUsuarios = actividadDao.contarTotalUsuarios();
        List<Actividad> listaActividad = actividadDao.obtenerActividadReciente();

        request.setAttribute("totalPublicaciones", totalPublicaciones);
        request.setAttribute("totalUsuarios", totalUsuarios);
        request.setAttribute("listaActividad", listaActividad);

        request.getRequestDispatcher("/admin/actividad.jsp").forward(request, response);
    }
}