package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.Articulo;
import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.model.dao.ArticuloDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "MisArticulosServlet", value = "/mis-articulos")
/**
 * Controlador web de MUA. Gestiona la interacción HTTP correspondiente a Mis Articulos Servlet.
 *
 * @author Equipo UniMarket
 */
public class MisArticulosServlet extends HttpServlet {

    @Override
/**
 * Procesa una solicitud HTTP GET y prepara la respuesta correspondiente.
 * @param request Parámetro de entrada de la operación.
 * @param response Parámetro de entrada de la operación.
 * @throws ServletException Excepción declarada por la operación.
 * @throws IOException Excepción declarada por la operación.
 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        ArticuloDao articuloDao = new ArticuloDao();

        // Obtiene las dos listas desde la BD
        List<Articulo> disponibles = articuloDao.obtenerPorUsuarioYEstado(usuario.getMatricula(), false); // No están en PENDIENTE
        List<Articulo> enProceso = articuloDao.obtenerPorUsuarioYEstado(usuario.getMatricula(), true);  // Están en PENDIENTE

        request.setAttribute("disponibles", disponibles);
        request.setAttribute("enProceso", enProceso);

        request.getRequestDispatcher("mis-articulos.jsp").forward(request, response);
    }
}