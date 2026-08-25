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

/**
 * Servlet encargado de administrar las publicaciones de artículos dentro del panel de control.
 *
 * @author Dulce Yazmin Canseco Juárez
 * @date 2026-06-06
 */
@WebServlet("/adminpublicaciones")
public class AdminPublicacionesServlet extends HttpServlet {

    private final ArticuloDao articuloDao = new ArticuloDao();

    /**
     * Maneja las peticiones GET para obtener y listar las publicaciones destinadas a la vista de administración.
     *
     * @param request  Objeto HttpServletRequest para enviar los artículos a la vista.
     * @param response Objeto HttpServletResponse para reenviar la solicitud al JSP.
     * @throws ServletException Si ocurre un error específico del servlet.
     * @throws IOException      Si ocurre un error de E/S.
     * @author Dulce Yazmin Canseco Juárez
     * @date 2026-06-06
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtiene las publicaciones activas
        List<Articulo> listaArticulos = articuloDao.listarParaAdmin();
        request.setAttribute("listaArticulos", listaArticulos);

        request.getRequestDispatcher("/admin/publicaciones.jsp").forward(request, response);
    }

    /**
     * Maneja las peticiones POST para procesar acciones administrativas sobre las publicaciones, como eliminar artículos.
     *
     * @param request  Objeto HttpServletRequest con los parámetros de la acción y el ID del artículo.
     * @param response Objeto HttpServletResponse para redireccionar al panel de publicaciones.
     * @throws ServletException Si ocurre un error específico del servlet.
     * @throws IOException      Si ocurre un error de E/S.
     * @author Dulce Yazmin Canseco Juárez
     * @date 2026-06-06
     */
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