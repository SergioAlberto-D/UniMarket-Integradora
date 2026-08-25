package com.unimarket.unimarketintegradora.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet encargado de gestionar el cierre de sesión de los usuarios y administradores,
 * invalidando la sesión activa y redirigiendo a la vista de inicio de sesión.
 *
 * @author Luis Fernando Rodriguez Rayo
 * @date 2026-06-06
 */
@WebServlet(name = "LogoutServlet", value = "/logout")
public class LogoutServlet extends HttpServlet {

    /**
     * Maneja las peticiones GET para invalidar la sesión actual si existe y redirigir al formulario de acceso (login.jsp).
     *
     * @param request  Objeto HttpServletRequest con la información de la petición.
     * @param response Objeto HttpServletResponse para redirigir al login.
     * @throws ServletException Si ocurre un error específico del servlet.
     * @throws IOException      Si ocurre un error de E/S.
     * @author Luis Fernando Rodriguez Rayo
     * @date 2026-06-06
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("login.jsp");
    }

    /**
     * Maneja las peticiones POST delegando la lógica al método doGet para invalidar la sesión de forma unificada.
     *
     * @param request  Objeto HttpServletRequest con la información de la petición.
     * @param response Objeto HttpServletResponse para redirigir al login.
     * @throws ServletException Si ocurre un error específico del servlet.
     * @throws IOException      Si ocurre un error de E/S.
     * @author Luis Fernando Rodriguez Rayo
     * @date 2026-06-06
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}