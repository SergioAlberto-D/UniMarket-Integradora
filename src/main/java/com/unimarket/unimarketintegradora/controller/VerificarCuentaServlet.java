package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.dao.UsuarioDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet encargado de procesar la verificación y activación de cuentas de usuario mediante un enlace por correo electrónico,
 * validando el parámetro de correo proporcionado, invocando al DAO para cambiar el estado de activación en la base de datos
 * y reenviando el resultado a la vista de inicio de sesión.
 *
 * @author Luis Fernando Rodriguez Rayo
 * @date 2026-06-06
 */
@WebServlet(name = "VerificarCuentaServlet", value = "/verificar-cuenta")
/**
 * Controlador web de MUA. Gestiona la interacción HTTP correspondiente a Verificar Cuenta Servlet.
 *
 * @author Equipo UniMarket
 */
public class VerificarCuentaServlet extends HttpServlet {

    /**
     * Maneja las peticiones GET para extraer el correo electrónico de los parámetros de la URL, validar su existencia,
     * activar la cuenta correspondiente en la base de datos y establecer los mensajes de éxito o error para la vista de login.
     *
     * @param request  Objeto HttpServletRequest con el parámetro de correo electrónico.
     * @param response Objeto HttpServletResponse para reenviar al JSP de inicio de sesión con los atributos de estado.
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
        // Obtenemos el correo desde la URL del enlace (ej. /verificar-cuenta?correo=matricula@utez.edu.mx)
        String correo = request.getParameter("correo");

        if (correo != null && !correo.trim().isEmpty()) {
            UsuarioDao dao = new UsuarioDao();
            boolean exito = dao.activarCuenta(correo);

            if (exito) {
                request.setAttribute("cuentaVerificada", "¡Tu cuenta ha sido verificada correctamente! Ya puedes iniciar sesión.");
            } else {
                request.setAttribute("errorVerificacion", "El enlace no es válido o tu cuenta ya estaba verificada.");
            }
        } else {
            request.setAttribute("errorVerificacion", "No se proporcionó un correo válido.");
        }

        // Mandamos a la vista final
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}