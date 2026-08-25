package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.Comentario;
import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.model.dao.ArticuloDao;
import com.unimarket.unimarketintegradora.model.dao.ComentarioDao;
import com.unimarket.unimarketintegradora.model.dao.UsuarioDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Servlet encargado de gestionar la visualización del perfil público de un vendedor,
 * consultando su información, comentarios y calificaciones, calculando porcentajes de estrellas y traduciendo su división académica.
 *
 * @author Luis Fernando Rodriguez Rayo
 * @date 2026-06-06
 */
@WebServlet(name = "PerfilVendedorServlet", value = "/perfil-vendedor")

public class PerfilVendedorServlet extends HttpServlet {
    private final UsuarioDao usuarioDao = new UsuarioDao();
    private final ComentarioDao comentarioDao = new ComentarioDao();
    private final ArticuloDao articuloDao = new ArticuloDao();

    /**
     * Maneja las peticiones GET para obtener la matrícula del vendedor, validar su existencia,
     * procesar sus valoraciones y estadísticas de publicaciones, y reenviar los datos a la vista de perfil de vendedor.
     *
     * @param request  Objeto HttpServletRequest con el parámetro de matrícula del vendedor.
     * @param response Objeto HttpServletResponse para redirigir al inicio en caso de no encontrarse o reenviar al JSP.
     * @throws ServletException Si ocurre un error específico del servlet.
     * @throws IOException      Si ocurre un error de E/S.
     * @author Luis Fernando Rodriguez Rayo
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
        String matricula = request.getParameter("matricula");
        if (matricula == null || matricula.isEmpty()) {
            response.sendRedirect("inicio");
            return;
        }

        Usuario vendedor = usuarioDao.getById(matricula);
        if (vendedor == null) {
            response.sendRedirect("inicio");
            return;
        }

        // 2. Obtener reseñas y calcular promedios
        List<Comentario> comentarios = comentarioDao.obtenerPorVendedor(matricula);
        int totalOpiniones = comentarios.size();
        double sumaCalificaciones = 0;
        int[] conteoEstrellas = new int[6];

        for (Comentario c : comentarios) {
            sumaCalificaciones += c.getCalificacion();
            if (c.getCalificacion() >= 1 && c.getCalificacion() <= 5) {
                conteoEstrellas[c.getCalificacion()]++;
            }
        }

        double promedio = totalOpiniones > 0 ? (sumaCalificaciones / totalOpiniones) : 0.0;
        int[] porcentajes = new int[6];
        if (totalOpiniones > 0) {
            for (int i = 1; i <= 5; i++) {
                porcentajes[i] = (int) Math.round(((double) conteoEstrellas[i] / totalOpiniones) * 100);
            }
        }

        // 3. Obtener conteo de artículos
        int articulosPublicados = articuloDao.contarPorUsuario(matricula);

        // 4. Enviar datos a la vista
        request.setAttribute("vendedor", vendedor);
        request.setAttribute("comentarios", comentarios);
        request.setAttribute("promedio", String.format("%.1f", promedio).replace(",", "."));
        request.setAttribute("totalOpiniones", totalOpiniones);
        request.setAttribute("porcentajes", porcentajes);
        request.setAttribute("articulosPublicados", articulosPublicados);
        int divisionId = vendedor.getIdDivisionAcademicaFk();
        String nombreDivision = "No especificada";

        if (divisionId == 1) {
            nombreDivision = "DATID";
        } else if (divisionId == 2) {
            nombreDivision = "DAMI";
        } else if (divisionId == 3) {
            nombreDivision = "DACEA";
        } else if (divisionId == 4) {
            nombreDivision = "DATEFI";
        }

        request.setAttribute("nombreDivision", nombreDivision);

        request.getRequestDispatcher("perfil-vendedor.jsp").forward(request, response);
    }
}