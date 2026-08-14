package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.Oferta;
import com.unimarket.unimarketintegradora.model.Usuario;
import com.unimarket.unimarketintegradora.model.dao.NotificacionDao;
import com.unimarket.unimarketintegradora.model.dao.OfertaDao;
import com.unimarket.unimarketintegradora.model.dao.TransaccionDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ResponderOfertaServlet", value = "/responder-oferta")
public class ResponderOfertaServlet extends HttpServlet {
    private final OfertaDao ofertaDao = new OfertaDao();
    private final NotificacionDao notificacionDao = new NotificacionDao();
    private final TransaccionDao transaccionDao = new TransaccionDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            out.print("{\"exito\": false, \"mensaje\": \"Tu sesión ha expirado.\"}");
            return;
        }

        String idOfertaStr = request.getParameter("idOferta");
        String nuevoEstado = request.getParameter("estado");
        String mensajeUsuario = request.getParameter("mensaje");

        try {
            int idOferta = Integer.parseInt(idOfertaStr);
            Oferta detalle = ofertaDao.obtenerDetalleParaNotificacion(idOferta);
            if (detalle == null) {
                out.print("{\"exito\": false, \"mensaje\": \"La oferta ya no está disponible.\"}");
                return;
            }

            String articulo = detalle.getNombreArticulo();
            String comprador = detalle.getMatriculaUsuarioFk();
            String vendedor = detalle.getNombreUsuario(); // guardado temporalmente aquí (ver OfertaDao)
            boolean exitoOperacion;

            if ("RECHAZADA".equalsIgnoreCase(nuevoEstado)) {
                String mensaje = "Tu oferta de $" + detalle.getMonto() + " MXN por '" + articulo + "' fue rechazada por el vendedor.";
                notificacionDao.crearNotificacion(comprador, mensaje, "OFERTA");
                exitoOperacion = ofertaDao.eliminarOferta(idOferta);

                if (exitoOperacion) {
                    out.print("{\"exito\": true, \"mensaje\": \"Acción realizada correctamente.\"}");
                } else {
                    out.print("{\"exito\": false, \"mensaje\": \"Hubo un problema al procesar la oferta en la base de datos.\"}");
                }

            } else if ("CANCELADA".equalsIgnoreCase(nuevoEstado)) {
                String mensaje = "El comprador retiró su oferta de $" + detalle.getMonto() + " MXN para tu artículo '" + articulo + "'.";
                notificacionDao.crearNotificacion(vendedor, mensaje, "OFERTA");
                exitoOperacion = ofertaDao.eliminarOferta(idOferta);

                if (exitoOperacion) {
                    out.print("{\"exito\": true, \"mensaje\": \"Acción realizada correctamente.\"}");
                } else {
                    out.print("{\"exito\": false, \"mensaje\": \"Hubo un problema al procesar la oferta en la base de datos.\"}");
                }

            } else {
                // ACEPTAR OFERTA: se crea la transacción como PENDIENTE (igual que una compra directa,
                // pero con el monto negociado). Se confirma después desde "Mis artículos > En proceso".
                boolean transaccionCreada = transaccionDao.crearTransaccionPendiente(
                        detalle.getIdArticuloFk(),
                        vendedor,   // Vendedor
                        comprador,  // Comprador
                        detalle.getMonto()
                );

                if (!transaccionCreada) {
                    out.print("{\"exito\": false, \"mensaje\": \"No se pudo registrar la transacción de la oferta.\"}");
                    return;
                }

                exitoOperacion = ofertaDao.cambiarEstado(idOferta, "ACEPTADA");

                if (!exitoOperacion) {
                    out.print("{\"exito\": false, \"mensaje\": \"La transacción se creó, pero no se pudo actualizar el estado de la oferta.\"}");
                    return;
                }

                String mensajeNotificacion = "¡Felicidades! Tu oferta por '" + articulo + "' fue aceptada. Ponte en contacto con el vendedor.";
                notificacionDao.crearNotificacion(comprador, mensajeNotificacion, "OFERTA");

                // Datos de contacto del COMPRADOR (a quien el vendedor debe contactar ahora)
                String telefonoComprador = transaccionDao.obtenerTelefonoUsuario(comprador);
                String correoComprador = transaccionDao.obtenerCorreoVendedor(comprador);

                // --- MENSAJE CON ESTRUCTURA FIJA: saludo + (mensaje del vendedor, si escribió algo) + cierre + foto ---
                String saludo = "Hola, buenas tardes 👋 Oye, vi tu oferta de $" + detalle.getMonto() +
                        " MXN por *" + articulo + "* y la acepto.";
                String cierre = "¿Dónde te veo para la entrega?";
                String urlImagen = construirUrlImagen(request, detalle.getImagenArticulo());

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

                out.print("{\"exito\": true, \"mensaje\": \"Oferta aceptada correctamente.\", " +
                        "\"telefonoComprador\": \"" + telefonoComprador + "\", " +
                        "\"correoComprador\": \"" + correoComprador + "\", " +
                        "\"mensajeChat\": \"" + mensajeChat + "\"}");
            }

        } catch (NumberFormatException e) {
            out.print("{\"exito\": false, \"mensaje\": \"Identificador no válido.\"}");
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