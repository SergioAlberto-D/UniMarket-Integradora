package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.Articulo;
import com.unimarket.unimarketintegradora.model.Oferta;
import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.model.dao.ArticuloDao;
import com.unimarket.unimarketintegradora.model.dao.NotificacionDao;
import com.unimarket.unimarketintegradora.model.dao.OfertaDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

@WebServlet(name = "OfertarServlet", value = "/ofertar-articulo")
/**
 * Controlador web de MUA. Gestiona la interacción HTTP correspondiente a Ofertar Servlet.
 *
 * @author Equipo UniMarket
 */
public class OfertarServlet extends HttpServlet {
    private final OfertaDao ofertaDao = new OfertaDao();
    private final NotificacionDao notificacionDao = new NotificacionDao();
    private final ArticuloDao articuloDao = new ArticuloDao();

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

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            out.print("{\"exito\": false, \"mensaje\": \"Debes iniciar sesión para hacer una oferta.\"}");
            return;
        }

        Usuario comprador = (Usuario) session.getAttribute("usuario");
        String matriculaComprador = comprador.getMatricula();
        String matriculaVendedor = request.getParameter("matriculaVendedor");
        String idArticuloStr = request.getParameter("idArticulo");
        String montoStr = request.getParameter("monto");

        if (matriculaComprador.equalsIgnoreCase(matriculaVendedor)) {
            out.print("{\"exito\": false, \"mensaje\": \"No puedes hacer ofertas en tus propios artículos.\"}");
            return;
        }

        try {
            int idArticulo = Integer.parseInt(idArticuloStr);
            BigDecimal monto = new BigDecimal(montoStr);

            if (monto.compareTo(BigDecimal.ZERO) <= 0) {
                out.print("{\"exito\": false, \"mensaje\": \"El monto de la oferta debe ser mayor a cero.\"}");
                return;
            }

            if (ofertaDao.existeOfertaPrevia(idArticulo, matriculaComprador)) {
                out.print("{\"exito\": false, \"mensaje\": \"Ya tienes una oferta pendiente para este artículo. Solo se permite 1 oferta activa por producto.\"}");
                return;
            }

            Oferta nuevaOferta = new Oferta(idArticulo, matriculaComprador, monto, "PENDIENTE");
            boolean guardado = ofertaDao.create(nuevaOferta);

            if (guardado) {
                // Notificar al vendedor en su campana
                Articulo art = articuloDao.getById(String.valueOf(idArticulo));
                String nombreArt = art != null ? art.getNombre() : "un artículo";
                String msjVendedor = "Recibiste una nueva oferta de $" + monto + " MXN en tu publicación '" + nombreArt + "'.";
                notificacionDao.crearNotificacion(matriculaVendedor, msjVendedor, "OFERTA");

                out.print("{\"exito\": true, \"mensaje\": \"¡Oferta enviada con éxito!\"}");
            } else {
                out.print("{\"exito\": false, \"mensaje\": \"Error al registrar la oferta en la base de datos.\"}");
            }
        } catch (Exception e) {
            out.print("{\"exito\": false, \"mensaje\": \"Los datos enviados no son válidos.\"}");
        }
    }
}