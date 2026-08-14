package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.Articulo;
import com.unimarket.unimarketintegradora.model.Comentario;
import com.unimarket.unimarketintegradora.model.ImagenArticulo;
import com.unimarket.unimarketintegradora.model.dao.ArticuloDao;
import com.unimarket.unimarketintegradora.model.dao.ComentarioDao;
import com.unimarket.unimarketintegradora.model.dao.ImagenArticuloDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "DetallesArticuloServlet", value = "/detalles-articulo")
public class DetallesArticuloServlet extends HttpServlet {
    private final ArticuloDao articuloDao = new ArticuloDao();
    private final ImagenArticuloDao imagenDao = new ImagenArticuloDao();
    private final ComentarioDao comentarioDao = new ComentarioDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idArticuloParam = request.getParameter("id");
        if (idArticuloParam == null || idArticuloParam.isEmpty()) {
            response.sendRedirect("inicio");
            return;
        }

        Articulo articulo = articuloDao.getDetallesCompletos(idArticuloParam);
        if (articulo == null) {
            response.sendRedirect("inicio");
            return;
        }

        List<ImagenArticulo> imagenes = imagenDao.obtenerPorArticulo(articulo.getIdArticulo());
        List<Comentario> comentarios = comentarioDao.obtenerPorVendedor(articulo.getIdUsuarioFk());

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

        request.setAttribute("articulo", articulo);
        request.setAttribute("imagenes", imagenes);
        request.setAttribute("comentarios", comentarios);
        request.setAttribute("promedio", String.format("%.1f", promedio).replace(",", "."));
        request.setAttribute("totalOpiniones", totalOpiniones);
        request.setAttribute("porcentajes", porcentajes);
        String nombreCategoria = "General";
        int idCat = articulo.getIdCategoriaFk();

        if (idCat == 1) {
            nombreCategoria = "Electrónica y Gadgets";
        } else if (idCat == 2) {
            nombreCategoria = "Libros";
        } else if (idCat == 3) {
            nombreCategoria = "Ropa";
        } else if (idCat == 4) {
            nombreCategoria = "Accesorios";
        } else if (idCat == 5) {
            nombreCategoria = "Material escolar";
        } else if (idCat == 6) {
            nombreCategoria = "Otros";
        }
        request.setAttribute("nombreCategoria", nombreCategoria);

        request.getRequestDispatcher("detalles-articulo.jsp").forward(request, response);
    }
}