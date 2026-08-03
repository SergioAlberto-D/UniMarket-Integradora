package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.Articulo;
import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.model.dao.ArticuloDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "InicioServlet", value = "/inicio")
public class InicioServlet extends HttpServlet {

    private final ArticuloDao articuloDao = new ArticuloDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Leer parámetros del filtro
        String orden = request.getParameter("orden");
        String catStr = request.getParameter("categoria");
        String divStr = request.getParameter("division");
        String minStr = request.getParameter("minPrecio");
        String maxStr = request.getParameter("maxPrecio");

        Integer categoria = (catStr != null && !catStr.isEmpty()) ? Integer.parseInt(catStr) : 0;
        Integer division = (divStr != null && !divStr.isEmpty()) ? Integer.parseInt(divStr) : 0;

        BigDecimal minPrecio = (minStr != null && !minStr.trim().isEmpty()) ? new BigDecimal(minStr.trim()) : BigDecimal.ZERO;
        BigDecimal maxPrecio = (maxStr != null && !maxStr.trim().isEmpty()) ? new BigDecimal(maxStr.trim()) : null;

        // 2. Obtener lista filtrada desde la base de datos
        List<Articulo> articulos = articuloDao.filtrarArticulos(orden, categoria, division, minPrecio, maxPrecio);

        // 3. VERIFICAR SI TIENE UN ARTÍCULO EN PROCESO DE VENTA (Para lanzar la notificación)
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuario") != null) {
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            List<Articulo> enProceso = articuloDao.obtenerPorUsuarioYEstado(usuario.getIdUsuario(), true);
            if (!enProceso.isEmpty()) {
                // Le mandamos el primer artículo pendiente que encontremos
                request.setAttribute("articuloRecordatorio", enProceso.get(0));
            }
        }

        // 4. Devolver atributos a la vista
        request.setAttribute("listaArticulos", articulos);
        request.setAttribute("ordenSel", orden != null ? orden : "Recientes");
        request.setAttribute("catSel", categoria);
        request.setAttribute("divSel", division);
        request.setAttribute("minSel", (minStr != null && !minStr.trim().isEmpty()) ? minStr : "");
        request.setAttribute("maxSel", (maxStr != null && !maxStr.trim().isEmpty()) ? maxStr : "");

        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}