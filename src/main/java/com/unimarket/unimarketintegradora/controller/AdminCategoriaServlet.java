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

        if (esAjax) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
        }

        try {
            if ("agregar".equals(accion)) {

                String nombreCat = request.getParameter("nombreCategoria");
                if (nombreCat == null || nombreCat.trim().isEmpty()) {
                    if (esAjax) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write(
                                "{\"success\": false, \"message\": \"El nombre de la categor\\u00eda es obligatorio.\"}");
                        return;
                    }
                    response.sendRedirect(request.getContextPath() + "/admincategorias");
                    return;
                }

                categoria nuevaCat = new categoria(nombreCat.trim());
                categoriaDao.agregarCategoria(nuevaCat);

                if (esAjax) {
                    // NOTA: asume que agregarCategoria() setea el id generado en nuevaCat.
                    // Si tu DAO no hace esto, avísame y ajustamos para recuperar el id.
                    String json = "{\"success\": true, \"idCategoria\": " + nuevaCat.getIdCategoria()
                            + ", \"nombreCategoria\": \"" + escapeJson(nuevaCat.getCategoria()) + "\"}";
                    response.getWriter().write(json);
                    return;
                }

            } else if ("editar".equals(accion)) {

                int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
                String nombreCat = request.getParameter("nombreCategoria");

                if (nombreCat == null || nombreCat.trim().isEmpty()) {
                    if (esAjax) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write(
                                "{\"success\": false, \"message\": \"El nombre de la categor\\u00eda es obligatorio.\"}");
                        return;
                    }
                    response.sendRedirect(request.getContextPath() + "/admincategorias");
                    return;
                }

                categoria catEditar = new categoria();
                catEditar.setIdCategoria(idCategoria);
                catEditar.setCategoria(nombreCat.trim());
                categoriaDao.editarCategoria(catEditar);

                if (esAjax) {
                    String json = "{\"success\": true, \"idCategoria\": " + idCategoria
                            + ", \"nombreCategoria\": \"" + escapeJson(nombreCat.trim()) + "\"}";
                    response.getWriter().write(json);
                    return;
                }

            } else if ("eliminar".equals(accion)) {

                int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
                categoriaDao.eliminarCategoria(idCategoria);

                if (esAjax) {
                    response.getWriter().write(
                            "{\"success\": true, \"idCategoria\": " + idCategoria + "}");
                    return;
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            if (esAjax) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(
                        "{\"success\": false, \"message\": \"Identificador de categor\\u00eda inv\\u00e1lido.\"}");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (esAjax) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write(
                        "{\"success\": false, \"message\": \"Error en el servidor al procesar la solicitud.\"}");
                return;
            }
        }

        response.sendRedirect(request.getContextPath() + "/admincategorias");
    }

    /**
     * Escapa comillas y backslashes para incrustar texto de forma segura en JSON armado a mano.
     */
    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}