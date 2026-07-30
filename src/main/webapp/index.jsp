<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.unimarket.unimarketintegradora.model.Articulo" %>
<%@ page import="java.util.List" %>

<%!
  private String textoSeguro(String valor) {
    if (valor == null) {
      return "";
    }
    return valor
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
  }

  private String resolverImagen(String contextPath, String imagenPrincipal) {
    if (imagenPrincipal == null || imagenPrincipal.trim().isEmpty()) {
      return "";
    }
    if (imagenPrincipal.startsWith("http://") || imagenPrincipal.startsWith("https://")) {
      return imagenPrincipal;
    }
    if (imagenPrincipal.startsWith("/")) {
      return contextPath + imagenPrincipal;
    }
    return contextPath + "/" + imagenPrincipal;
  }
%>

<%
  @SuppressWarnings("unchecked")
  List<Articulo> articulos = (List<Articulo>) request.getAttribute("listaArticulos");
%>

<!doctype html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <title>Inicio - MUA</title>
  <link rel="icon" href="<%= request.getContextPath() %>/static/img/logoMUA.png" type="image/png">
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/bootstrap.css">
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/bi_s/bootstrap-icons.css">
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/index.css">
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/header.css">
</head>

<body>

<jsp:include page="components/header.jsp" />

<main class="index-page">

  <section class="index-title">
    <div class="index-title-icon">
      <i class="bi bi-house-door"></i>
    </div>
    <div>
      <h1>Inicio</h1>
      <p>Encuentra lo que necesitas en la universidad</p>
    </div>
  </section>

  <section class="catalog-layout">
    <aside class="filters-card">
      <h2>Filtros</h2>
      <div class="filter-group">
        <label for="orden">Ordenar por</label>
        <select id="orden" class="filter-control">
          <option selected>Recientes</option>
          <option>Precio menor</option>
          <option>Precio mayor</option>
          <option>Más populares</option>
        </select>
      </div>

      <div class="filter-group">
        <label for="categoria">Categoría</label>
        <select id="categoria" class="filter-control">
          <option selected>Todas</option>
          <option>Electrónica y Gadgets</option>
          <option>Libros</option>
          <option>Ropa</option>
          <option>Accesorios</option>
          <option>Material escolar</option>
          <option>Otros</option>
        </select>
      </div>

      <div class="filter-group">
        <label for="carrera">Carrera</label>
        <select id="carrera" class="filter-control">
          <option selected>Todas</option>
          <option>Desarrollo Multiplataforma</option>
          <option>Terapia Física</option>
          <option>Diseño Digital</option>
          <option>Mecatrónica</option>
        </select>
      </div>

      <div class="filter-group">
        <div class="price-header">
          <label>Precio</label>
          <span>$0 - $5,000</span>
        </div>
        <div class="price-inputs">
          <input type="number" value="0" min="0" class="filter-control">
          <input type="number" value="5000" min="0" class="filter-control">
        </div>
      </div>

      <div class="active-filters">
        <h3>Filtros activos</h3>
        <p>Mostrando todo el catálogo disponible.</p>
      </div>
    </aside>

    <section class="products-panel">
      <div class="products-grid">

        <% if (articulos == null || articulos.isEmpty()) { %>
        <div class="empty-catalog">
          <i class="bi bi-box-seam"></i>
          <h2>Aún no hay artículos publicados</h2>
          <p>Cuando publiques un artículo, aparecerá aquí automáticamente.</p>
        </div>
        <% } else { %>
        <% for (Articulo articulo : articulos) { %>
        <article class="product-card">

          <%
            String imagen = resolverImagen(request.getContextPath(), articulo.getImagenPrincipal());
          %>

          <% if (!imagen.isEmpty()) { %>
          <img class="product-img"
               src="<%= textoSeguro(imagen) %>"
               alt="<%= textoSeguro(articulo.getNombre()) %>">
          <% } else { %>
          <div class="product-placeholder">
            <i class="bi bi-image"></i>
          </div>
          <% } %>

          <div class="product-info">
            <h3><%= textoSeguro(articulo.getNombre()) %></h3>
            <p>Vendedor: <%= textoSeguro(articulo.getNombreUsuario()) %></p>
            <strong>
              $<%= articulo.getPrecio() != null ? articulo.getPrecio().toPlainString() : "0.00" %>
            </strong>
            <a href="<%= request.getContextPath() %>/detalles-articulo?id=<%= articulo.getIdArticulo() %>" class="details-button">Mas detalles</a>
          </div>
        </article>
        <% } %>
        <% } %>

      </div>
    </section>
  </section>

</main>

<script src="<%= request.getContextPath() %>/assets/js/bootstrap.js"></script>
</body>
</html>