package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.Articulo;
import com.unimarket.unimarketintegradora.model.ImagenArticulo;
import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.model.dao.ArticuloDao;
import com.unimarket.unimarketintegradora.model.dao.ImagenArticuloDao;
import com.unimarket.unimarketintegradora.model.dao.NotificacionDao;
import com.unimarket.unimarketintegradora.model.dao.UsuarioDao;

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
import com.unimarket.unimarketintegradora.model.Categoria;
import com.unimarket.unimarketintegradora.model.dao.CategoriaDao;

@WebServlet(name = "PublicarArticuloServlet", value = "/publicar-articulo")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024, maxRequestSize = 20 * 1024 * 1024)
public class PublicarArticuloServlet extends HttpServlet {

    private final ArticuloDao articuloDao = new ArticuloDao();
    private final ImagenArticuloDao imagenDao = new ImagenArticuloDao();
    private final UsuarioDao usuarioDao = new UsuarioDao();
    private final NotificacionDao notificacionDao = new NotificacionDao();
    private final CategoriaDao categoriaDao = new CategoriaDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Categoria> categorias = categoriaDao.getAll();
        request.setAttribute("categorias", categorias);
        request.getRequestDispatcher("publicar-articulo.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        String titulo = limpiar(request.getParameter("titulo"));
        String descripcion = limpiar(request.getParameter("descripcion"));
        String precioTexto = limpiar(request.getParameter("precio"));

        int idCategoria = 1;
        try { idCategoria = Integer.parseInt(request.getParameter("idCategoria")); } catch(Exception ignored){}

        if (titulo.isEmpty() || precioTexto.isEmpty() || descripcion.isEmpty()) {
            enviarError(request, response, "Completa todos los campos del artículo.");
            return;
        }

        BigDecimal precio;
        try {
            precio = new BigDecimal(precioTexto);
            if (precio.compareTo(BigDecimal.ZERO) < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            enviarError(request, response, "Ingresa un precio válido.");
            return;
        }

        Articulo articulo = new Articulo(titulo, precio, idCategoria, descripcion, usuario.getIdUsuario());
        boolean publicado = articuloDao.create(articulo);

        if (publicado) {
            int idArticuloGenerado = articuloDao.obtenerUltimoIdPorUsuario(usuario.getIdUsuario());

            List<String> rutas = guardarImagenes(request);
            for (String ruta : rutas) {
                imagenDao.create(new ImagenArticulo(idArticuloGenerado, ruta));
            }

            if (usuario.getIdRolFk() != 3) {
                usuario.setIdRolFk(3);
                usuarioDao.update(usuario);
                session.setAttribute("usuario", usuario);
            }

            // Notificar al propio vendedor del éxito de su publicación
            String mensaje = "Tu artículo '" + titulo + "' ($" + precio + " MXN) fue publicado correctamente en el catálogo.";
            notificacionDao.crearNotificacion(usuario.getIdUsuario(), mensaje, "SISTEMA");

            response.sendRedirect(request.getContextPath() + "/inicio?exito=true");

        } else {
            enviarError(request, response, "No se pudo publicar el artículo.");
        }
    }

    private List<String> guardarImagenes(HttpServletRequest request) throws IOException, ServletException {
        List<String> rutas = new ArrayList<>();
        String uploadPath = System.getenv("MUA_UPLOAD_PATH");
        if (uploadPath == null || uploadPath.trim().isEmpty()) {
            uploadPath = System.getProperty("user.home") + File.separator + "mua_uploads";
        }
        uploadPath = uploadPath + File.separator + "articulos";

        File carpeta = new File(uploadPath);
        if (!carpeta.exists()) carpeta.mkdirs();

        for (Part part : request.getParts()) {
            if (!"imagenes".equals(part.getName())) continue;
            if (part.getSize() <= 0) continue;
            if (rutas.size() >= 3) break;

            String nombreOriginal = obtenerNombreArchivo(part);
            if (nombreOriginal.isEmpty()) continue;

            String extension = obtenerExtension(nombreOriginal);
            if (!extension.equals(".jpg") && !extension.equals(".jpeg") && !extension.equals(".png")) continue;

            String nombreFinal = UUID.randomUUID() + extension;
            String rutaArchivo = uploadPath + File.separator + nombreFinal;
            part.write(rutaArchivo);
            rutas.add("uploads/articulos/" + nombreFinal);
        }
        return rutas;
    }

    private String obtenerNombreArchivo(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        if (contentDisposition == null) return "";
        for (String contenido : contentDisposition.split(";")) {
            contenido = contenido.trim();
            if (contenido.startsWith("filename")) {
                return contenido.substring(contenido.indexOf("=") + 1).trim().replace("\"", "");
            }
        }
        return "";
    }

    private String obtenerExtension(String nombreArchivo) {
        int punto = nombreArchivo.lastIndexOf(".");
        if (punto == -1) return "";
        return nombreArchivo.substring(punto).toLowerCase();
    }

    private String limpiar(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private void enviarError(HttpServletRequest request, HttpServletResponse response, String mensaje) throws ServletException, IOException {
        request.setAttribute("error", mensaje);
        request.setAttribute("categorias", categoriaDao.getAll()); // <- Evita que el select aparezca vacío al haber un error
        request.getRequestDispatcher("publicar-articulo.jsp").forward(request, response);
    }
}