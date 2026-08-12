package com.unimarket.unimarketintegradora.controller.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {
        // Servlets del admin
        "/adminactividad",
        "/admincategorias",
        "/adminpublicaciones",
        "/adminreportes",

        // Vistas JSP directas del admin
        "/admin/actividad.jsp",
        "/admin/categoria.jsp",
        "/admin/publicaciones.jsp",
        "/admin/reportes.jsp"
})
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        if (session != null && session.getAttribute("admin") != null) {
            chain.doFilter(request, response);
        } else {
            if (session != null && session.getAttribute("usuario") != null) {
                res.sendRedirect(req.getContextPath() + "/inicio");
            } else {
                res.sendRedirect(req.getContextPath() + "/login.jsp");
            }
        }
    }
}