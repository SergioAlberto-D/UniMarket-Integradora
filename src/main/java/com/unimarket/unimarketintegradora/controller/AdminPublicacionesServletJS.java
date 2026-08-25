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

/**
 * Servlet encargado de administrar las publicaciones con soporte JavaScript/JSON,
 * realizando bajas lógicas y notificando a los usuarios afectados.
 *
 * @author Dulce Yazmin Canseco Juárez
 * @date 2026-06-06
 */
@WebServlet("/adminpublicacionesJS")
/**
 * Controlador web de MUA. Gestiona la interacción HTTP correspondiente a Admin Publicaciones Servlet J S.
 *
 * @author Equipo UniMarket
 */
public class AdminPublicacionesServletJS extends HttpServlet {

    private final ArticuloDao articuloDao = new ArticuloDao();
    private final TransaccionDao transaccionDao = new TransaccionDao();
    private final OfertaDao ofertaDao = new OfertaDao();
    private final NotificacionDao notificacionDao = new NotificacionDao();

    /**
     * Maneja las peticiones GET para obtener y listar las publicaciones en el panel de administración.
     *
     * @param request  Objeto HttpServletRequest para enviar los artículos a la vista.
     * @param response Objeto HttpServletResponse para reenviar la solicitud al JSP.
     * @throws ServletException Si ocurre un error específico del servlet.
     * @throws IOException      Si ocurre un error de E/S.
     * @author Dulce Yazmin Canseco Juárez
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Articulo> listaArticulos = articuloDao.listarParaAdmin();
        request.setAttribute("listaArticulos", listaArticulos);
        request.getRequestDispatcher("/admin/publicaciones.jsp").forward(request, response);
    }

    /**
     * Maneja las peticiones POST para procesar la baja lógica de publicaciones y notificar tanto al vendedor como a los compradores.
     *
     * @param request  Objeto HttpServletRequest con los parámetros de la acción y el ID del artículo.
     * @param response Objeto HttpServletResponse para enviar la respuesta en formato JSON.
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        boolean success = false;
        String mensaje = "";

        try {
            if ("eliminar".equals(accion)) {
                String idArticuloStr = request.getParameter("idArticulo");
                System.out.println("--- INICIANDO PROCESO DE BAJA LOGICA ---");
                System.out.println("1. ID del artículo recibido: " + idArticuloStr);

                if (idArticuloStr != null && !idArticuloStr.trim().isEmpty()) {
                    int idArticulo = Integer.parseInt(idArticuloStr);

                    Articulo articulo = articuloDao.getById(idArticuloStr);
                    System.out.println("2. ¿Se encontró el artículo en BD?: " + (articulo != null));

                    if (articulo != null) {
                        String matriculaVendedor = articulo.getIdUsuarioFk();

                        System.out.println("3. Verificando transacciones completadas...");
                        boolean estaCompletada = transaccionDao.tieneTransaccionCompletada(idArticulo);
                        System.out.println("4. ¿Tiene transacciones completas?: " + estaCompletada);

                        if (!estaCompletada) {
                            System.out.println("5. Eliminando ofertas y transacciones pendientes en cascada...");
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

                        System.out.println("6. Solicitando UPDATE de estado en la tabla articulo...");
                        // Validamos que el DAO realmente devuelva true al hacer el UPDATE
                        boolean eliminado = articuloDao.delete(idArticuloStr);
                        System.out.println("7. ¿La BD realizó la baja lógica con éxito?: " + eliminado);

                        if (eliminado) {
                            notificacionDao.crearNotificacion(
                                    matriculaVendedor,
                                    "Tu publicación '" + articulo.getNombre() + "' ha sido dada de baja de la plataforma por un administrador.",
                                    "MODERACION"
                            );
                            success = true;
                            System.out.println("8. Proceso finalizado correctamente.");
                        } else {
                            mensaje = "Fallo en la BD: No se pudo hacer el UPDATE. ¿La columna 'estado' existe y está bien escrita?";
                            System.out.println("ERROR: El articuloDao.delete devolvió false.");
                        }
                    } else {
                        mensaje = "Artículo no encontrado.";
                    }
                } else {
                    mensaje = "ID de artículo inválido.";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mensaje = "Error interno del servidor: " + e.getMessage();
            System.out.println("EXCEPCIÓN ATRAPADA: " + e.getMessage());
        }

        // Configurar la respuesta como JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"success\": " + success + ", \"message\": \"" + mensaje + "\"}");
    }
}