<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ page import="com.unimarket.unimarketintegradora.model.Articulo" %>

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
    <div class="index-title-icon"><i class="bi bi-house-door"></i></div>
    <div>
      <h1>Inicio</h1>
      <p>Encuentra lo que necesitas en la universidad</p>
    </div>
  </section>

  <section class="catalog-layout">
    <aside class="filters-card">
      <h2>Filtros <a href="<%= request.getContextPath() %>/inicio" style="font-size:12px; float:right; color:#7c3f1d; text-decoration:none;">Limpiar</a></h2>

      <form id="formFiltros" method="GET" action="<%= request.getContextPath() %>/inicio">
        <div class="filter-group">
          <label for="orden">Ordenar por</label>
          <select name="orden" id="orden" class="filter-control">
            <option value="Recientes" <%= "Recientes".equals(request.getAttribute("ordenSel")) ? "selected" : "" %>>Recientes</option>
            <option value="Precio menor" <%= "Precio menor".equals(request.getAttribute("ordenSel")) ? "selected" : "" %>>Precio menor</option>
            <option value="Precio mayor" <%= "Precio mayor".equals(request.getAttribute("ordenSel")) ? "selected" : "" %>>Precio mayor</option>
          </select>
        </div>

        <div class="filter-group">
          <label for="categoria">Categoría</label>
          <select name="categoria" id="categoria" class="filter-control">
            <option value="0" ${catSel == null || catSel == 0 ? 'selected' : ''}>Todas</option>
            <c:forEach var="cat" items="${categorias}">
              <option value="${cat.idCategoria}" ${catSel == cat.idCategoria ? 'selected' : ''}>
                  ${fn:escapeXml(cat.categoria)}
              </option>
            </c:forEach>
          </select>
        </div>

        <div class="filter-group">
          <label for="division">División Académica</label>
          <select name="division" id="division" class="filter-control">
            <option value="0" <%= (request.getAttribute("divSel") != null && (Integer)request.getAttribute("divSel") == 0) ? "selected" : "" %>>Todas</option>
            <option value="1" <%= (request.getAttribute("divSel") != null && (Integer)request.getAttribute("divSel") == 1) ? "selected" : "" %>>DATID</option>
            <option value="2" <%= (request.getAttribute("divSel") != null && (Integer)request.getAttribute("divSel") == 2) ? "selected" : "" %>>DAMI</option>
            <option value="3" <%= (request.getAttribute("divSel") != null && (Integer)request.getAttribute("divSel") == 3) ? "selected" : "" %>>DACEA</option>
            <option value="4" <%= (request.getAttribute("divSel") != null && (Integer)request.getAttribute("divSel") == 4) ? "selected" : "" %>>DATEFI</option>
          </select>
        </div>

        <div class="filter-group">
          <div class="price-header">
            <label>Precio</label>
            <span>$0 - $5,000</span>
          </div>
          <div class="price-inputs">
            <input type="number" name="minPrecio" value="<%= request.getAttribute("minSel") != null ? request.getAttribute("minSel") : "" %>" min="0" class="filter-control" placeholder="Mín">
            <input type="number" name="maxPrecio" value="<%= request.getAttribute("maxSel") != null ? request.getAttribute("maxSel") : "" %>" min="0" class="filter-control" placeholder="Máx">
          </div>
          <button type="submit" style="width:100%; margin-top:10px; height:36px; border:none; background:#7c3f1d; color:white; border-radius:8px; font-weight:bold; cursor:pointer;">
            Aplicar precio
          </button>
        </div>
      </form>
    </aside>

    <section class="products-panel">
      <!-- Carga inicial por JSP. Después JS sobreescribirá este contenedor -->
      <div class="products-grid" id="contenedorArticulos">
        <jsp:include page="articulos-fragment.jsp" />
      </div>
    </section>
  </section>

  <%
    Articulo recordatorio = (Articulo) request.getAttribute("articuloRecordatorio");
    if (recordatorio != null) {
  %>
  <div class="modal-overlay" id="modalRecordatorioProceso" style="display: flex; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.55); z-index: 3000; align-items: center; justify-content: center;">
    <div class="floating-modal" style="background: #fff; border-radius: 18px; width: 90%; max-width: 430px; padding: 26px; box-shadow: 0 10px 30px rgba(0,0,0,0.25); text-align: center; position: relative;">
      <button type="button" onclick="document.getElementById('modalRecordatorioProceso').style.display='none'" style="position: absolute; right: 18px; top: 16px; background: none; border: none; font-size: 22px; color: #888; cursor: pointer;">&times;</button>
      <div style="width: 58px; height: 58px; background: #fef3c7; color: #d97706; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 26px; margin: 0 auto 14px;">
        <i class="bi bi-bell-fill"></i>
      </div>
      <h3 style="margin: 0 0 10px; font-size: 20px; font-weight: 800; color: #111;">¿Ya vendiste tu artículo?</h3>
      <p style="font-size: 14px; color: #555; margin-bottom: 24px; line-height: 1.5;">
        Tienes una venta en curso para el producto <strong>"<%= recordatorio.getNombre().replace("\"", "&quot;") %>"</strong>. ¿Ya completaste la entrega en la universidad?
      </p>
      <a href="<%= request.getContextPath() %>/mis-articulos?tab=proceso" style="display: block; width: 100%; height: 45px; line-height: 45px; background: #7c3f1d; color: #fff; border-radius: 10px; font-weight: 700; font-size: 14px; text-decoration: none; box-shadow: 0 4px 12px rgba(124, 63, 29, 0.25);">
        Gestionar estado de venta ahora
      </a>
    </div>
  </div>
  <% } %>
</main>

<script src="<%= request.getContextPath() %>/assets/js/bootstrap.js"></script>
<script src="<%= request.getContextPath() %>/static/js/index.js"></script>
</body>
</html>