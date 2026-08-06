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
public class AdminPublicacionesServlet extends HttpServlet {

    private final ArticuloDao articuloDao = new ArticuloDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtiene las publicaciones activas
        List<Articulo> listaArticulos = articuloDao.listarParaAdmin();
        request.setAttribute("listaArticulos", listaArticulos);

        request.getRequestDispatcher("/admin/publicaciones.jsp").forward(request, response);
    }

    @Override
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