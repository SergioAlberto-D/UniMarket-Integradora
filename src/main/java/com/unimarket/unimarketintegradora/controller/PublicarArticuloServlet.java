package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.Articulo;
import com.unimarket.unimarketintegradora.model.ImagenArticulo;
import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.model.categoria;
import com.unimarket.unimarketintegradora.model.dao.ArticuloDao;
import com.unimarket.unimarketintegradora.model.dao.ImagenArticuloDao;
import com.unimarket.unimarketintegradora.model.dao.UsuarioDao;
import com.unimarket.unimarketintegradora.model.dao.NotificacionDao;
import com.unimarket.unimarketintegradora.model.dao.categoriaDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "PublicarArticuloServlet", value = "/publicar-articulo")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 10 * 1024 * 1024,
        maxRequestSize = 40 * 1024 * 1024
)
/**
 * Controlador web de MUA. Gestiona la interacción HTTP correspondiente a Publicar Articulo Servlet.
 *
 * @author Equipo UniMarket
 */
public class PublicarArticuloServlet extends HttpServlet {

    private final ArticuloDao articuloDao = new ArticuloDao();
    private final ImagenArticuloDao imagenDao = new ImagenArticuloDao();
    private final UsuarioDao usuarioDao = new UsuarioDao();
    private final NotificacionDao notificacionDao = new NotificacionDao();
    private final categoriaDao categoriaDao = new categoriaDao();

    @Override
/**
 * Procesa una solicitud HTTP GET y prepara la respuesta correspondiente.
 * @param request Parámetro de entrada de la operación.
 * @param response Parámetro de entrada de la operación.
 * @throws ServletException Excepción declarada por la operación.
 * @throws IOException Excepción declarada por la operación.
 */
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        List<categoria> categorias = categoriaDao.getAll();

        request.setAttribute("categorias", categorias);

        request.getRequestDispatcher("publicar-articulo.jsp")
                .forward(request, response);
    }

    @Override
