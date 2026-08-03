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
      <a href="${pageContext.request.contextPath}/mis-articulos" class="${paginaMisArticulos ? 'nav-active' : 'nav-inactive'}">
        <i class="bi bi-archive-fill"></i> Mis artículos
      </a>
    </c:if>

    <a href="${pageContext.request.contextPath}/publicar-articulo.jsp" class="${paginaPublicar ? 'nav-active' : 'nav-inactive'}">
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

  <script>
    function mostrarModalMUA(mensaje, tipo = 'info', callback = null) {
      const modal = document.getElementById('muaModalUniversal');
      const titulo = document.getElementById('muaModalTitulo');
      const texto = document.getElementById('muaModalMensaje');
      const icono = document.getElementById('muaModalIcono');
      const btnAceptar = document.getElementById('muaModalBtnAceptar');

      if (!modal) return;

      if (tipo === 'exito') {
        titulo.textContent = '¡Éxito!';
        icono.innerHTML = '<i class="bi bi-check-circle-fill" style="color: #28a745;"></i>';
      } else if (tipo === 'error') {
        titulo.textContent = '¡Atención!';
        icono.innerHTML = '<i class="bi bi-exclamation-triangle-fill" style="color: #d9534f;"></i>';
      } else {
        titulo.textContent = 'Notificación';
        icono.innerHTML = '<i class="bi bi-info-circle-fill" style="color: #7c3f1d;"></i>';
      }

      texto.textContent = mensaje;
      btnAceptar.onclick = function() {
        modal.classList.remove('show-modal');
        if (typeof callback === 'function') callback();
      };

      modal.classList.add('show-modal');
    }
  </script>
</header>

<%-- ==========================================
     SCRIPTS GLOBALES DEL HEADER (TOAST + CAMPANA AJAX)
     ========================================== --%>
<script>
  // 1. LÓGICA DE LA BANDEJA DE NOTIFICACIONES
  function toggleBandejaNotificaciones(e) {
    e.stopPropagation();
    const tray = document.getElementById('trayNotificaciones');
    if (tray) {
      tray.classList.toggle('show');
    }
  }

  // Cerrar la bandeja si hacen clic en cualquier otro lado
  window.addEventListener('click', function(e) {
    const dropdown = document.getElementById('dropdownNotificaciones');
    const tray = document.getElementById('trayNotificaciones');
    if (dropdown && tray && !dropdown.contains(e.target)) {
      tray.classList.remove('show');
    }
  });

  // --- CARGAR NOTIFICACIONES REALES DESDE ORACLE VÍA AJAX ---
  function cargarNotificacionesReales() {
    fetch('${pageContext.request.contextPath}/api/notificaciones')
            .then(response => response.json())
            .then(data => {
              const contenedor = document.getElementById('listaNotificacionesDinamica');
              const badge = document.getElementById('badgeCount');

              if (!contenedor) return;

              if (!data || data.length === 0) {
                contenedor.innerHTML = '<p style="padding: 20px; text-align: center; color: #888; font-size: 13px; margin: 0;">No tienes notificaciones pendientes.</p>';
                if (badge) badge.style.display = 'none';
                return;
              }

              // Mostrar contador en el globito rojo
              if (badge) {
                badge.textContent = data.length;
                badge.style.display = 'inline-block';
              }

              // Pintar cada notificación real en el menú
              let html = '';
              data.forEach(item => {
                const colorIcono = item.tipo === 'OFERTA' ? 'bg-brown' : 'bg-green';
                const icono = item.tipo === 'OFERTA' ? 'bi-tag-fill' : 'bi-check-lg';

                html += `
            <div class="tray-item unread">
              <div class="tray-icon \${colorIcono}"><i class="bi \${icono}"></i></div>
              <div class="tray-content">
                <p>\${item.mensaje}</p>
                <span class="tray-time">\${item.tiempo}</span>
              </div>
            </div>
          `;
              });
              contenedor.innerHTML = html;
            })
            .catch(error => {
              console.error("Error al cargar notificaciones:", error);
              const contenedor = document.getElementById('listaNotificacionesDinamica');
              if (contenedor) {
                contenedor.innerHTML = '<p style="padding: 15px; text-align: center; color: #888; font-size: 13px; margin: 0;">Sin notificaciones recientes.</p>';
              }
            });
  }

  // Marcar como leídas en la base de datos
  function marcarTodasLeidas() {
    fetch('${pageContext.request.contextPath}/api/notificaciones/marcar-leidas', { method: 'POST' })
            .then(() => {
              const unreadItems = document.querySelectorAll('.tray-item.unread');
              unreadItems.forEach(item => item.classList.remove('unread'));
              const badge = document.getElementById('badgeCount');
              if (badge) badge.style.display = 'none';
            })
            .catch(err => console.error("Error al marcar como leídas:", err));
  }

  // Ejecutar carga dinámica al abrir cualquier página
  document.addEventListener('DOMContentLoaded', cargarNotificacionesReales);

  // 2. SISTEMA DE TOAST FLOTANTE (ESQUINA SUPERIOR DERECHA)
  function mostrarToast(mensaje, tipo = 'info', titulo = 'Notificación') {
    let container = document.getElementById('muaToastContainer');
    if (!container) {
      container = document.createElement('div');
      container.id = 'muaToastContainer';
      container.className = 'mua-toast-container';
      document.body.appendChild(container);
    }

    const iconos = {
      exito: 'bi-check-circle-fill',
      error: 'bi-exclamation-triangle-fill',
      info: 'bi-info-circle-fill'
    };

    const toast = document.createElement('div');
    toast.className = `mua-toast \${tipo}`;
    toast.innerHTML = `
        <i class="bi \${iconos[tipo] || iconos.info}"></i>
        <div class="mua-toast-text">
            <strong>\${titulo}</strong>
            <span>\${mensaje}</span>
        </div>
    `;

    container.appendChild(toast);

    setTimeout(() => {
      toast.style.animation = 'toastSlideOut 0.3s forwards';
      setTimeout(() => {
        toast.remove();
      }, 300);
    }, 4000);
  }
</script>