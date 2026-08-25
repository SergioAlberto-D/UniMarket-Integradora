package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.dao.UsuarioDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "VerificarCuentaServlet", value = "/verificar-cuenta")
/**
 * Controlador web de MUA. Gestiona la interacción HTTP correspondiente a Verificar Cuenta Servlet.
 *
 * @author Equipo UniMarket
 */
public class VerificarCuentaServlet extends HttpServlet {

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