package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.Articulo;
import com.unimarket.unimarketintegradora.model.ImagenArticulo;
import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.model.dao.ArticuloDao;
import com.unimarket.unimarketintegradora.model.dao.ImagenArticuloDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "EliminarArticuloServlet", value = "/eliminar-articulo")
public class EliminarArticuloServlet extends HttpServlet {
    private final ArticuloDao articuloDao = new ArticuloDao();
    private final ImagenArticuloDao imagenDao = new ImagenArticuloDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        Usuario usuarioLogueado = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        if (usuarioLogueado == null) {
            out.print("{\"exito\": false, \"mensaje\": \"Debes iniciar sesión para eliminar artículos.\"}");
            return;
        }

        String idArticuloStr = request.getParameter("id");
        if (idArticuloStr == null || idArticuloStr.isEmpty()) {
            out.print("{\"exito\": false, \"mensaje\": \"ID de artículo no válido.\"}");
            return;
        }

        try {
            int idArticulo = Integer.parseInt(idArticuloStr);
            Articulo articulo = articuloDao.getById(String.valueOf(idArticulo));

            // Validar que el artículo exista y que pertenezca al usuario logueado
            if (articulo == null || !articulo.getIdUsuarioFk().equals(usuarioLogueado.getIdUsuario())) {
                out.print("{\"exito\": false, \"mensaje\": \"No tienes permiso para eliminar este artículo.\"}");
                return;
            }

            // 1. Obtener imágenes y borrar archivos físicos del disco (/home/sheuko/mua_uploads/articulos/)
            List<ImagenArticulo> imagenes = imagenDao.obtenerPorArticulo(idArticulo);
            String uploadBasePath = System.getenv("MUA_UPLOAD_PATH");
            if (uploadBasePath == null || uploadBasePath.trim().isEmpty()) {
                uploadBasePath = System.getProperty("user.home") + File.separator + "mua_uploads";
            }

            for (ImagenArticulo img : imagenes) {
                // img.getUrlImagen() viene como "uploads/articulos/nombre.jpg", extraemos solo el nombre de archivo
                String nombreArchivo = new File(img.getUrlImagen()).getName();
                File archivoFisico = new File(uploadBasePath + File.separator + "articulos", nombreArchivo);
                if (archivoFisico.exists()) {
                    archivoFisico.delete();
                }
            }

            // 2. Eliminar imágenes de la base de datos
            imagenDao.eliminarPorArticulo(idArticulo);

            // 3. Eliminar el artículo de la base de datos
            boolean eliminado = articuloDao.delete(String.valueOf(idArticulo));

            if (eliminado) {
                out.print("{\"exito\": true, \"mensaje\": \"Artículo e imágenes eliminados correctamente.\"}");
            } else {
                out.print("{\"exito\": false, \"mensaje\": \"No se pudo eliminar el artículo en la base de datos.\"}");
            }

        } catch (NumberFormatException e) {
            out.print("{\"exito\": false, \"mensaje\": \"Error en el formato del ID.\"}");
        }
    }
}