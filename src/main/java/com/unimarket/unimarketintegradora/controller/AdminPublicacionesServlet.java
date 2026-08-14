package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.Articulo;
import com.unimarket.unimarketintegradora.model.dao.ArticuloDao;
import com.unimarket.unimarketintegradora.model.dao.NotificacionDao;
import com.unimarket.unimarketintegradora.model.dao.OfertaDao;
import com.unimarket.unimarketintegradora.model.dao.TransaccionDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebServlet("/adminpublicaciones")
public class AdminPublicacionesServlet extends HttpServlet {

    private final ArticuloDao articuloDao = new ArticuloDao();
    private final TransaccionDao transaccionDao = new TransaccionDao();
    private final OfertaDao ofertaDao = new OfertaDao();
    private final NotificacionDao notificacionDao = new NotificacionDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Articulo> listaArticulos = articuloDao.listarParaAdmin();
        request.setAttribute("listaArticulos", listaArticulos);
        request.getRequestDispatcher("/admin/publicaciones.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        try {
            if ("eliminar".equals(accion)) {
                String idArticuloStr = request.getParameter("idArticulo");

                if (idArticuloStr != null && !idArticuloStr.trim().isEmpty()) {
                    int idArticulo = Integer.parseInt(idArticuloStr);

                    // 1. Obtenemos el artículo para conocer al dueño
                    Articulo articulo = articuloDao.getById(idArticuloStr);

                    if (articulo != null) {
                        String matriculaVendedor = articulo.getIdUsuarioFk();

                        // 2. Verificamos si existe una transacción COMPLETADA
                        boolean estaCompletada = transaccionDao.tieneTransaccionCompletada(idArticulo);

                        // 3. Si NO está completada, barremos con las ofertas y transacciones pendientes
                        if (!estaCompletada) {
                            Set<String> compradoresAfectados = new HashSet<>();
                            compradoresAfectados.addAll(transaccionDao.obtenerCompradoresTransaccionesIncompletas(idArticulo));
                            compradoresAfectados.addAll(ofertaDao.obtenerCompradoresOfertasActivas(idArticulo));

                            transaccionDao.eliminarTransaccionesIncompletas(idArticulo);
                            ofertaDao.eliminarOfertasNoCompletadas(idArticulo);

                            for (String comprador : compradoresAfectados) {
                                notificacionDao.crearNotificacion(
                                        comprador,
                                        "El artículo '" + articulo.getNombre() + "' por el que estabas en proceso ha sido dado de baja por moderación.",
                                        "MODERACION"
                                );
                            }
                        }

                        // 4. Baja Lógica del artículo
                        boolean eliminado = articuloDao.delete(idArticuloStr);

                        // 5. Notificar al dueño solo si la BD lo eliminó con éxito
                        if (eliminado) {
                            notificacionDao.crearNotificacion(
                                    matriculaVendedor,
                                    "Tu publicación '" + articulo.getNombre() + "' ha sido dada de baja de la plataforma por un administrador.",
                                    "MODERACION"
                            );
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error en moderación de artículo: " + e.getMessage());
        }

        // Redirección normal garantizada
        response.sendRedirect(request.getContextPath() + "/adminpublicaciones");
    }
}