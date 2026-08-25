package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.ContrasenaUsuario;
import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.model.dao.ContrasenaUsuarioDao;
import com.unimarket.unimarketintegradora.model.dao.UsuarioDao;
import com.unimarket.unimarketintegradora.utils.HashUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet encargado de procesar la actualización de contraseña del usuario
 * mediante un flujo de recuperación con token.
 *
 * @author Dulce Yazmin Canseco Juárez
 * @date 2026-06-06
 */
@WebServlet(name = "ActualizarPasswordServlet", value = "/ActualizarPasswordServlet")
/**
 * Controlador web de MUA. Gestiona la interacción HTTP correspondiente a Actualizar Password Servlet.
 *
 * @author Equipo UniMarket
 */
public class ActualizarPasswordServlet extends HttpServlet {
    private final UsuarioDao usuarioDao = new UsuarioDao();
    private final ContrasenaUsuarioDao contrasenaDao = new ContrasenaUsuarioDao();

    /**
     * Maneja las peticiones POST para validar el token y actualizar la contraseña en el sistema.
     *
     * @param request  Objeto HttpServletRequest con los parámetros de la petición (correo, token y contraseñas).
     * @param response Objeto HttpServletResponse para redirigir según el resultado del proceso.
     * @throws ServletException Si ocurre un error específico del servlet.
     * @throws IOException      Si ocurre un error de E/S.
     * @author Dulce Yazmin Canseco Juárez
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
        String correo = request.getParameter("correo");
        String token = request.getParameter("token");
        String contra1 = request.getParameter("contra1");
        String contra2 = request.getParameter("contra2");

        if (correo == null || token == null || contra1 == null || contra1.isEmpty() || !contra1.equals(contra2)) {
            request.setAttribute("error", "Datos inválidos o las contraseñas no coinciden.");
            request.getRequestDispatcher("nueva-password.jsp?correo=" + correo + "&token=" + token).forward(request, response);
            return;
        }

        if (usuarioDao.validarToken(correo, token)) {
            Usuario usuario = usuarioDao.buscarPorCorreo(correo);
            String passwordHasheada = HashUtils.convertirSHA256(contra1);

            ContrasenaUsuario passModel = new ContrasenaUsuario(usuario.getMatricula(), passwordHasheada);

            // Actualizamos en la tabla de contraseñas
            if (contrasenaDao.update(passModel)) {
                // Limpiamos el token del usuario
                usuarioDao.limpiarToken(correo);
                request.setAttribute("mensaje", "¡Tu contraseña ha sido actualizada con éxito!");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Error al actualizar la contraseña.");
                request.getRequestDispatcher("nueva-password.jsp?correo=" + correo + "&token=" + token).forward(request, response);
            }
        } else {
            request.setAttribute("error", "Código caducado o inválido.");
            request.getRequestDispatcher("recuperar-password.jsp").forward(request, response);
        }
    }
}