package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.dao.categoriaDao;
import com.unimarket.unimarketintegradora.model.categoria;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admincategorias")
public class AdminCategoriaServlet extends HttpServlet {

    private categoriaDao categoriaDao;

    @Override
    public void init() throws ServletException {
        this.categoriaDao = new categoriaDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<categoria> listaCategorias = categoriaDao.listarCategorias();
            request.setAttribute("listaCategorias", listaCategorias);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("/admin/categoria.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        boolean esAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

        try {
            if ("agregar".equals(accion)) {
                String nombreCat = request.getParameter("nombreCategoria");
                if (nombreCat != null && !nombreCat.trim().isEmpty()) {
                    categoria nuevaCat = new categoria(nombreCat.trim());
                    categoriaDao.agregarCategoria(nuevaCat);
                }

                if (esAjax) {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("{\"success\": true}");
                    return;
                }
            } else if ("editar".equals(accion)) {
                int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
                String nombreCat = request.getParameter("nombreCategoria");

                if (nombreCat != null && !nombreCat.trim().isEmpty()) {
                    categoria catEditar = new categoria();
                    catEditar.setIdCategoria(idCategoria);
                    catEditar.setCategoria(nombreCat.trim());

                    categoriaDao.editarCategoria(catEditar);
                }
            } else if ("eliminar".equals(accion)) {
                int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
                categoriaDao.eliminarCategoria(idCategoria);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (esAjax) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"success\": false}");
                return;
            }
        }

        response.sendRedirect(request.getContextPath() + "/admincategorias");
    }
}