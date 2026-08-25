package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.model.dao.NotificacionDao;
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

/**
 * Servlet encargado de procesar y actualizar la foto de perfil de los usuarios.
 * Recibe la imagen en formato Base64, la decodifica, la almacena en el servidor
 * y actualiza la base de datos y la sesión del usuario.
 *
 * @author Dulce Yazmin Canseco Juárez
 * @date 2026-06-06
 */
@WebServlet(name = "ActualizarFotoPerfilServlet", value = "/actualizar-foto-perfil")
public class ActualizarFotoPerfilServlet extends HttpServlet {

    private final UsuarioDao usuarioDao = new UsuarioDao();
    private final NotificacionDao notificacionDao = new NotificacionDao();

    /**
     * Maneja las peticiones POST para actualizar la imagen de perfil.
     *
     * @param request  Objeto HttpServletRequest con los datos de la petición (incluyendo la foto en Base64).
     * @param response Objeto HttpServletResponse para enviar la respuesta en formato JSON.
     * @throws ServletException Si ocurre un error específico del servlet.
     * @throws IOException      Si ocurre un error de E/S.
     * @author Dulce Yazmin Canseco Juárez
     * @date 2026-06-06
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            out.print("{\"exito\": false, \"mensaje\": \"Debes iniciar sesión.\"}");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String fotoBase64 = request.getParameter("fotoBase64");

        if (fotoBase64 != null && fotoBase64.contains(",")) {
            try {
                String encabezado = fotoBase64.split(",")[0].toLowerCase();
                String extension = ".jpg";
                if (encabezado.contains("image/gif")) {
                    extension = ".gif";
                } else if (encabezado.contains("image/png")) {
                    extension = ".png";
                }

                String base64Data = fotoBase64.split(",")[1];
                byte[] imageBytes = Base64.getDecoder().decode(base64Data);

                String uploadPath = System.getenv("MUA_UPLOAD_PATH");
                if (uploadPath == null || uploadPath.trim().isEmpty()) {
                    uploadPath = System.getProperty("user.home") + File.separator + "mua_uploads";
                }
                uploadPath = uploadPath + File.separator + "perfiles";

                File carpeta = new File(uploadPath);
                if (!carpeta.exists()) carpeta.mkdirs();

                String fotoAnterior = usuario.getFotoPerfil();
                if (fotoAnterior != null && !fotoAnterior.trim().isEmpty() && !fotoAnterior.contains("default")) {
                    String nombreAnterior = fotoAnterior.replace("uploads/perfiles/", "");
                    File archivoViejo = new File(carpeta, nombreAnterior);
                    if (archivoViejo.exists()) {
                        archivoViejo.delete();
                    }
                }

                String nombreArchivo = "perfil_" + UUID.randomUUID() + extension;
                File archivoDestino = new File(carpeta, nombreArchivo);

                try (FileOutputStream fos = new FileOutputStream(archivoDestino)) {
                    fos.write(imageBytes);
                }

                String rutaRelativa = "uploads/perfiles/" + nombreArchivo;
                usuario.setFotoPerfil(rutaRelativa);

                boolean actualizado = usuarioDao.actualizarFotoPerfil(usuario.getMatricula(), rutaRelativa);

                if (actualizado) {
                    session.setAttribute("usuario", usuario);

                    String mensaje = "Has actualizado tu foto de perfil de manera exitosa.";
                    notificacionDao.crearNotificacion(usuario.getMatricula(), mensaje, "SISTEMA");

                    out.print("{\"exito\": true, \"mensaje\": \"Foto de perfil actualizada correctamente.\", \"nuevaRuta\": \"" + rutaRelativa + "\"}");
                    return;
                }
            } catch (Exception e) {
                System.out.println("Error procesando foto de perfil: " + e.getMessage());
            }
        }
        out.print("{\"exito\": false, \"mensaje\": \"Ocurrió un error al procesar o guardar la nueva imagen.\"}");
    }
}