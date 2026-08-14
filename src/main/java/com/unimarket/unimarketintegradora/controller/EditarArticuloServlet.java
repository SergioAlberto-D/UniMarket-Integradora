package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.Articulo;
import com.unimarket.unimarketintegradora.model.ImagenArticulo;
import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.model.categoria;
import com.unimarket.unimarketintegradora.model.dao.ArticuloDao;
import com.unimarket.unimarketintegradora.model.dao.ImagenArticuloDao;

import com.unimarket.unimarketintegradora.model.dao.categoriaDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "EditarArticuloServlet", value = "/editar-articulo")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 30
)
public class EditarArticuloServlet extends HttpServlet {
    private final ArticuloDao articuloDao = new ArticuloDao();
    private final ImagenArticuloDao imagenDao = new ImagenArticuloDao();
    private final categoriaDao categoriaDao = new categoriaDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect("mis-articulos");
            return;
        }

        Articulo articulo = articuloDao.getById(idParam);
        if (articulo == null) {
            response.sendRedirect("mis-articulos");
            return;
        }

        // 1. Obtener las imágenes actuales del artículo desde la base de datos
        List<ImagenArticulo> imagenes = imagenDao.obtenerPorArticulo(articulo.getIdArticulo());

        // --- NUEVO: Obtener todas las categorías desde la base de datos ---
        List<categoria> categorias = categoriaDao.getAll();

        // 2. Mandarlas al request
        request.setAttribute("articulo", articulo);
        request.setAttribute("imagenes", imagenes);
        // --- NUEVO: Mandar las categorías al JSP ---
        request.setAttribute("categorias", categorias);

        request.getRequestDispatcher("editar-articulo.jsp").forward(request, response);
    }

    // POST: Guardar cambios y reemplazar imágenes si se subieron nuevas
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String idArticuloStr = request.getParameter("idArticulo");
        String titulo = request.getParameter("titulo");
        String precioStr = request.getParameter("precio");
        String catStr = request.getParameter("idCategoria");
        String descripcion = request.getParameter("descripcion");

        try {
            int idArticulo = Integer.parseInt(idArticuloStr);
            Articulo articulo = articuloDao.getById(idArticuloStr);

            if (articulo == null || !articulo.getIdUsuarioFk().equals(usuario.getIdUsuario())) {
                response.sendRedirect("mis-articulos");
                return;
            }

            if (titulo != null && !titulo.trim().isEmpty()) {
                articulo.setNombre(titulo.trim());
            }

            if (precioStr != null && !precioStr.trim().isEmpty()) {
                articulo.setPrecio(new BigDecimal(precioStr.trim()));
            }

            if (catStr != null && !catStr.trim().isEmpty()) {
                articulo.setIdCategoriaFk(Integer.parseInt(catStr));
            }

            if (descripcion != null && !descripcion.trim().isEmpty()) {
                articulo.setDescripcion(descripcion.trim());
            }

            articuloDao.update(articulo);

            Collection<Part> parts = request.getParts();
            boolean hayNuevasImagenes = false;
            for (Part part : parts) {
                if ("imagenes".equals(part.getName()) && part.getSize() > 0 && part.getSubmittedFileName() != null && !part.getSubmittedFileName().isEmpty()) {
                    hayNuevasImagenes = true;
                    break;
                }
            }

            if (hayNuevasImagenes) {
                String uploadBasePath = System.getenv("MUA_UPLOAD_PATH");
                if (uploadBasePath == null || uploadBasePath.trim().isEmpty()) {
                    uploadBasePath = System.getProperty("user.home") + File.separator + "mua_uploads";
                }
                File uploadDir = new File(uploadBasePath, "articulos");
                if (!uploadDir.exists()) uploadDir.mkdirs();

                // Borrar archivos físicos viejos
                List<ImagenArticulo> imagenesViejas = imagenDao.obtenerPorArticulo(idArticulo);
                for (ImagenArticulo img : imagenesViejas) {
                    String nomArchivo = new File(img.getUrlImagen()).getName();
                    File archivoViejo = new File(uploadDir, nomArchivo);
                    if (archivoViejo.exists()) archivoViejo.delete();
                }
                imagenDao.eliminarPorArticulo(idArticulo);

                // Guardar imágenes nuevas
                int conteo = 0;
                for (Part part : parts) {
                    if ("imagenes".equals(part.getName()) && part.getSize() > 0 && conteo < 3) {
                        String nombreOriginal = part.getSubmittedFileName();
                        String extension = "";
                        int indexPunto = nombreOriginal.lastIndexOf(".");
                        if (indexPunto > 0) extension = nombreOriginal.substring(indexPunto);

                        String nuevoNombre = UUID.randomUUID().toString() + extension;
                        File archivoDestino = new File(uploadDir, nuevoNombre);
                        part.write(archivoDestino.getAbsolutePath());

                        ImagenArticulo nuevaImg = new ImagenArticulo();
                        nuevaImg.setIdArticuloFk(idArticulo);
                        nuevaImg.setUrlImagen("uploads/articulos/" + nuevoNombre);
                        imagenDao.create(nuevaImg);
                        conteo++;
                    }
                }
            }

            response.sendRedirect("mis-articulos?exito=true");

        } catch (Exception e) {
            request.setAttribute("error", "Error al actualizar el artículo: " + e.getMessage());
            doGet(request, response);
        }
    }
}