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
/**
 * Controlador web de MUA. Gestiona la interacción HTTP correspondiente a Admin Reportes Servlet.
 *
 * @author Equipo UniMarket
 */
public class AdminReportesServlet extends HttpServlet {

    private ReportesDao reporteDao;

    @Override
/**
 * Ejecuta la operación 'init' del componente.
 * @throws ServletException Excepción declarada por la operación.
 */
    public void init() throws ServletException {
        this.reporteDao = new ReportesDao();
    }

    @Override
/**
 * Procesa una solicitud HTTP GET y prepara la respuesta correspondiente.
 * @param request Parámetro de entrada de la operación.
 * @param response Parámetro de entrada de la operación.
 * @throws ServletException Excepción declarada por la operación.
 * @throws IOException Excepción declarada por la operación.
 */
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
/**
 * Procesa una solicitud HTTP POST y ejecuta la operación solicitada.
 * @param request Parámetro de entrada de la operación.
 * @param response Parámetro de entrada de la operación.
 * @throws ServletException Excepción declarada por la operación.
 * @throws IOException Excepción declarada por la operación.
 */
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