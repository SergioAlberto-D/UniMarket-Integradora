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
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    private final UsuarioDao usuarioDao = new UsuarioDao();
    private final ContrasenaUsuarioDao contrasenaDao = new ContrasenaUsuarioDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String correo = limpiar(request.getParameter("email")).toLowerCase();
        String contraPlana = limpiar(request.getParameter("password"));

        if (correo.isEmpty() || contraPlana.isEmpty()) {
            enviarError(request, response, "Ingresa tu correo y contraseña.");
            return;
        }

        try {
            // 1. Buscar al usuario por correo
            Usuario usuario = usuarioDao.buscarPorCorreo(correo);

            if (usuario != null) {
                // Si la cuenta no está verificada, lo detenemos aquí
                if (usuario.getEstado().equals("unverificado")) {
                    enviarError(request, response, "Debes confirmar tu correo electrónico antes de iniciar sesión.");
                    return;
                }

                // Ahora sí, comprobamos el estado correcto
                if (usuario.getEstado().equals("verificado")) {
                    // 2. Buscar su contraseña hasheada en la tabla relacional
                    ContrasenaUsuario passModel = contrasenaDao.getById(usuario.getIdUsuario());

                    // 3. Comparar hashes
                    String hashIngresado = HashUtils.convertirSHA256(contraPlana);

                    // === INICIO DE DEBUG (Ver en la consola de IntelliJ) ===
                    if (passModel == null) {
                        System.out.println("¡ALERTA! No se encontró ninguna contraseña en la BD para el usuario: " + usuario.getIdUsuario());
                    } else {
                        System.out.println("Hash BD    : [" + passModel.getContrasenaHash() + "]");
                        System.out.println("Hash Login : [" + hashIngresado + "]");
                    }
                    // === FIN DE DEBUG ===

                    // 4. Validación blindada (ignora espacios y mayúsculas/minúsculas)
                    if (passModel != null && passModel.getContrasenaHash().trim().equalsIgnoreCase(hashIngresado.trim())) {
                        HttpSession session = request.getSession();
                        session.setAttribute("usuario", usuario);
                        response.sendRedirect("index.jsp");
                        return;
                    }
                }
            }
            enviarError(request, response, "Correo o contraseña incorrectos.");
        } catch (Exception e) {
            enviarError(request, response, "Error interno del servidor.");
        }
    }

    private String limpiar(String valor) { return valor == null ? "" : valor.trim(); }
    private void enviarError(HttpServletRequest request, HttpServletResponse response, String mensaje) throws ServletException, IOException {
        request.setAttribute("error", mensaje);
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}