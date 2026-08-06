package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.model.dao.UsuarioDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminUsuarioServlet", urlPatterns = {"/adminusuarios"})
public class AdminUsuarioServlet extends HttpServlet {

    private UsuarioDao usuarioDao;

    @Override
    public void init() {
        usuarioDao = new UsuarioDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        String matricula = request.getParameter("matricula");

        // Acción para desactivar (borrado lógico)
        if ("desactivar".equals(accion) || "eliminar".equals(accion)) {
            if (matricula != null && !matricula.isEmpty()) {
                usuarioDao.cambiarEstado(matricula, "INACTIVO");
            }
            response.sendRedirect(request.getContextPath() + "/adminusuarios");
            return;
        }
        // Acción para activar desde GET
        else if ("activar".equals(accion)) {
            if (matricula != null && !matricula.isEmpty()) {
                usuarioDao.activarUsuario(matricula);
            }
            response.sendRedirect(request.getContextPath() + "/adminusuarios");
            return;
        }

        // Cargar lista completa desde el DAO y enviarla al JSP
        List<Usuario> listaUsuarios = usuarioDao.getAll();
        request.setAttribute("listaUsuarios", listaUsuarios);

        // Redirige al JSP del panel de administración de usuarios
        request.getRequestDispatcher("/admin/usuarios.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("accion");

        if ("desactivar".equals(accion) || "eliminar".equals(accion)) {
            String idUsuario = request.getParameter("idUsuario");
            if (idUsuario == null || idUsuario.isEmpty()) {
                idUsuario = request.getParameter("matricula");
            }
            if (idUsuario != null && !idUsuario.isEmpty()) {
                usuarioDao.cambiarEstado(idUsuario, "INACTIVO");
            }
        } else if ("activar".equals(accion)) {
            String idUsuario = request.getParameter("idUsuario");
            if (idUsuario == null || idUsuario.isEmpty()) {
                idUsuario = request.getParameter("matricula");
            }
            if (idUsuario != null && !idUsuario.isEmpty()) {
                usuarioDao.activarUsuario(idUsuario);
            }
        } else {
            // Obtención de parámetros según el formulario de edición o creación
            String matricula = request.getParameter("matricula");
            String nombre = request.getParameter("nombre");
            String apellidoPaterno = request.getParameter("apellidoPaterno");
            String apellidoMaterno = request.getParameter("apellidoMaterno");
            String numeroCelular = request.getParameter("numeroCelular");

            int idDivision = 0;
            if (request.getParameter("idDivisionAcademica") != null && !request.getParameter("idDivisionAcademica").isEmpty()) {
                idDivision = Integer.parseInt(request.getParameter("idDivisionAcademica"));
            }

            String correoInstitucional = request.getParameter("correoInstitucional");

            int idRol = 1;
            if (request.getParameter("idRol") != null && !request.getParameter("idRol").isEmpty()) {
                idRol = Integer.parseInt(request.getParameter("idRol"));
            }

            String estado = request.getParameter("estado");

            Usuario usuario = new Usuario(
                    nombre,
                    apellidoPaterno,
                    apellidoMaterno,
                    numeroCelular,
                    idDivision,
                    null,
                    correoInstitucional,
                    idRol,
                    estado
            );
            usuario.setIdUsuario(matricula);

            if ("actualizar".equals(accion)) {
                usuarioDao.update(usuario);
            } else {
                usuarioDao.create(usuario);
            }
        }

        response.sendRedirect(request.getContextPath() + "/adminusuarios");
    }
}