package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.Articulo;
import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.model.dao.ArticuloDao;
import com.unimarket.unimarketintegradora.model.dao.UsuarioDao;
import com.unimarket.unimarketintegradora.utils.EmailSender;
import com.unimarket.unimarketintegradora.model.dao.ActividadDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "AdminActividadServlet", value = "/adminactividad")
public class AdminActividadServlet extends HttpServlet {

    private final UsuarioDao usuarioDao = new UsuarioDao();
    private final ArticuloDao articuloDao = new ArticuloDao();
    private final ActividadDao actividadDao = new ActividadDao();

    // ============================================================
    // GET - Cargar peticiones pendientes
    // ============================================================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Usuario> peticiones = usuarioDao.obtenerPeticiones();
        List<Articulo> articulosEspera = articuloDao.obtenerArticulosEnEspera();

        int totalPublicaciones = actividadDao.contarTotalPublicaciones();
        int totalUsuarios = actividadDao.contarTotalUsuarios();

        request.setAttribute("listaPeticiones", peticiones);
        request.setAttribute("listaArticulosEspera", articulosEspera);
        request.setAttribute("totalPublicaciones", totalPublicaciones); // NUEVO
        request.setAttribute("totalUsuarios", totalUsuarios);           // NUEVO

        request.getRequestDispatcher("/admin/actividad.jsp").forward(request, response);
    }

    // ============================================================
    // POST - Procesar acciones de usuarios y artículos
    // ============================================================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String tipo = request.getParameter("tipo");
        String accion = request.getParameter("accion");

        PrintWriter out = response.getWriter();

        // Validar datos generales
        if (tipo == null || accion == null) {
            out.print("{\"exito\":false,\"mensaje\":\"Solicitud inválida.\"}");
            return;
        }

        // ========================================================
        // USUARIO
        // ========================================================
        if ("usuario".equalsIgnoreCase(tipo)) {

            String matricula = request.getParameter("matricula");
            String correo = request.getParameter("correo");

            if (matricula == null || matricula.trim().isEmpty()) {
                out.print("{\"exito\":false,\"mensaje\":\"La matrícula es obligatoria.\"}");
                return;
            }

            // Aceptar usuario
            if ("aceptar".equalsIgnoreCase(accion)) {

                boolean verificado = usuarioDao.verificarUsuario(matricula);

                if (verificado) {
                    String msjBienvenida =
                            "<h2>¡Bienvenido a MUA!</h2>"
                                    + "<p>Un moderador ha verificado tu identidad. "
                                    + "Tu cuenta ha sido activada y ya puedes iniciar sesión.</p>"
                                    + "<p>Recuerda respetar a los demás y leer nuestros "
                                    + "términos y condiciones.</p>";

                    if (correo != null && !correo.trim().isEmpty()) {
                        EmailSender.sendMail(
                                correo,
                                "¡Cuenta Verificada en MUA!",
                                msjBienvenida
                        );
                    }

                    out.print("{\"exito\":true,\"mensaje\":\"Usuario verificado correctamente.\"}");
                } else {
                    out.print("{\"exito\":false,\"mensaje\":\"Hubo un problema al verificar al usuario.\"}");
                }

                return;
            }

            // Rechazar usuario
            if ("rechazar".equalsIgnoreCase(accion)) {

                boolean eliminado = usuarioDao.rechazarUsuario(matricula);

                if (eliminado) {
                    out.print("{\"exito\":true,\"mensaje\":\"Petición de usuario rechazada correctamente.\"}");
                } else {
                    out.print("{\"exito\":false,\"mensaje\":\"No se pudo rechazar la petición del usuario.\"}");
                }

                return;
            }

            out.print("{\"exito\":false,\"mensaje\":\"Acción de usuario no válida.\"}");
            return;
        }

        // ========================================================
        // ARTÍCULO
        // ========================================================
        if ("articulo".equalsIgnoreCase(tipo)) {

            String idArticuloTexto = request.getParameter("idArticulo");

            if (idArticuloTexto == null || idArticuloTexto.trim().isEmpty()) {
                out.print("{\"exito\":false,\"mensaje\":\"El ID del artículo es obligatorio.\"}");
                return;
            }

            int idArticulo;

            try {
                idArticulo = Integer.parseInt(idArticuloTexto);
            } catch (NumberFormatException e) {
                out.print("{\"exito\":false,\"mensaje\":\"El ID del artículo no es válido.\"}");
                return;
            }

            // Aceptar artículo
            if ("aceptar".equalsIgnoreCase(accion)) {

                boolean aprobado = articuloDao.verificarArticulo(idArticulo);

                if (aprobado) {
                    out.print(
                            "{\"exito\":true,\"mensaje\":\"Artículo aprobado correctamente. Ahora aparecerá en el catálogo.\"}"
                    );
                } else {
                    out.print(
                            "{\"exito\":false,\"mensaje\":\"No se pudo aprobar el artículo.\"}"
                    );
                }

                return;
            }

            // Rechazar artículo
            if ("rechazar".equalsIgnoreCase(accion)) {

                boolean rechazado = articuloDao.rechazarArticulo(idArticulo);

                if (rechazado) {
                    out.print(
                            "{\"exito\":true,\"mensaje\":\"Artículo rechazado correctamente.\"}"
                    );
                } else {
                    out.print(
                            "{\"exito\":false,\"mensaje\":\"No se pudo rechazar el artículo.\"}"
                    );
                }

                return;
            }

            out.print("{\"exito\":false,\"mensaje\":\"Acción de artículo no válida.\"}");
            return;
        }

        // ========================================================
        // TIPO NO VÁLIDO
        // ========================================================
        out.print("{\"exito\":false,\"mensaje\":\"Tipo de petición no válido.\"}");
    }
}