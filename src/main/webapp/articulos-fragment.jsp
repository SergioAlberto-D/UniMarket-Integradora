<%--
  DOCUMENTACIÓN TÉCNICA — MUA
  Archivo: src/main/webapp/articulos-fragment.jsp
  Propósito: Recurso de vista JSP para el módulo articulos-fragment. Integra HTML, JSTL y/o expresiones JSP según el contenido fuente.
  Integración: la vista recibe datos desde Servlets mediante request/session y utiliza recursos CSS/JS del proyecto.
  Created by IntelliJ IDEA.
  User: sergio
  Date: 8/10/26
  Time: 8:45 AM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.unimarket.unimarketintegradora.model.Articulo" %>
<%@ page import="java.util.List" %>

<%!
    // Mantenemos las funciones de limpieza y resolución aquí para que el fragmento sea independiente
    private String textoSeguro(String valor) {
        if (valor == null) return "";
        return valor.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;");
    }

    private String resolverImagen(String contextPath, String imagenPrincipal) {
        if (imagenPrincipal == null || imagenPrincipal.trim().isEmpty()) return "";
        if (imagenPrincipal.startsWith("http://") || imagenPrincipal.startsWith("https://")) return imagenPrincipal;
        if (imagenPrincipal.startsWith("/")) return contextPath + imagenPrincipal;
        return contextPath + "/" + imagenPrincipal;
    }
%>

<%
    @SuppressWarnings("unchecked")
    List<Articulo> articulos = (List<Articulo>) request.getAttribute("listaArticulos");
%>

<% if (articulos == null || articulos.isEmpty()) { %>
<div class="empty-catalog">
    <i class="bi bi-box-seam"></i>
    <h2>Aún no hay artículos publicados</h2>
    <p>Cuando publiques un artículo o ajustemos los filtros, aparecerán aquí automáticamente.</p>
</div>
<% } else { %>
<% for (Articulo articulo : articulos) { %>
<article class="product-card">
    <% String imagen = resolverImagen(request.getContextPath(), articulo.getImagenPrincipal()); %>

    <% if (!imagen.isEmpty()) { %>
    <img class="product-img" src="<%= textoSeguro(imagen) %>" alt="<%= textoSeguro(articulo.getNombre()) %>">
    <% } else { %>
    <div class="product-placeholder"><i class="bi bi-image"></i></div>
    <% } %>

    <div class="product-info">
        <h3><%= textoSeguro(articulo.getNombre()) %></h3>
        <p>Vendedor: <%= textoSeguro(articulo.getNombreUsuario()) %></p>
        <strong>$<%= articulo.getPrecio() != null ? articulo.getPrecio().toPlainString() : "0.00" %></strong>
        <a href="<%= request.getContextPath() %>/detalles-articulo?id=<%= articulo.getIdArticulo() %>" class="details-button">Más detalles</a>
    </div>
</article>
<% } %>
<% } %>
