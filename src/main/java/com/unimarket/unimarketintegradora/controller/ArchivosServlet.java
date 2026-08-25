package com.unimarket.unimarketintegradora.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Servlet encargado de servir y mostrar archivos subidos por los usuarios (como imágenes de perfil o productos) desde el disco del servidor.
 *
 * @author Dulce Yazmin Canseco Juárez
 * @date 2026-06-06
 */
// Se intercepta cualquier petición que empiece con /uploads/
@WebServlet(name = "ArchivosServlet", urlPatterns = {"/uploads/*"})

public class ArchivosServlet extends HttpServlet {

    /**
     * Maneja las peticiones GET para buscar y transmitir el archivo solicitado al cliente.
     *
     * @param request  Objeto HttpServletRequest con la ruta solicitada del archivo.
     * @param response Objeto HttpServletResponse para enviar el contenido binario y tipo MIME del archivo.
     * @throws ServletException Si ocurre un error específico del servlet.
     * @throws IOException      Si ocurre un error de E/S.
     * @author Dulce Yazmin Canseco Juárez
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

        // 1. Saber qué archivo pide el JSP (ej. /perfiles/perfil_1.jpg)
        String rutaSolicitada = request.getPathInfo();

        if (rutaSolicitada == null || rutaSolicitada.equals("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 2. Definir la misma ruta base que usamos en el Servlet de subida
        String uploadBasePath = System.getenv("MUA_UPLOAD_PATH");
        if (uploadBasePath == null || uploadBasePath.trim().isEmpty()) {
            uploadBasePath = System.getProperty("user.home") + File.separator + "mua_uploads";
        }

        // 3. Unir la ruta base con el archivo solicitado
        File file = new File(uploadBasePath, rutaSolicitada);

        // 4. Si el archivo no existe en el disco duro, retornar error 404
        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 5. Configurar la respuesta HTTP para enviar una imagen
        String mimeType = getServletContext().getMimeType(file.getName());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        response.setContentType(mimeType);
        response.setContentLength((int) file.length());

        // 6. Leer el archivo del disco y escribirlo en la respuesta web
        try (FileInputStream in = new FileInputStream(file);
             OutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
}