package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.model.dao.UsuarioDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@WebServlet(name = "SubirFotoPerfilServlet", value = "/SubirFotoPerfilServlet")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 5,       // 5 MB
        maxRequestSize = 1024 * 1024 * 10    // 10 MB
)
public class SubirFotoPerfilServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Part filePart = request.getPart("fotoPerfil");

        if (filePart != null && filePart.getSize() > 0) {

            String fileNameOriginal = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String extension = "";
            int i = fileNameOriginal.lastIndexOf('.');
            if (i > 0) {
                extension = fileNameOriginal.substring(i);
            }

            String nuevoNombre = "perfil_" + usuario.getIdUsuario() + extension;

            // LÓGICA DE RUTA EXTERNA:
            // 1. Intenta leer una variable de entorno (Ideal para servidores de producción)
            String uploadPath = System.getenv("MUA_UPLOAD_PATH");

            // 2. Si no hay variable, usa el "home" del sistema operativo (Ideal para tu entorno local)
            if (uploadPath == null || uploadPath.trim().isEmpty()) {
                uploadPath = System.getProperty("user.home") + File.separator + "mua_uploads";
            }

            // Agregamos la subcarpeta específica para perfiles
            uploadPath = uploadPath + File.separator + "perfiles";

            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // --- NUEVO: ELIMINAR LA FOTO ANTERIOR ---
            String rutaFotoAnterior = usuario.getFotoPerfil();

            // Verificamos que el usuario ya tuviera una foto y que no sea un valor nulo
            if (rutaFotoAnterior != null && !rutaFotoAnterior.trim().isEmpty()) {
                // Extraemos solo el nombre del archivo viejo (ej. "perfil_1.jpg")
                String nombreAnterior = Paths.get(rutaFotoAnterior).getFileName().toString();
                File archivoViejo = new File(uploadPath + File.separator + nombreAnterior);

                // Si el archivo viejo existe en el disco, lo borramos
                if (archivoViejo.exists()) {
                    archivoViejo.delete();
                }
            }
            // ----------------------------------------

            // Guardar físicamente en el disco externo
            String filePath = uploadPath + File.separator + nuevoNombre;
            filePart.write(filePath);

            // Guardar la ruta relativa en la base de datos (se mantiene igual para no romper los JSP)
            String rutaRelativa = "uploads/perfiles/" + nuevoNombre;

            UsuarioDao usuarioDao = new UsuarioDao();
            boolean actualizado = usuarioDao.actualizarFotoPerfil(usuario.getIdUsuario(), rutaRelativa);

            if (actualizado) {
                usuario.setFotoPerfil(rutaRelativa);
                session.setAttribute("usuario", usuario);
            }
        }

        response.sendRedirect(request.getContextPath() + "/perfil");
    }
}