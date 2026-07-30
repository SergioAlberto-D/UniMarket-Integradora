package com.unimarket.unimarketintegradora.controller;

import com.unimarket.unimarketintegradora.model.Articulo;
import com.unimarket.unimarketintegradora.model.dao.ArticuloDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

// Asignamos la ruta "/inicio" a este Servlet
@WebServlet(name = "InicioServlet", value = "/inicio")
public class InicioServlet extends HttpServlet {

    private final ArticuloDao articuloDao = new ArticuloDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Obtenemos la lista de artículos desde la base de datos
        List<Articulo> articulos = articuloDao.getAll();

        // 2. Guardamos la lista en el request con el nombre "listaArticulos"
        request.setAttribute("listaArticulos", articulos);

        // 3. Redirigimos el tráfico hacia el index.jsp para que dibuje la pantalla
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}