package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.DivisionAcademica;
import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.model.dao.DivisionAcademicaDao;
import com.unimarket.unimarketintegradora.model.dao.UsuarioDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet encargado de administrar los usuarios y sus divisiones académicas en el panel de control.
 *
 * @author Dulce Yazmin Canseco Juárez
 * @date 2026-06-06
 */
@WebServlet(name = "AdminUsuarioServlet", value = "/adminusuarios")
/**
 * Controlador web de MUA. Gestiona la interacción HTTP correspondiente a Admin Usuario Servlet.
 *
 * @author Equipo UniMarket
 */
public class AdminUsuarioServlet extends HttpServlet {

    private final UsuarioDao usuarioDao = new UsuarioDao();
    private final DivisionAcademicaDao divisionDao = new DivisionAcademicaDao();

    /**
     * Maneja las peticiones GET para validar la sesión del administrador, cargar los usuarios y el mapa de divisiones académicas.
     *
     * @param request  Objeto HttpServletRequest para enviar los atributos a la vista.
     * @param response Objeto HttpServletResponse para redirigir al login o al JSP de administración.
     * @throws ServletException Si ocurre un error específico del servlet.
     * @throws IOException      Si ocurre un error de E/S.
     * @author Dulce Yazmin Canseco Juárez
     * @date 2026-06-06
     */
    @Override
<<<<<<< HEAD
<<<<<<< HEAD
    public void init() {
        usuarioDao = new UsuarioDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        String matricula = request.getParameter("matricula");
        if (matricula == null || matricula.isEmpty()) {
            matricula = request.getParameter("idUsuario");
        }

        // Acción para desactivar (borrado lógico)
        if ("desactivar".equals(accion) || "eliminar".equals(accion)) {
            if (matricula != null && !matricula.isEmpty()) {
                usuarioDao.cambiarEstado(matricula, "INACTIVO");
            }
            response.sendRedirect(request.getContextPath() + "/adminusuarios");
            return;
        }
        // Acción para activar desde GET
        else if ("activar".equals(accion)) {
            if (matricula != null && !matricula.isEmpty()) {
                usuarioDao.activarUsuario(matricula);
            }
            response.sendRedirect(request.getContextPath() + "/adminusuarios");
            return;
        }

        // Cargar lista filtrada de usuarios activos para el panel de administración
        List<Usuario> listaUsuarios = usuarioDao.getUsuariosActivosParaAdmin();
        request.setAttribute("listaUsuarios", listaUsuarios);
=======
=======
/**
 * Procesa una solicitud HTTP GET y prepara la respuesta correspondiente.
 * @param request Parámetro de entrada de la operación.
 * @param response Parámetro de entrada de la operación.
 * @throws ServletException Excepción declarada por la operación.
 * @throws IOException Excepción declarada por la operación.
 */
>>>>>>> origin/sergio
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Validar sesión del administrador por seguridad
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 2. Obtener la lista de usuarios
        List<Usuario> listaUsuarios = usuarioDao.getAll();
>>>>>>> sergio

        // 3. Obtener todas las divisiones académicas de la base de datos
        List<DivisionAcademica> listaDivisiones = divisionDao.getAll();

        // 4. Crear el "Diccionario" (Map) para traducir el ID al Nombre de la división
        Map<Integer, String> mapaDivisiones = new HashMap<>();
        if (listaDivisiones != null) {
            for (DivisionAcademica div : listaDivisiones) {
                mapaDivisiones.put(div.getIdDivisionAcademica(), div.getDivisionAcademica());
            }
        }

        // 5. Enviar las listas a la vista JSP (AQUÍ ESTÁ LA CORRECCIÓN A "listaUsuarios")
        request.setAttribute("listaUsuarios", listaUsuarios);
        request.setAttribute("mapaDivisiones", mapaDivisiones);

        // 6. Redirigir al JSP
        request.getRequestDispatcher("/admin/usuarios.jsp").forward(request, response);
    }

    /**
     * Maneja las peticiones POST para procesar acciones administrativas sobre los usuarios, como la eliminación por matrícula.
     *
     * @param request  Objeto HttpServletRequest con los parámetros de la acción y la matrícula.
     * @param response Objeto HttpServletResponse para redireccionar al panel de usuarios.
     * @throws ServletException Si ocurre un error específico del servlet.
     * @throws IOException      Si ocurre un error de E/S.
     * @author Dulce Yazmin Canseco Juárez
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
        String accion = request.getParameter("accion");
        String matricula = request.getParameter("matricula");

        if ("eliminar".equals(accion) && matricula != null) {
            usuarioDao.delete(matricula);
        }

        // Recargar la tabla
        response.sendRedirect(request.getContextPath() + "/adminusuarios");
    }
}