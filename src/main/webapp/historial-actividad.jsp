<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!doctype html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Historial de actividad - MUA</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="icon" href="${pageContext.request.contextPath}/static/img/logoMUA.png" type="image/png">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bi_s/bootstrap-icons.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/header.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/historial-actividad.css">
</head>

<body style="background-color: #f7efe9;">

<%-- Header integrado --%>
<jsp:include page="/components/header.jsp" />

<main class="history-page">

  <div class="header-section">
    <div class="title-box">
      <div class="title-icon"><i class="bi bi-clock-history"></i></div>
      <div class="title-text">
        <h1>Historial de Actividad</h1>
        <p>Todas las compras y ventas realizadas en tu cuenta</p>
      </div>
    </div>

    <!-- SWITCH DE PESTAÑAS (Idéntico al de Mis Artículos) -->
    <div class="tabs-switch">
      <button type="button" class="tab-btn active" onclick="cambiarPestana('compras', this)">Compras</button>
      <button type="button" class="tab-btn" onclick="cambiarPestana('ventas', this)">Ventas</button>
    </div>
  </div>

  <!-- SECCIÓN 1: COMPRAS -->
  <section id="tab-compras" class="history-list">
    <c:choose>
      <c:when test="${not empty historial}">
        <c:set var="tieneCompras" value="false" />
        <c:forEach var="item" items="${historial}">
          <c:if test="${item.tipo == 'COMPRA'}">
            <c:set var="tieneCompras" value="true" />
            <div class="history-item">
              <div class="icon-box bg-compra">
                <i class="bi bi-bag-check-fill"></i>
              </div>

              <div class="item-details">
                <h3>${fn:escapeXml(item.tituloArticulo)}</h3>
                <p>Compraste <strong>${fn:escapeXml(item.tituloArticulo)}</strong> a <strong>$${item.precio}</strong> a <strong>${fn:escapeXml(item.nombreContraparte)}</strong></p>
              </div>

              <div class="item-type">
                <span class="badge badge-compra">COMPRA</span>
              </div>

              <div class="item-price-date">
                <span class="price">$<fmt:formatNumber value="${item.precio}" pattern="#,##0.00"/></span>
                <small><fmt:formatDate value="${item.fecha}" pattern="dd/MM/yyyy HH:mm"/></small>
              </div>
            </div>
          </c:if>
        </c:forEach>

        <c:if test="${!tieneCompras}">
          <div class="empty-history">
            <i class="bi bi-bag-check"></i>
            <h2>No tienes compras registradas</h2>
            <p>Cuando realices compras de artículos, aparecerán en esta sección de forma automática.</p>
          </div>
        </c:if>
      </c:when>
      <c:otherwise>
        <div class="empty-history">
          <i class="bi bi-bag-check"></i>
          <h2>Aún no hay actividad</h2>
          <p>Cuando realices compras o ventas, aparecerán en esta sección de forma automática.</p>
        </div>
      </c:otherwise>
    </c:choose>
  </section>

  <!-- SECCIÓN 2: VENTAS -->
  <section id="tab-ventas" class="history-list" style="display: none;">
    <c:choose>
      <c:when test="${not empty historial}">
        <c:set var="tieneVentas" value="false" />
        <c:forEach var="item" items="${historial}">
          <c:if test="${item.tipo == 'VENTA'}">
            <c:set var="tieneVentas" value="true" />
            <div class="history-item">
              <div class="icon-box bg-venta">
                <i class="bi bi-cash-stack"></i>
              </div>

              <div class="item-details">
                <h3>${fn:escapeXml(item.tituloArticulo)}</h3>
                <p>Vendiste <strong>${fn:escapeXml(item.tituloArticulo)}</strong> a <strong>${fn:escapeXml(item.nombreContraparte)}</strong> a <strong>$${item.precio}</strong></p>
              </div>

              <div class="item-type">
                <span class="badge badge-venta">VENTA</span>
              </div>

              <div class="item-price-date">
                <span class="price">$<fmt:formatNumber value="${item.precio}" pattern="#,##0.00"/></span>
                <small><fmt:formatDate value="${item.fecha}" pattern="dd/MM/yyyy HH:mm"/></small>
              </div>
            </div>
          </c:if>
        </c:forEach>

        <c:if test="${!tieneVentas}">
          <div class="empty-history">
            <i class="bi bi-cash-stack"></i>
            <h2>No tienes ventas registradas</h2>
            <p>Cuando concretes ventas de tus artículos, aparecerán en esta sección de forma automática.</p>
          </div>
        </c:if>
      </c:when>
      <c:otherwise>
        <div class="empty-history">
          <i class="bi bi-cash-stack"></i>
          <h2>Aún no hay actividad</h2>
          <p>When realices compras o ventas, aparecerán en esta sección de forma automática.</p>
        </div>
      </c:otherwise>
    </c:choose>
  </section>

</main>

<script>
  function cambiarPestana(tipo, btn) {
    document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');

    document.getElementById('tab-compras').style.display = (tipo === 'compras') ? 'flex' : 'none';
    document.getElementById('tab-ventas').style.display = (tipo === 'ventas') ? 'flex' : 'none';
  }
</script>

</body>
</html>