package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.Articulo;
import com.unimarket.unimarketintegradora.model.dao.ArticuloDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/adminpublicaciones")
/**
 * Controlador web de MUA. Gestiona la interacción HTTP correspondiente a Admin Publicaciones Servlet.
 *
 * @author Equipo UniMarket
 */
public class AdminPublicacionesServlet extends HttpServlet {

    private final ArticuloDao articuloDao = new ArticuloDao();

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

        // Obtiene las publicaciones activas
        List<Articulo> listaArticulos = articuloDao.listarParaAdmin();
        request.setAttribute("listaArticulos", listaArticulos);

        request.getRequestDispatcher("/admin/publicaciones.jsp").forward(request, response);
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

        if ("eliminar".equals(accion)) {
            String idArticulo = request.getParameter("idArticulo");
            if (idArticulo != null && !idArticulo.trim().isEmpty()) {
                articuloDao.delete(idArticulo);
            }
        }

        response.sendRedirect(request.getContextPath() + "/adminpublicaciones");
    }
}