package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.Comentario;
import com.unimarket.unimarketintegradora.model.Oferta;
import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.model.dao.ArticuloDao;
import com.unimarket.unimarketintegradora.model.dao.ComentarioDao;
import com.unimarket.unimarketintegradora.model.dao.OfertaDao;

import com.unimarket.unimarketintegradora.model.dao.TransaccionDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Servlet encargado de gestionar el perfil del usuario autenticado, calculando métricas de vendedor y comprador,
 * cargando ofertas enviadas y recibidas, determinando la división académica y reenviando los datos a la vista correspondiente.
 *
 * @author Luis Fernando Rodriguez Rayo
 * @date 2026-06-06
 */
@WebServlet(name = "MiPerfilServlet", value = "/mi-perfil")

public class MiPerfilServlet extends HttpServlet {
    private final ArticuloDao articuloDao = new ArticuloDao();
    private final ComentarioDao comentarioDao = new ComentarioDao();
    private final OfertaDao ofertaDao = new OfertaDao();
    private final TransaccionDao transaccionDao = new TransaccionDao();

    /**
     * Maneja las peticiones GET para validar la sesión del usuario, consultar sus ofertas, calcular estadísticas
     * de publicaciones, calificaciones, comentarios y transacciones según su rol, y reenviar la información al JSP de perfil.
     *
     * @param request  Objeto HttpServletRequest para gestionar la sesión y los atributos enviados a la vista.
     * @param response Objeto HttpServletResponse para redirigir al login si no hay sesión activa o reenviar al JSP.
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
        HttpSession session = request.getSession(false);
        Usuario usuarioLogueado = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        if (usuarioLogueado == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String matricula = usuarioLogueado.getMatricula(); // O getCorreoInstitucional()

        // 1. Cargar Ofertas (Hechas por mí y Recibidas)
        List<Oferta> ofertasHechas = ofertaDao.obtenerOfertasHechasPorUsuario(matricula);
        List<Oferta> ofertasRecibidas = ofertaDao.obtenerOfertasRecibidas(matricula);

        // 2. CÁLCULO PARA VENDEDOR (Rol 3)
        int articulosPublicados = articuloDao.contarPorUsuario(matricula);
        List<Comentario> comentariosRecibidos = comentarioDao.obtenerPorVendedor(matricula);
        int totalComentariosRecibidos = comentariosRecibidos.size();

        double sumaCalificaciones = 0;
        for (Comentario c : comentariosRecibidos) {
            sumaCalificaciones += c.getCalificacion();
        }
        double promedio = (totalComentariosRecibidos > 0) ? (sumaCalificaciones / totalComentariosRecibidos) : 0.0;

        long transaccionesVenta = transaccionDao.contarVentasCompletadas(matricula);

        // 3. CÁLCULO PARA COMPRADOR (Rol 2 y también contable para el Vendedor)
        int comentariosRealizados = comentarioDao.contarComentariosRealizados(matricula);

        long transaccionesCompra = transaccionDao.contarComprasCompletadas(matricula);

        // 4. Traducir División Académica
        String nombreDivision = "DATID";
        int divisionId = usuarioLogueado.getIdDivisionAcademicaFk();
        if (divisionId == 2) nombreDivision = "DAMI";
        else if (divisionId == 3) nombreDivision = "DACEA";
        else if (divisionId == 4) nombreDivision = "DATEFI";

        // 5. Mandar atributos a la vista
        request.setAttribute("articulosPublicados", articulosPublicados);
        request.setAttribute("totalComentariosRecibidos", totalComentariosRecibidos);
        request.setAttribute("comentariosRealizados", comentariosRealizados);
        request.setAttribute("promedioCalificacion", String.format("%.1f", promedio).replace(",", "."));

        // Si es vendedor mostramos ventas cerradas; si es comprador mostramos compras cerradas
        long transaccionesMostrar = (usuarioLogueado.getIdRolFk() == 3) ? transaccionesVenta : transaccionesCompra;
        request.setAttribute("transaccionesCompletadas", transaccionesMostrar);

        request.setAttribute("nombreDivision", nombreDivision);
        request.setAttribute("ofertasHechas", ofertasHechas);
        request.setAttribute("ofertasRecibidas", ofertasRecibidas);

        request.getRequestDispatcher("mi-perfil.jsp").forward(request, response);
    }
}