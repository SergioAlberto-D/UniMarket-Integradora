package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.ImagenArticulo;
import com.unimarket.unimarketintegradora.model.dao.ImagenArticuloDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "EliminarImagenArticuloServlet", value = "/eliminar-imagen-articulo")
/**
 * Controlador web de MUA. Gestiona la interacción HTTP correspondiente a Eliminar Imagen Articulo Servlet.
 *
 * @author Equipo UniMarket
 */
public class EliminarImagenArticuloServlet extends HttpServlet {
    private final ImagenArticuloDao imagenDao = new ImagenArticuloDao();

    @Override
/**
 * Procesa una solicitud HTTP POST y ejecuta la operación solicitada.
 * @param request Parámetro de entrada de la operación.
 * @param response Parámetro de entrada de la operación.
 * @throws ServletException Excepción declarada por la operación.
 * @throws IOException Excepción declarada por la operación.
 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String idImgStr = request.getParameter("id");
        if (idImgStr == null || idImgStr.isEmpty()) {
            out.print("{\"exito\": false, \"mensaje\": \"ID de imagen no válido.\"}");
            return;
        }

        try {
            int idImagen = Integer.parseInt(idImgStr);

            // 1. Buscamos la imagen en la base de datos para saber su nombre de archivo
            ImagenArticulo img = imagenDao.getById(idImagen);
            if (img != null) {
                String uploadBasePath = System.getenv("MUA_UPLOAD_PATH");
                if (uploadBasePath == null || uploadBasePath.trim().isEmpty()) {
                    uploadBasePath = System.getProperty("user.home") + File.separator + "mua_uploads";
                }

                // Extraemos el nombre de archivo (ej: "foto1.jpg") y lo borramos del disco en Linux
                String nombreArchivo = new File(img.getUrlImagen()).getName();
                File archivoFisico = new File(uploadBasePath + File.separator + "articulos", nombreArchivo);
                if (archivoFisico.exists()) {
                    archivoFisico.delete();
                }
            }

            // 2. Eliminamos el registro de la tabla IMAGEN_ARTICULO en Oracle
            boolean eliminada = imagenDao.delete(idImagen);

            if (eliminada) {
                out.print("{\"exito\": true, \"mensaje\": \"Imagen eliminada.\"}");
            } else {
                out.print("{\"exito\": false, \"mensaje\": \"No se pudo eliminar de la base de datos.\"}");
            }
        } catch (NumberFormatException e) {
            out.print("{\"exito\": false, \"mensaje\": \"Formato de ID incorrecto.\"}");
        }
    }
}