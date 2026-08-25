package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.Administrador;
import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.model.dao.AdministradorDao;
import com.unimarket.unimarketintegradora.model.dao.UsuarioDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet encargado de gestionar el proceso de inicio de sesión tanto para administradores como para usuarios normales,
 * validando credenciales, verificando estados de cuenta y estableciendo las sesiones correspondientes.
 *
 * @author Luis Fernando Rodriguez Rayo
 * @date 2026-06-06
 */
@WebServlet(name = "LoginServlet", value = "/login")
/**
 * Controlador web de MUA. Gestiona la interacción HTTP correspondiente a Login Servlet.
 *
 * @author Equipo UniMarket
 */
public class LoginServlet extends HttpServlet {

    private final UsuarioDao usuarioDao = new UsuarioDao();
    private final AdministradorDao adminDao = new AdministradorDao();

    /**
     * Maneja las peticiones POST para autenticar las credenciales del usuario o administrador,
     * validar si la cuenta se encuentra verificada y redirigir al panel o página principal según corresponda.
     *
     * @param request  Objeto HttpServletRequest con los parámetros de correo y contraseña del formulario de acceso.
     * @param response Objeto HttpServletResponse para redireccionar tras un login exitoso o reenviar al JSP en caso de error.
     * @throws ServletException Si ocurre un error específico del servlet.
     * @throws IOException      Si ocurre un error de E/S.
     * @author Luis Fernando Rodriguez Rayo
     * @date 2026-06-06
     */
    @Override
/**
 * Procesa una solicitud HTTP POST y ejecuta la operación solicitada.
 * @param request Parámetro de entrada de la operación.
 * @param response Parámetro de entrada de la operación.
 * @throws ServletException Excepción declarada por la operación.
 * @throws IOException Excepción declarada por la operación.
 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            // 1. Verificar si es ADMINISTRADOR
            Administrador admin = adminDao.validarLoginAdmin(email, password);
            if (admin != null) {
                HttpSession session = request.getSession();
                session.setAttribute("admin", admin);
                session.setAttribute("rol", "ADMIN");
                response.sendRedirect(request.getContextPath() + "/adminactividad");
                return;
            }

            // 2. Si no es admin, verificar si es USUARIO NORMAL
            Usuario usuario = usuarioDao.buscarPorCorreoYContrasena(email, password);
            if (usuario != null) {

                String estadoUsuario = usuario.getEstado();
                if (estadoUsuario == null || !estadoUsuario.equalsIgnoreCase("verificado")) {
                    request.setAttribute("error", "Acceso denegado. Tu cuenta no está verificada o disponible");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                    return;
                }
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario); // Guardamos al usuario en sesión
                session.setAttribute("rol", "USUARIO");

                response.sendRedirect(request.getContextPath() + "/inicio");// Redireccionamiento al la paguina principal
                return;
            }

            // 3. Si no existe en ninguno de los dos, error.
            request.setAttribute("error", "Correo o contraseña incorrectos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Ocurrió un error en el servidor. Intente más tarde.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}