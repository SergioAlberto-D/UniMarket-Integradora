package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.model.dao.UsuarioDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

// Definimos la URL clara y segura para el panel de administración
@WebServlet(name = "AdminUsuarioServlet", value = "/adminusuarios")
public class AdminUsuarioServlet extends HttpServlet {
    private final UsuarioDao usuarioDao = new UsuarioDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            // 1. Consultamos la lista de usuarios desde tu base de datos
            List<Usuario> lista = usuarioDao.listarUsuarios();

            // 2. Adjuntamos la lista al request para que el JSP la pueda iterar
            request.setAttribute("listaUsuarios", lista);

        } catch (SQLException e) {
            e.printStackTrace();
            // Enviamos el mensaje de error por si se requiere mostrar en el JSP
            request.setAttribute("error", "Error en la base de datos: " + e.getMessage());
        }

        // 3. Redirigimos el flujo al JSP que está dentro de la carpeta admin
        request.getRequestDispatcher("admin/usuarios.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}