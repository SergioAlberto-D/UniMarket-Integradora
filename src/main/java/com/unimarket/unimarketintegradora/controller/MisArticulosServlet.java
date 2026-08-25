package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.Articulo;
import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.model.dao.ArticuloDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Servlet encargado de gestionar la visualización de los artículos publicados por el usuario autenticado,
 * clasificándolos en artículos disponibles y artículos en proceso de venta o negociación, y reenviándolos a la vista correspondiente.
 *
 * @author Luis Fernando Rodriguez Rayo
 * @date 2026-06-06
 */
@WebServlet(name = "MisArticulosServlet", value = "/mis-articulos")
/**
 * Controlador web de MUA. Gestiona la interacción HTTP correspondiente a Mis Articulos Servlet.
 *
 * @author Equipo UniMarket
 */
public class MisArticulosServlet extends HttpServlet {

    /**
     * Maneja las peticiones GET para validar la sesión del usuario, consultar los artículos publicados
     * separados por su estado (disponibles vs. en proceso) mediante la matrícula, y reenviar los datos al JSP.
     *
     * @param request  Objeto HttpServletRequest para gestionar la sesión y enviar atributos a la vista.
     * @param response Objeto HttpServletResponse para redirigir al login si no hay sesión activa o reenviar al JSP.
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