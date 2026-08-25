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

@WebServlet(name = "PerfilVendedorServlet", value = "/perfil-vendedor")
/**
 * Controlador web de MUA. Gestiona la interacción HTTP correspondiente a Perfil Vendedor Servlet.
 *
 * @author Equipo UniMarket
 */
public class PerfilVendedorServlet extends HttpServlet {
    private final UsuarioDao usuarioDao = new UsuarioDao();
    private final ComentarioDao comentarioDao = new ComentarioDao();
    private final ArticuloDao articuloDao = new ArticuloDao();

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