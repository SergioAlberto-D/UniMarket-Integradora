<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="uri" value="${pageContext.request.requestURI}" />
<c:set var="paginaBuscar" value="${uri.endsWith('/') || uri.contains('inicio') || uri.contains('index.jsp')}" />
<c:set var="paginaMisArticulos" value="${uri.contains('mis-articulos')}" />
<c:set var="paginaPublicar" value="${uri.contains('publicar-articulo')}" />

<header class="mua-header">
  <div class="header-left">
    <a href="${pageContext.request.contextPath}/inicio" class="brand">
      <img src="${pageContext.request.contextPath}/static/img/logoMUA.png" alt="Logo MUA" style="height: 75px; width: 75px; object-fit: contain; display: block; transform: scale(1.2);">
      <span>Mua</span>
    </a>

    <div class="search-box">
      <i class="bi bi-search"></i>
      <input type="text" placeholder="Buscar artículos...">
    </div>
  </div>

  <nav class="header-nav">
    <a href="${pageContext.request.contextPath}/inicio" class="${paginaBuscar ? 'nav-active' : 'nav-inactive'}">
      <i class="bi bi-house-door-fill"></i> Inicio
    </a>

    <c:if test="${esVendedor}">
      <a href="${pageContext.request.contextPath}/mis-articulos.jsp" class="${paginaMisArticulos ? 'nav-active' : 'nav-inactive'}">
        <i class="bi bi-archive-fill"></i> Mis artículos
      </a>
    </c:if>

    <a href="${pageContext.request.contextPath}/publicar-articulo.jsp" class="${paginaPublicar ? 'nav-active' : 'nav-inactive'}">
      <i class="bi bi-plus-lg"></i> Publicar
    </a>

    <%-- CONTENEDOR DEL PERFIL Y LA CAJA FLOTANTE --%>
    <div class="caja-flotante-perfil">

      <%-- El círculo que activa el menú --%>
      <div class="circulo-trigger ${empty sessionScope.usuario.fotoPerfil ? 'fondo-iniciales' : ''}">
        <c:choose>
          <c:when test="${not empty sessionScope.usuario.fotoPerfil}">
            <img src="${pageContext.request.contextPath}/${sessionScope.usuario.fotoPerfil}" alt="Perfil" style="width: 100%; height: 100%; border-radius: 50%; object-fit: cover;">
          </c:when>
          <c:otherwise>
            ${iniciales}
          </c:otherwise>
        </c:choose>
      </div>

      <%-- La caja blanca que aparecerá por debajo --%>
        <div class="menu-blanco-desplegable">

        <%-- UN SOLO ENLACE PARA TODOS LOS USUARIOS --%>
        <a href="${pageContext.request.contextPath}/mi-perfil">
          <i class="bi bi-person"></i> Ver perfil
        </a>

        <a href="${pageContext.request.contextPath}/logout" class="opcion-salir">
          <i class="bi bi-box-arrow-right"></i> Cerrar sesión
        </a>
      </div>

    </div>
  </nav>
</header>