package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.Articulo;
import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.model.dao.ArticuloDao;
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
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 20 * 1024 * 1024
)
public class PublicarArticuloServlet extends HttpServlet {

    private final ArticuloDao articuloDao = new ArticuloDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        String titulo = limpiar(request.getParameter("titulo"));
        String precioTexto = limpiar(request.getParameter("precio"));
        String categoria = limpiar(request.getParameter("categoria"));
        String descripcion = limpiar(request.getParameter("descripcion"));
        String lugarEncuentro = limpiar(request.getParameter("lugarEncuentro"));

        if (titulo.isEmpty() || precioTexto.isEmpty() || categoria.isEmpty() || descripcion.isEmpty() || lugarEncuentro.isEmpty()) {
            enviarError(request, response, "Completa todos los campos del artículo.");
            return;
        }

        BigDecimal precio;

        try {
            precio = new BigDecimal(precioTexto);

            if (precio.compareTo(BigDecimal.ZERO) < 0) {
                enviarError(request, response, "El precio no puede ser negativo.");
                return;
            }

        } catch (NumberFormatException e) {
            enviarError(request, response, "Ingresa un precio válido.");
            return;
        }

        List<String> rutasImagenes = guardarImagenes(request);

        Articulo articulo = new Articulo(
                usuario.getIdUsuario(),
                titulo,
                descripcion,
                precio,
                categoria,
                usuario.getCarrera(),
                lugarEncuentro
        );

        boolean publicado = articuloDao.crearConImagenes(articulo, rutasImagenes);

        if (publicado) {
            response.sendRedirect("publicar-articulo.jsp?exito=true");
        } else {
            enviarError(request, response, "No se pudo publicar el artículo.");
        }
    }

    private List<String> guardarImagenes(HttpServletRequest request) throws IOException, ServletException {
        List<String> rutas = new ArrayList<>();

        String rutaReal = getServletContext().getRealPath("/uploads/articulos");

        if (rutaReal == null) {
            return rutas;
        }

        File carpeta = new File(rutaReal);

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

            String nombreOriginal = obtenerNombreArchivo(part);

            if (nombreOriginal.isEmpty()) {
                continue;
            }

            String extension = obtenerExtension(nombreOriginal);

            if (!extension.equals(".jpg") && !extension.equals(".jpeg") && !extension.equals(".png")) {
                continue;
            }

            String nombreFinal = UUID.randomUUID() + extension;
            String rutaArchivo = rutaReal + File.separator + nombreFinal;

            part.write(rutaArchivo);

            rutas.add("uploads/articulos/" + nombreFinal);
        }

        return rutas;
    }

    private String obtenerNombreArchivo(Part part) {
        String contentDisposition = part.getHeader("content-disposition");

        if (contentDisposition == null) {
            return "";
        }

        for (String contenido : contentDisposition.split(";")) {
            contenido = contenido.trim();

            if (contenido.startsWith("filename")) {
                return contenido.substring(contenido.indexOf("=") + 1)
                        .trim()
                        .replace("\"", "");
            }
        }

        return "";
    }

    private String obtenerExtension(String nombreArchivo) {
        int punto = nombreArchivo.lastIndexOf(".");

        if (punto == -1) {
            return "";
        }

        return nombreArchivo.substring(punto).toLowerCase();
    }

    private String limpiar(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private void enviarError(HttpServletRequest request, HttpServletResponse response, String mensaje) throws ServletException, IOException {
        request.setAttribute("error", mensaje);
        request.getRequestDispatcher("publicar-articulo.jsp").forward(request, response);
    }
}
