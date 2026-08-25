package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.Articulo;
import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.model.dao.ArticuloDao;
import com.unimarket.unimarketintegradora.model.dao.TransaccionDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet encargado de procesar la solicitud de compra de un artículo,
 * registrando la transacción pendiente y generando los datos de contacto y mensaje estructurado para el chat.
 *
 * @author Dulce Yazmin Canseco Juárez
 * @date 2026-06-06
 */
@WebServlet(name = "ComprarArticuloServlet", value = "/comprar-articulo")
/**
 * Controlador web de MUA. Gestiona la interacción HTTP correspondiente a Comprar Articulo Servlet.
 *
 * @author Equipo UniMarket
 */
public class ComprarArticuloServlet extends HttpServlet {

    /**
     * Maneja las peticiones POST para validar la sesión del comprador, verificar la disponibilidad del artículo,
     * registrar la transacción pendiente y retornar los datos y mensaje estructurado en formato JSON.
     *
     * @param request  Objeto HttpServletRequest con los parámetros del artículo y el mensaje opcional del comprador.
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

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            out.print("{\"exito\": false, \"mensaje\": \"Debes iniciar sesión para realizar una compra.\"}");
            return;
        }

        Usuario comprador = (Usuario) session.getAttribute("usuario");
        String idArticuloStr = request.getParameter("idArticulo");
        String mensajeUsuario = request.getParameter("mensaje");

        try {
            int idArticulo = Integer.parseInt(idArticuloStr);
            ArticuloDao articuloDao = new ArticuloDao();
            // Usamos getDetallesCompletos (en vez de getById) porque también trae la imagen principal
            Articulo articulo = articuloDao.getDetallesCompletos(String.valueOf(idArticulo));

            if (articulo == null) {
                out.print("{\"exito\": false, \"mensaje\": \"El artículo ya no está disponible.\"}");
                return;
            }

            // Registrar transacción con estado PENDIENTE
            TransaccionDao transaccionDao = new TransaccionDao();
            boolean registrada = transaccionDao.crearTransaccionPendiente(
                    idArticulo,
                    articulo.getIdUsuarioFk(), // Vendedor
                    comprador.getMatricula(),  // Comprador
                    articulo.getPrecio()
            );

            if (registrada) {
                String telefonoVendedor = transaccionDao.obtenerTelefonoUsuario(articulo.getIdUsuarioFk());
                String correoVendedor = transaccionDao.obtenerCorreoVendedor(articulo.getIdUsuarioFk());

                // --- MENSAJE CON ESTRUCTURA FIJA: saludo + (mensaje del comprador, si escribió algo) + cierre + foto ---
                String saludo = "Hola, buenas tardes 👋 Vi tu artículo *" + articulo.getNombre() +
                        "* en MUA y me interesa comprarlo ($" + articulo.getPrecio() + " MXN).";
                String cierre = "¿Dónde y cuándo podemos vernos en la universidad?";
                String urlImagen = construirUrlImagen(request, articulo.getImagenPrincipal());

                StringBuilder mensaje = new StringBuilder();
                mensaje.append(saludo);
                if (mensajeUsuario != null && !mensajeUsuario.trim().isEmpty()) {
                    mensaje.append("\n\n").append(mensajeUsuario.trim());
                }
                mensaje.append("\n\n").append(cierre);
                if (urlImagen != null) {
                    mensaje.append("\n\nFoto del producto: ").append(urlImagen);
                }

                String mensajeChat = prepararTextoJson(mensaje.toString());

                out.print("{\"exito\": true, \"telefonoVendedor\": \"" + telefonoVendedor + "\", \"correoVendedor\": \"" + correoVendedor + "\", \"mensajeChat\": \"" + mensajeChat + "\"}");
            } else {
                out.print("{\"exito\": false, \"mensaje\": \"No se pudo procesar la solicitud de compra.\"}");
            }

        } catch (Exception e) {
            out.print("{\"exito\": false, \"mensaje\": \"Error en los datos de la compra: " + e.getMessage() + "\"}");
        }
    }

    // Arma una URL absoluta hacia la imagen del artículo (WhatsApp/Gmail solo aceptan texto,
    // así que mandamos el link para que la otra persona la pueda abrir con un toque)
    private String construirUrlImagen(HttpServletRequest request, String rutaImagen) {
        if (rutaImagen == null || rutaImagen.trim().isEmpty()) {
            return null;
        }
        String esquema = request.getScheme();
        String host = request.getServerName();
        int puerto = request.getServerPort();
        boolean puertoEstandar = (esquema.equals("http") && puerto == 80) || (esquema.equals("https") && puerto == 443);

        StringBuilder base = new StringBuilder();
        base.append(esquema).append("://").append(host);
        if (!puertoEstandar) {
            base.append(":").append(puerto);
        }
        base.append(request.getContextPath()).append("/").append(rutaImagen);
        return base.toString();
    }

    // Escapa el texto para insertarlo dentro de un JSON armado a mano,
    // conservando los saltos de línea como \n literal (no los borra)
    private String prepararTextoJson(String texto) {
        if (texto == null) return "";
        return texto.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "")
                .replace("\n", "\\n");
    }
}