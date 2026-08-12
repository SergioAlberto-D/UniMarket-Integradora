package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.dao.ReportesDao; // Ajusta al nombre de tu DAO
import com.unimarket.unimarketintegradora.model.Reportes;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/adminreportes")
public class AdminReportesServlet extends HttpServlet {

    private ReportesDao reporteDao;

    @Override
    public void init() throws ServletException {
        this.reporteDao = new ReportesDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Obtiene la lista de reportes
            List<Reportes> listaReportes = reporteDao.listarReportes();
            request.setAttribute("listaReportes", listaReportes);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("/admin/reportes.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        int idReporte = Integer.parseInt(request.getParameter("idReporte"));

        try {
            if ("atender".equals(accion)) {
                reporteDao.actualizarEstadoReporte(idReporte, "Atendido");
            } else if ("desestimar".equals(accion)) {
                reporteDao.actualizarEstadoReporte(idReporte, "Desestimado");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/adminreportes");
    }
}