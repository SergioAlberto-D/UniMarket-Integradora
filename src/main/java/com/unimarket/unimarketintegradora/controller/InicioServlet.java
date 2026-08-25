package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.Articulo;
import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.model.categoria;
import com.unimarket.unimarketintegradora.model.dao.ArticuloDao;

import com.unimarket.unimarketintegradora.model.dao.categoriaDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Servlet encargado de gestionar la página principal y el catálogo de artículos,
 * permitiendo filtrar por orden, categoría, división y rangos de precio, además de soportar peticiones AJAX para fragmentos de vista.
 *
 * @author Luis Fernando Rodriguez Rayo
 * @date 2026-06-06
 */
@WebServlet(name = "InicioServlet", value = "/inicio")
/**
 * Controlador web de MUA. Gestiona la interacción HTTP correspondiente a Inicio Servlet.
 *
 * @author Equipo UniMarket
 */
public class InicioServlet extends HttpServlet {

    private final ArticuloDao articuloDao = new ArticuloDao();
    private final categoriaDao categoriaDao = new categoriaDao();

    /**
     * Maneja las peticiones GET para obtener la sesión del usuario, procesar los filtros del catálogo (orden, categorías, precios),
     * consultar los artículos disponibles y decidir si se responde con la página completa o con un fragmento vía AJAX.
     *
     * @param request  Objeto HttpServletRequest con los parámetros de filtrado y cabeceras de la petición.
     * @param response Objeto HttpServletResponse para reenviar al JSP correspondiente o fragmento de artículos.
     * @throws ServletException Si ocurre un error específico del servlet.
     * @throws IOException      Si ocurre un error de E/S.
     * @author Luis Fernando Rodriguez Rayo
     * @date 2026-06-06
     */
    @Override
/**
 * Procesa una solicitud HTTP GET y prepara la respuesta correspondiente.
 * @param request Parámetro de entrada de la operación.
 * @param response Parámetro de entrada de la operación.
 * @throws ServletException Excepción declarada por la operación.
 * @throws IOException Excepción declarada por la operación.
 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Obtener la sesión y la matrícula del usuario actual (si ha iniciado sesión)
        HttpSession session = request.getSession(false);
        String matriculaActual = "";

        if (session != null && session.getAttribute("usuario") != null) {
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            matriculaActual = usuario.getMatricula();

            // Verificamos también si tiene un artículo en proceso de venta para la notificación
            List<Articulo> enProceso = articuloDao.obtenerPorUsuarioYEstado(usuario.getMatricula(), true);
            if (!enProceso.isEmpty()) {
                request.setAttribute("articuloRecordatorio", enProceso.get(0));
            }
        }

        // 2. Leer parámetros del filtro
        String orden = request.getParameter("orden");
        String catStr = request.getParameter("categoria");
        String divStr = request.getParameter("division");
        String minStr = request.getParameter("minPrecio");
        String maxStr = request.getParameter("maxPrecio");

        Integer categoria = (catStr != null && !catStr.isEmpty()) ? Integer.parseInt(catStr) : 0;
        Integer division = (divStr != null && !divStr.isEmpty()) ? Integer.parseInt(divStr) : 0;

        BigDecimal minPrecio = (minStr != null && !minStr.trim().isEmpty()) ? new BigDecimal(minStr.trim()) : BigDecimal.ZERO;
        BigDecimal maxPrecio = (maxStr != null && !maxStr.trim().isEmpty()) ? new BigDecimal(maxStr.trim()) : null;

        // 3. Obtener lista filtrada excluyendo los artículos propios del usuario actual
        List<Articulo> articulos = articuloDao.filtrarArticulos(orden, categoria, division, minPrecio, maxPrecio, matriculaActual);

        // 4. Obtener las categorías
        List<categoria> categorias = categoriaDao.getAll();

        // 5. Devolver atributos a la vista
        request.setAttribute("listaArticulos", articulos);
        request.setAttribute("ordenSel", orden != null ? orden : "Recientes");
        request.setAttribute("catSel", categoria);
        request.setAttribute("divSel", division);
        request.setAttribute("minSel", (minStr != null && !minStr.trim().isEmpty()) ? minStr : "");
        request.setAttribute("maxSel", (maxStr != null && !maxStr.trim().isEmpty()) ? maxStr : "");
        request.setAttribute("categorias", categorias);

        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

        if (isAjax) {
            request.getRequestDispatcher("articulos-fragment.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }
}