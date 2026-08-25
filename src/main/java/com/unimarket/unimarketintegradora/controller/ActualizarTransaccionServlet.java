package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.utils.SQLConnector;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Servlet encargado de actualizar el estado de una transacción (completar o cancelar/eliminar pendiente).
 *
 * @author Dulce Yazmin Canseco Juárez
 * @date 2026-06-06
 */
@WebServlet(name = "ActualizarTransaccionServlet", value = "/actualizar-transaccion")
/**
 * Controlador web de MUA. Gestiona la interacción HTTP correspondiente a Actualizar Transaccion Servlet.
 *
 * @author Equipo UniMarket
 */
public class ActualizarTransaccionServlet extends HttpServlet {

    /**
     * Maneja las peticiones POST para actualizar o eliminar la transacción de un artículo según su estado.
     *
     * @param request  Objeto HttpServletRequest con los parámetros de la petición (idArticulo y estado).
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String idArticuloStr = request.getParameter("idArticulo");
        String estado = request.getParameter("estado"); // 'COMPLETADO' o 'CANCELADO'

        try {
            int idArticulo = Integer.parseInt(idArticuloStr);
            String sql;

            if ("COMPLETADO".equals(estado)) {
                // Se marca como completada para que quede en el historial de ventas
                sql = "UPDATE transaccion SET estado = 'COMPLETADO' WHERE id_articulo_fk = ? AND estado = 'PENDIENTE'";
            } else {
                // Si el vendedor elige "No vendido", se elimina la transacción pendiente para que el artículo vuelva a estar disponible
                sql = "DELETE FROM transaccion WHERE id_articulo_fk = ? AND estado = 'PENDIENTE'";
            }

            try (Connection con = SQLConnector.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idArticulo);
                if (ps.executeUpdate() > 0) {
                    out.print("{\"exito\": true, \"mensaje\": \"El estado del artículo se actualizó correctamente.\"}");
                } else {
                    out.print("{\"exito\": false, \"mensaje\": \"No se encontró una transacción pendiente para este artículo.\"}");
                }
            }
        } catch (Exception e) {
            out.print("{\"exito\": false, \"mensaje\": \"Error al procesar la actualización.\"}");
        }
    }
}