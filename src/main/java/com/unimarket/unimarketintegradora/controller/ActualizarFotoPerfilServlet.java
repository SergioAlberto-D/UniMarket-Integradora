package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.model.dao.UsuarioDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.UUID;

@WebServlet(name = "ActualizarFotoPerfilServlet", value = "/actualizar-foto-perfil")
public class ActualizarFotoPerfilServlet extends HttpServlet {
    private final UsuarioDao usuarioDao = new UsuarioDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            out.print("{\"exito\": false}");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String fotoBase64 = request.getParameter("fotoBase64");

        if (fotoBase64 != null && fotoBase64.contains(",")) {
            try {
                // 1. Detectar extensión según el encabezado Base64 (admite .gif, .png, .jpg)
                String encabezado = fotoBase64.split(",")[0].toLowerCase();
                String extension = ".jpg"; // por defecto
                if (encabezado.contains("image/gif")) {
                    extension = ".gif";
                } else if (encabezado.contains("image/png")) {
                    extension = ".png";
                }

                // 2. Limpiar el prefijo data:image/...;base64,
                String base64Data = fotoBase64.split(",")[1];
                byte[] imageBytes = Base64.getDecoder().decode(base64Data);

                // 3. Determinar la carpeta de subidas multiplataforma
                String uploadPath = System.getenv("MUA_UPLOAD_PATH");
                if (uploadPath == null || uploadPath.trim().isEmpty()) {
                    uploadPath = System.getProperty("user.home") + File.separator + "mua_uploads";
                }
                uploadPath = uploadPath + File.separator + "perfiles";

                File carpeta = new File(uploadPath);
                if (!carpeta.exists()) carpeta.mkdirs();

                // =========================================================
                // 4. AQUÍ VA EL BORRADO DE LA FOTO ANTERIOR EN EL DISCO
                // =========================================================
                String fotoAnterior = usuario.getFotoPerfil();
                if (fotoAnterior != null && !fotoAnterior.trim().isEmpty() && !fotoAnterior.contains("default")) {
                    String nombreAnterior = fotoAnterior.replace("uploads/perfiles/", "");
                    File archivoViejo = new File(carpeta, nombreAnterior);
                    if (archivoViejo.exists()) {
                        archivoViejo.delete(); // Elimina el archivo anterior (sea .jpg, .png o .gif)
                    }
                }
                // =========================================================

                // 5. Generar un nombre único con su extensión real y guardar
                String nombreArchivo = "perfil_" + UUID.randomUUID() + extension;
                File archivoDestino = new File(carpeta, nombreArchivo);

                try (FileOutputStream fos = new FileOutputStream(archivoDestino)) {
                    fos.write(imageBytes);
                }

                // 6. Actualizar la ruta relativa en el objeto y en Oracle
                String rutaRelativa = "uploads/perfiles/" + nombreArchivo;
                usuario.setFotoPerfil(rutaRelativa);

                boolean actualizado = usuarioDao.update(usuario);

                if (actualizado) {
                    session.setAttribute("usuario", usuario); // Refresca sesión
                    out.print("{\"exito\": true, \"nuevaRuta\": \"" + rutaRelativa + "\"}");
                    return;
                }
            } catch (Exception e) {
                System.out.println("Error procesando foto de perfil: " + e.getMessage());
            }
        }
        out.print("{\"exito\": false}");
    }
}