/**
 * Procesa una solicitud HTTP POST y ejecuta la operación solicitada.
 * @param request Parámetro de entrada de la operación.
 * @param response Parámetro de entrada de la operación.
 * @throws ServletException Excepción declarada por la operación.
 * @throws IOException Excepción declarada por la operación.
 */
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
                session.getAttribute("usuario") == null) {

            response.sendRedirect("login.jsp");
            return;
        }

        Usuario usuario =
                (Usuario) session.getAttribute("usuario");

        String titulo =
                limpiar(request.getParameter("titulo"));

        String descripcion =
                limpiar(request.getParameter("descripcion"));

        String precioTexto =
                limpiar(request.getParameter("precio"));

        int idCategoria = 1;

        try {

            idCategoria =
                    Integer.parseInt(
                            request.getParameter("idCategoria")
                    );

        } catch (Exception ignored) {
        }

        /*
         * VALIDACIÓN
         */
        if (titulo.isEmpty() ||
                precioTexto.isEmpty() ||
                descripcion.isEmpty()) {

            enviarError(
                    request,
                    response,
                    "Completa todos los campos del artículo."
            );

            return;
        }

        /*
         * VALIDACIÓN DEL PRECIO
         */
        BigDecimal precio;

        try {

            precio = new BigDecimal(precioTexto);

            if (precio.compareTo(BigDecimal.ZERO) < 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {

            enviarError(
                    request,
                    response,
                    "Ingresa un precio válido."
            );

            return;
        }

        /*
         * CREAR ARTÍCULO
         *
         * IMPORTANTE:
         *
         * ArticuloDao.create()
         * lo registra automáticamente como:
         *
         * estado = espera
         *
         * NO se activa inmediatamente.
         */
        Articulo articulo =
                new Articulo(
                        titulo,
                        precio,
                        idCategoria,
                        descripcion,
                        usuario.getMatricula()
                );

        boolean publicado =
                articuloDao.create(articulo);

        if (publicado) {

            /*
             * Obtener el ID generado
             */
            int idArticuloGenerado =
                    articuloDao.obtenerUltimoIdPorUsuario(
                            usuario.getMatricula()
                    );

            /*
             * Guardar imágenes
             */
            List<String> rutas =
                    guardarImagenes(request);

            for (String ruta : rutas) {

                imagenDao.create(
                        new ImagenArticulo(
                                idArticuloGenerado,
                                ruta
                        )
                );
            }

            /*
             * El usuario sigue pudiendo ser vendedor.
             * Esta parte se conserva del proyecto original.
             */
            if (usuario.getIdRolFk() != 3) {

                boolean ascendido =
                        usuarioDao.ascenderAVendedor(
                                usuario.getMatricula()
                        );

                if (ascendido) {

                    usuario.setIdRolFk(3);

                    session.setAttribute(
                            "usuario",
                            usuario
                    );
                }
            }

            /*
             * NOTIFICACIÓN
             */
            String mensaje =
                    "Tu artículo '" +
                            titulo +
                            "' ($" +
                            precio +
                            " MXN) fue registrado y quedó en espera de verificación por un administrador.";

            notificacionDao.crearNotificacion(
                    usuario.getMatricula(),
                    mensaje,
                    "SISTEMA"
            );

            /*
             * Regresamos a Inicio.
             *
             * El artículo NO aparecerá todavía
             * porque está en estado "espera".
             */
            response.sendRedirect(
                    request.getContextPath() +
                            "/inicio?espera=true"
            );

        } else {

            enviarError(
                    request,
                    response,
                    "No se pudo registrar el artículo."
            );
        }
    }

    /*
     * ============================================================
     * GUARDAR IMÁGENES
     * ============================================================
     */
    private List<String> guardarImagenes(
            HttpServletRequest request)
            throws IOException, ServletException {

        List<String> rutas =
                new ArrayList<>();

        String uploadPath =
                System.getenv("MUA_UPLOAD_PATH");

        if (uploadPath == null ||
                uploadPath.trim().isEmpty()) {

            uploadPath =
                    System.getProperty("user.home") +
                            File.separator +
                            "mua_uploads";
        }

        uploadPath =
                uploadPath +
                        File.separator +
                        "articulos";

        File carpeta =
                new File(uploadPath);

        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        for (Part part : request.getParts()) {

            if (!"imagenes".equals(part.getName())) {
                continue;
            }

            if (part.getSize() <= 0) {
                continue;
            }

            if (rutas.size() >= 3) {
                break;
            }

            String nombreOriginal =
                    obtenerNombreArchivo(part);

            if (nombreOriginal.isEmpty()) {
                continue;
            }

            String extension =
                    obtenerExtension(nombreOriginal);

            if (!extension.equals(".jpg") &&
                    !extension.equals(".jpeg") &&
                    !extension.equals(".png")) {

                continue;
            }

            String nombreFinal =
                    UUID.randomUUID() +
                            extension;

            String rutaArchivo =
                    uploadPath +
                            File.separator +
                            nombreFinal;

            part.write(rutaArchivo);

            rutas.add(
                    "uploads/articulos/" +
                            nombreFinal
            );
        }

        return rutas;
    }

    /*
     * ============================================================
     * OBTENER NOMBRE DEL ARCHIVO
     * ============================================================
     */
    private String obtenerNombreArchivo(Part part) {

        String contentDisposition =
                part.getHeader("content-disposition");

        if (contentDisposition == null) {
            return "";
        }

        for (String contenido :
                contentDisposition.split(";")) {

            contenido =
                    contenido.trim();

            if (contenido.startsWith("filename")) {

                return contenido
                        .substring(
                                contenido.indexOf("=") + 1
                        )
                        .trim()
                        .replace("\"", "");
            }
        }

        return "";
    }

    /*
     * ============================================================
     * OBTENER EXTENSIÓN
     * ============================================================
     */
    private String obtenerExtension(
            String nombreArchivo) {

        int punto =
                nombreArchivo.lastIndexOf(".");

        if (punto == -1) {
            return "";
        }

        return nombreArchivo
                .substring(punto)
                .toLowerCase();
    }

    /*
     * ============================================================
     * LIMPIAR TEXTO
     * ============================================================
     */
    private String limpiar(String valor) {

        return valor == null
                ? ""
                : valor.trim();
    }

    /*
     * ============================================================
     * ERROR
     * ============================================================
     */
    private void enviarError(
            HttpServletRequest request,
            HttpServletResponse response,
            String mensaje)
            throws ServletException, IOException {

        request.setAttribute(
                "error",
                mensaje
        );

        request.setAttribute(
                "categorias",
                categoriaDao.getAll()
        );

        request.getRequestDispatcher(
                "publicar-articulo.jsp"
        ).forward(
                request,
                response
        );
    }
}