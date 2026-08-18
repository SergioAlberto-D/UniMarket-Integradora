<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%-- Variable global para que los JS externos puedan usar el Context Path --%>
<script>
  const CONTEXT_PATH = '${pageContext.request.contextPath}';
</script>

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
  </div>

  <nav class="header-nav">
    <a href="${pageContext.request.contextPath}/inicio" class="${paginaBuscar ? 'nav-active' : 'nav-inactive'}">
      <i class="bi bi-house-door-fill"></i> Inicio
    </a>
    <c:if test="${esVendedor}">
      <a href="${pageContext.request.contextPath}/mis-articulos" class="${paginaMisArticulos ? 'nav-active' : 'nav-inactive'}">
        <i class="bi bi-archive-fill"></i> Mis artículos
      </a>
    </c:if>

    <a href="${pageContext.request.contextPath}/publicar-articulo" class="${paginaPublicar ? 'nav-active' : 'nav-inactive'}">
      <i class="bi bi-plus-lg"></i> Publicar
    </a>

    <%-- ==========================================
         CAMPANA DE NOTIFICACIONES DINÁMICA
         ========================================== --%>
    <div class="header-notification-dropdown" id="dropdownNotificaciones">
      <button type="button" class="btn-bell" onclick="toggleBandejaNotificaciones(event)" title="Notificaciones">
        <i class="bi bi-bell-fill"></i>
        <span class="notification-badge" id="badgeCount" style="display: none;">0</span>
      </button>

      <div class="notification-tray" id="trayNotificaciones">
        <div class="tray-header">
          <h4>Notificaciones</h4>
          <a href="javascript:void(0)" onclick="marcarTodasLeidas()">Marcar como leídas</a>
        </div>

        <%-- Contenedor dinámico: se llena mediante AJAX desde Oracle --%>
        <div class="tray-list" id="listaNotificacionesDinamica">
          <p style="padding: 15px; text-align: center; color: #888; font-size: 13px; margin: 0;">Cargando notificaciones...</p>
        </div>

      </div>
    </div>

    <%-- CONTENEDOR DEL PERFIL Y LA CAJA FLOTANTE --%>
    <div class="caja-flotante-perfil">

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

      <div class="menu-blanco-desplegable">
        <a href="${pageContext.request.contextPath}/mi-perfil">
          <i class="bi bi-person"></i> Ver perfil
        </a>

        <a href="${pageContext.request.contextPath}/logout" class="opcion-salir">
          <i class="bi bi-box-arrow-right"></i> Cerrar sesión
        </a>
      </div>

    </div>
  </nav>

  <!-- ==========================================
     MODAL UNIVERSAL DE ALERTAS MUA (SIN ALERT)
     ========================================== -->
  <div class="modal-overlay" id="muaModalUniversal" style="display: none;">
    <div class="floating-modal confirm-modal" style="max-width: 400px; text-align: center;">
      <div id="muaModalIcono" style="font-size: 44px; margin-bottom: 12px;"></div>
      <h2 id="muaModalTitulo" style="margin-bottom: 8px;">Notificación</h2>
      <p id="muaModalMensaje" style="color: #555; font-size: 14px; line-height: 1.4; margin-bottom: 22px;">Mensaje del sistema</p>
      <div class="modal-actions" style="display: flex; justify-content: center;">
        <button type="button" class="btn btn-primary" id="muaModalBtnAceptar" style="background: #7c3f1d; border: none; min-width: 130px; border-radius: 10px; padding: 10px;">
          Aceptar
        </button>
      </div>
    </div>
  </div>
</header>

<script src="${pageContext.request.contextPath}/static/js/header.js"></script>