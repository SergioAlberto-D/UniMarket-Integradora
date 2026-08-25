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
/**
 * Controlador web de MUA. Gestiona la interacción HTTP correspondiente a Admin Categoria Servlet.
 *
 * @author Equipo UniMarket
 */
public class AdminCategoriaServlet extends HttpServlet {

    private categoriaDao categoriaDao;

    @Override
/**
 * Ejecuta la operación 'init' del componente.
 * @throws ServletException Excepción declarada por la operación.
 */
    public void init() throws ServletException {
        this.categoriaDao = new categoriaDao();
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
            List<categoria> listaCategorias = categoriaDao.listarCategorias();
            request.setAttribute("listaCategorias", listaCategorias);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("/admin/categoria.jsp").forward(request, response);
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

                if (categoriaDao.existeCategoria(nombreCat.trim(), null)) {
                    if (esAjax) {
                        response.setStatus(HttpServletResponse.SC_CONFLICT);
                        response.getWriter().write(
                                "{\"success\": false, \"message\": \"Ya existe una categor\\u00eda con ese nombre.\"}");
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
                if (categoriaDao.existeCategoria(nombreCat.trim(), idCategoria)) {
                    if (esAjax) {
                        response.setStatus(HttpServletResponse.SC_CONFLICT);
                        response.getWriter().write(
                                "{\"success\": false, \"message\": \"Ya existe una categor\\u00eda con ese nombre.\"}");
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