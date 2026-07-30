<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

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

<body>

<%-- Header integrado --%>
<jsp:include page="/components/header.jsp" />

<main class="history-page">

  <section class="history-title">
    <a href="${pageContext.request.contextPath}/perfil" class="back-link"><i class="bi bi-arrow-left"></i> Volver al perfil</a>
    <h1>Historial de Actividad</h1>
    <p>Todas las compras y ventas realizadas en tu cuenta</p>
  </section>

  <section class="history-list">

    <c:choose>
      <%-- Si la lista está vacía --%>
      <c:when test="${empty historial}">
        <div class="empty-history">
          <i class="bi bi-bag-check"></i>
          <h2>Aún no hay actividad</h2>
          <p>Cuando realices compras o ventas, aparecerán en esta sección de forma automática.</p>
        </div>
      </c:when>

      <%-- Si hay transacciones --%>
      <c:otherwise>
        <c:forEach var="item" items="${historial}">
          <div class="history-item">
            <div class="icon-box ${item.tipo == 'COMPRA' ? 'bg-compra' : 'bg-venta'}">
              <i class="bi ${item.tipo == 'COMPRA' ? 'bi-bag-check-fill' : 'bi-cash-stack'}"></i>
            </div>

            <div class="item-details">
              <h3>${item.tituloArticulo}</h3>
              <p>
                <c:if test="${item.tipo == 'COMPRA'}">Comprado a: </c:if>
                <c:if test="${item.tipo == 'VENTA'}">Vendido a: </c:if>
                <strong>${item.nombreContraparte}</strong>
              </p>
            </div>

            <div class="item-type">
              <span class="badge ${item.tipo == 'COMPRA' ? 'badge-compra' : 'badge-venta'}">${item.tipo}</span>
            </div>

            <div class="item-price-date">
              <span class="price">$<fmt:formatNumber value="${item.precio}" pattern="#,##0.00"/></span>
              <small><fmt:formatDate value="${item.fecha}" pattern="dd/MM/yyyy HH:mm"/></small>
            </div>
          </div>
        </c:forEach>
      </c:otherwise>
    </c:choose>

  </section>

</main>

</body>
</html>