package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.ContrasenaUsuario;
import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.model.dao.ContrasenaUsuarioDao;
import com.unimarket.unimarketintegradora.model.dao.UsuarioDao;
import com.unimarket.unimarketintegradora.utils.HashUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;

/**
 * Servlet encargado de gestionar el proceso de registro de nuevos usuarios en el sistema,
 * configurando codificación, validando campos y contraseñas, procesando y almacenando físicamente las credenciales universitarias (frente y reverso),
 * registrando el usuario y su contraseña cifrada en la base de datos con control de errores.
 *
 * @author Luis Fernando Rodriguez Rayo
 * @date 2026-06-06
 */
@WebServlet(name = "RegisterServlet", value = "/register")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB máximo por archivo
        maxRequestSize = 1024 * 1024 * 50    // 50MB máximo por petición
)

public class RegisterServlet extends HttpServlet {

    private final UsuarioDao usuarioDao = new UsuarioDao();
    private final ContrasenaUsuarioDao contrasenaDao = new ContrasenaUsuarioDao();

    /**
     * Maneja las peticiones POST para extraer los datos del formulario de registro, validar campos obligatorios y coincidencia de contraseñas,
     * almacenar las imágenes de las credenciales de identificación, registrar al usuario y su hash de contraseña en la base de datos,
     * y redireccionar al login o retornar al formulario de registro en caso de errores.
     *
     * @param request  Objeto HttpServletRequest con los parámetros de registro y las partes multiparte de los archivos de credenciales.
     * @param response Objeto HttpServletResponse para reenviar al JSP correspondiente según el resultado de la operación.
     * @throws ServletException Si ocurre un error específico del servlet.
     * @throws IOException      Si ocurre un error de E/S.
     * @author Luis Fernando Rodriguez Rayo
     * @date 2026-06-06
     */
    @Override
/**
 * Procesa una solicitud HTTP POST y ejecuta la operación solicitada.
 * @param request Parámetro de entrada de la operación.
 * @param response Parámetro de entrada de la operación.
 * @throws ServletException Excepción declarada por la operación.
 * @throws IOException Excepción declarada por la operación.
 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Configurar UTF-8 para acentos
        request.setCharacterEncoding("UTF-8");

        // 2. Extraer datos coincidiendo exactamente con los 'name' del JSP
        String nombre = request.getParameter("nombre");
        String apePat = request.getParameter("apellidoPaterno");
        String apeMat = request.getParameter("apellidoMaterno");
        String telefono = request.getParameter("telefono");
        String idDivisionStr = request.getParameter("idDivision");
        String matriculaStr = request.getParameter("matricula");
        String contra1 = request.getParameter("contra1");
        String contra2 = request.getParameter("contra2");

        // Lectura de los archivos de imagen recibidos desde el formulario
        Part credencialFrente = request.getPart("credencialFrente");
        Part credencialReverso = request.getPart("credencialReverso");

        // 3. Validaciones iniciales
        if (matriculaStr == null || matriculaStr.trim().isEmpty() || contra1 == null || contra1.trim().isEmpty()) {
            request.setAttribute("error", "Todos los campos son obligatorios.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        if (!contra1.equals(contra2)) {
            request.setAttribute("error", "Las contraseñas no coinciden.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        try {
            // Unir el nombre completo
            String nombreCompleto = nombre.trim() + " " + apePat.trim() + " " + apeMat.trim();

            // 4. Limpiar matrícula y generar correo
            String matriculaLimpia = matriculaStr.trim().toLowerCase();
            String correoGenerado = matriculaLimpia + "@utez.edu.mx";

            // ---------------------------------------------------------
            // LÓGICA DE GUARDADO FÍSICO DE LAS CREDENCIALES
            // ---------------------------------------------------------
            String uploadBasePath = System.getenv("MUA_UPLOAD_PATH");
            if (uploadBasePath == null || uploadBasePath.trim().isEmpty()) {
                uploadBasePath = System.getProperty("user.home") + File.separator + "mua_uploads";
            }

            // Creamos la carpeta "credenciales" si no existe
            File uploadDir = new File(uploadBasePath + File.separator + "credenciales");
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Generamos nombres únicos para los archivos usando la matrícula
            String nombreArchivoFrente = matriculaLimpia + "_frente_" + credencialFrente.getSubmittedFileName();
            String nombreArchivoReverso = matriculaLimpia + "_reverso_" + credencialReverso.getSubmittedFileName();

            // Guardamos físicamente los archivos en el servidor
            credencialFrente.write(uploadDir.getAbsolutePath() + File.separator + nombreArchivoFrente);
            credencialReverso.write(uploadDir.getAbsolutePath() + File.separator + nombreArchivoReverso);

            // Generamos las rutas relativas que se guardarán en Oracle (igual que tus artículos)
            String rutaFrente = "uploads/credenciales/" + nombreArchivoFrente;
            String rutaReverso = "uploads/credenciales/" + nombreArchivoReverso;
            // ---------------------------------------------------------

            // 5. Instanciar y popular el objeto Usuario (Alineado con el DAO)
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setMatricula(matriculaLimpia);
            nuevoUsuario.setCorreoInstitucional(correoGenerado);

            nuevoUsuario.setNombre(nombre.trim());
            nuevoUsuario.setApellidoPaterno(apePat.trim());
            nuevoUsuario.setApellidoMaterno(apeMat.trim());
            nuevoUsuario.setNumeroCelular(telefono);

            nuevoUsuario.setFotoPerfil("default.png");

            // ¡AHORA SÍ ENVIAMOS LAS RUTAS REALES AL DAO!
            nuevoUsuario.setFotoCredencialFrente(rutaFrente);
            nuevoUsuario.setFotoCredencialReverso(rutaReverso);

            if (idDivisionStr != null && !idDivisionStr.isEmpty()) {
                nuevoUsuario.setIdDivisionAcademicaFk(Integer.parseInt(idDivisionStr));
            }

            nuevoUsuario.setIdRolFk(2); // Rol normal

            // 6. Guardar los datos en Oracle
            boolean usuarioCreado = usuarioDao.create(nuevoUsuario);

            if (usuarioCreado) {
                // 7. Encriptar y guardar contraseña
                String hashPassword = HashUtils.convertirSHA256(contra1);
                ContrasenaUsuario passUsuario = new ContrasenaUsuario(matriculaLimpia, hashPassword);

                boolean passCreada = contrasenaDao.create(passUsuario);

                if (passCreada) {
                    request.setAttribute("mensaje", "¡Registro exitoso! Tu cuenta ha sido creada como: " + correoGenerado);
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                } else {
                    usuarioDao.delete(matriculaLimpia); // Rollback
                    request.setAttribute("error", "Ocurrió un problema al configurar la contraseña. Inténtalo de nuevo.");
                    request.getRequestDispatcher("registro.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("error", "No se pudo registrar la cuenta. ¿Esta matrícula ya está registrada?");
                request.getRequestDispatcher("registro.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error interno en el servidor: " + e.getMessage());
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }
    }
}