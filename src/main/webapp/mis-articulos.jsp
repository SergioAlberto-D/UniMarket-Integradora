<%--
  Created by IntelliJ IDEA.
  User: sheuko
  Date: 8/1/26
  Time: 9:55 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mis artículos - MUA</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" href="${pageContext.request.contextPath}/static/img/logoMUA.png" type="image/png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bi_s/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/mis-articulos.css">
</head>
<body style="background-color: #f7efe9;">

<jsp:include page="components/header.jsp" />

<main class="mis-articulos-container">
    <div class="header-section">
        <div class="title-box">
            <div class="title-icon"><i class="bi bi-box-seam-fill"></i></div>
            <div class="title-text">
                <h1>Mis artículos</h1>
                <p>Aquí podrás ver los artículos que has creado, editarlos y poder eliminarlos</p>
            </div>
        </div>

        <!-- SWITCH DE PESTAÑAS -->
        <div class="tabs-switch">
            <button type="button" class="tab-btn active" onclick="cambiarPestana('disponibles', this)">Disponibles</button>
            <button type="button" class="tab-btn" onclick="cambiarPestana('proceso', this)">En proceso</button>
        </div>
    </div>

    <!-- PESTAÑA 1: ARTÍCULOS DISPONIBLES (NO ESTÁN EN TRANSACCIÓN PENDIENTE) -->
    <section id="tab-disponibles" class="articulos-list">
        <c:choose>
            <c:when test="${not empty disponibles}">
                <c:forEach var="art" items="${disponibles}">
                    <div class="articulo-card">
                        <div class="articulo-info">
                            <div class="articulo-img">
                                <c:if test="${not empty art.imagenPrincipal}">
                                    <img src="${pageContext.request.contextPath}/${art.imagenPrincipal}" alt="Producto">
                                </c:if>
                            </div>
                            <div class="articulo-text">
                                <h3>${fn:escapeXml(art.nombre)}</h3>
                                <span class="precio">$${art.precio}</span>
                            </div>
                        </div>

                        <div class="menu-accion-wrapper">
                            <button type="button" class="btn-hamburguesa" onclick="toggleMenuAction(event, 'menu-disp-${art.idArticulo}')">
                                <i class="bi bi-list"></i>
                            </button>
                            <div class="dropdown-menu-acciones" id="menu-disp-${art.idArticulo}">
                                <a href="${pageContext.request.contextPath}/editar-articulo?id=${art.idArticulo}">Editar</a>
                                <button type="button" onclick="confirmarEliminacion(${art.idArticulo})">Eliminar</button>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p style="text-align: center; color: #777; padding: 40px 0;">No tienes artículos disponibles por el momento.</p>
            </c:otherwise>
        </c:choose>
    </section>

    <!-- PESTAÑA 2: ARTÍCULOS EN PROCESO (EN TABLA TRANSACCION COMO PENDIENTE) -->
    <section id="tab-proceso" class="articulos-list" style="display: none;">
        <c:choose>
            <c:when test="${not empty enProceso}">
                <c:forEach var="art" items="${enProceso}">
                    <div class="articulo-card">
                        <div class="articulo-info">
                            <div class="articulo-img">
                                <c:if test="${not empty art.imagenPrincipal}">
                                    <img src="${pageContext.request.contextPath}/${art.imagenPrincipal}" alt="Producto">
                                </c:if>
                            </div>
                            <div class="articulo-text">
                                <h3>${fn:escapeXml(art.nombre)}</h3>
                                <span class="precio">$${art.precio}</span>
                            </div>
                        </div>

                        <div class="menu-accion-wrapper">
                            <button type="button" class="btn-hamburguesa" onclick="toggleMenuAction(event, 'menu-proc-${art.idArticulo}')">
                                <i class="bi bi-list"></i>
                            </button>
                            <div class="dropdown-menu-acciones" id="menu-proc-${art.idArticulo}">
                                <a href="${pageContext.request.contextPath}/editar-articulo.jsp?id=${art.idArticulo}">Editar</a>
                                <button type="button" onclick="actualizarEstadoTransaccion(${art.idArticulo}, 'COMPLETADO')">Vendido</button>
                                <button type="button" onclick="actualizarEstadoTransaccion(${art.idArticulo}, 'CANCELADO')">No vendido</button>
                                <button type="button" onclick="confirmarEliminacion(${art.idArticulo})">Eliminar</button>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p style="text-align: center; color: #777; padding: 40px 0;">No tienes artículos en proceso de venta en este momento.</p>
            </c:otherwise>
        </c:choose>
    </section>
</main>

<script>
    // ALTERNAR PESTAÑAS
    function cambiarPestana(tipo, btn) {
        document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
        btn.classList.add('active');

        document.getElementById('tab-disponibles').style.display = (tipo === 'disponibles') ? 'flex' : 'none';
        document.getElementById('tab-proceso').style.display = (tipo === 'proceso') ? 'flex' : 'none';
    }

    // ABRIR / CERRAR EL MENÚ HAMBURGUESA
    function toggleMenuAction(e, menuId) {
        e.stopPropagation();
        document.querySelectorAll('.dropdown-menu-acciones').forEach(menu => {
            if (menu.id !== menuId) menu.classList.remove('show');
        });
        const targetMenu = document.getElementById(menuId);
        if (targetMenu) targetMenu.classList.toggle('show');
    }

    // CERRAR LOS MENÚS AL HACER CLIC FUERA
    window.addEventListener('click', function() {
        document.querySelectorAll('.dropdown-menu-acciones').forEach(menu => menu.classList.remove('show'));
    });

    // ACTUALIZAR ESTADO (VENDIDO / NO VENDIDO)
    function actualizarEstadoTransaccion(idArticulo, nuevoEstado) {
        const datos = new URLSearchParams({ idArticulo, estado: nuevoEstado });

        fetch('${pageContext.request.contextPath}/actualizar-transaccion', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: datos
        })
            .then(res => res.json())
            .then(data => {
                if (data.exito) {
                    mostrarModalMUA(data.mensaje, 'exito', () => location.reload());
                } else {
                    mostrarModalMUA(data.mensaje || "Ocurrió un error al actualizar el estado.", 'error');
                }
            })
            .catch(() => mostrarModalMUA("Error de conexión con el servidor.", 'error'));
    }

    // ELIMINAR ARTÍCULO
    function confirmarEliminacion(idArticulo) {
        mostrarModalMUA("¿Estás seguro de que deseas eliminar este artículo?", "info", () => {
            fetch('${pageContext.request.contextPath}/eliminar-articulo?id=' + idArticulo, { method: 'POST' })
                .then(res => res.json())
                .then(data => {
                    if (data.exito) {
                        mostrarModalMUA("Artículo eliminado correctamente.", 'exito', () => location.reload());
                    } else {
                        mostrarModalMUA(data.mensaje || "No se pudo eliminar el artículo.", 'error');
                    }
                });
        });
    }
</script>
<!-- MODAL FLOTANTE: ¿QUÉ HACER DESPUÉS DE VENDER? -->
<div class="modal-overlay" id="modalDecisionVenta" style="display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); z-index: 3000; align-items: center; justify-content: center;">
    <div class="floating-modal" style="background: #fff; border-radius: 18px; width: 90%; max-width: 440px; padding: 26px; box-shadow: 0 10px 30px rgba(0,0,0,0.25); text-align: center;">
        <div style="width: 60px; height: 60px; background: #e8cdbd; color: #7c3f1d; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 28px; margin: 0 auto 16px;">
            <i class="bi bi-check-lg"></i>
        </div>
        <h3 style="margin: 0 0 10px; font-size: 21px; font-weight: 800; color: #111;">¡Venta confirmada!</h3>
        <p style="font-size: 14px; color: #555; margin-bottom: 24px; line-height: 1.5;">
            El artículo ha sido marcado como vendido. ¿Deseas seguir vendiendo más unidades de este producto o prefieres quitarlo del catálogo?
        </p>
        <div style="display: flex; gap: 12px; flex-direction: column;">
            <button type="button" onclick="accionSeguirVendiendo()" style="width: 100%; height: 44px; background: #f3dad0; color: #7c3f1d; border: none; border-radius: 10px; font-weight: 700; font-size: 14px; cursor: pointer;">
                Seguir vendiéndolo (Conservar en catálogo)
            </button>
            <button type="button" onclick="accionQuitarCatalogo()" style="width: 100%; height: 44px; background: #7c3f1d; color: #fff; border: none; border-radius: 10px; font-weight: 700; font-size: 14px; cursor: pointer;">
                Quitar del catálogo (Eliminar artículo)
            </button>
        </div>
    </div>
</div>

<script>
    let articuloVendidoIdTemporal = null;

    // ALTERNAR PESTAÑAS
    function cambiarPestana(tipo, btn) {
        document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
        btn.classList.add('active');

        document.getElementById('tab-disponibles').style.display = (tipo === 'disponibles') ? 'flex' : 'none';
        document.getElementById('tab-proceso').style.display = (tipo === 'proceso') ? 'flex' : 'none';
    }

    // SI LA URL DICE ?tab=proceso, ABRIR LA PESTAÑA AUTOMÁTICAMENTE
    window.addEventListener('DOMContentLoaded', () => {
        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.get('tab') === 'proceso') {
            const btnProceso = document.querySelectorAll('.tab-btn')[1];
            if (btnProceso) cambiarPestana('proceso', btnProceso);
        }
    });

    // ABRIR / CERRAR EL MENÚ HAMBURGUESA
    function toggleMenuAction(e, menuId) {
        e.stopPropagation();
        document.querySelectorAll('.dropdown-menu-acciones').forEach(menu => {
            if (menu.id !== menuId) menu.classList.remove('show');
        });
        const targetMenu = document.getElementById(menuId);
        if (targetMenu) targetMenu.classList.toggle('show');
    }

    window.addEventListener('click', function() {
        document.querySelectorAll('.dropdown-menu-acciones').forEach(menu => menu.classList.remove('show'));
    });

    // ACTUALIZAR ESTADO DE LA TRANSACCIÓN
    function actualizarEstadoTransaccion(idArticulo, nuevoEstado) {
        const datos = new URLSearchParams({ idArticulo, estado: nuevoEstado });

        fetch('${pageContext.request.contextPath}/actualizar-transaccion', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: datos
        })
            .then(res => res.json())
            .then(data => {
                if (data.exito) {
                    if (nuevoEstado === 'COMPLETADO') {
                        // Si le dio a "Vendido", abrimos el modal de decisión en vez de recargar
                        articuloVendidoIdTemporal = idArticulo;
                        document.getElementById('modalDecisionVenta').style.display = 'flex';
                    } else {
                        mostrarModalMUA(data.mensaje, 'exito', () => location.reload());
                    }
                } else {
                    mostrarModalMUA(data.mensaje || "Ocurrió un error al actualizar el estado.", 'error');
                }
            })
            .catch(() => mostrarModalMUA("Error de conexión con el servidor.", 'error'));
    }

    // OPCIÓN 1: SEGUIR VENDIÉNDOLO (Al completar la transacción, vuelve a "Disponibles" al recargar)
    function accionSeguirVendiendo() {
        document.getElementById('modalDecisionVenta').style.display = 'none';
        location.reload();
    }

    // OPCIÓN 2: QUITAR DEL CATÁLOGO (Elimina el artículo y sus imágenes)
    function accionQuitarCatalogo() {
        if (!articuloVendidoIdTemporal) return;
        document.getElementById('modalDecisionVenta').style.display = 'none';

        fetch('${pageContext.request.contextPath}/eliminar-articulo?id=' + articuloVendidoIdTemporal, { method: 'POST' })
            .then(res => res.json())
            .then(data => {
                if (data.exito) {
                    mostrarModalMUA("Artículo eliminado del catálogo correctamente.", 'exito', () => location.reload());
                } else {
                    mostrarModalMUA(data.mensaje || "No se pudo eliminar el artículo.", 'error', () => location.reload());
                }
            });
    }

    // ELIMINAR ARTÍCULO DIRECTAMENTE
    function confirmarEliminacion(idArticulo) {
        mostrarModalMUA("¿Estás seguro de que deseas eliminar este artículo?", "info", () => {
            fetch('${pageContext.request.contextPath}/eliminar-articulo?id=' + idArticulo, { method: 'POST' })
                .then(res => res.json())
                .then(data => {
                    if (data.exito) {
                        mostrarModalMUA("Artículo eliminado correctamente.", 'exito', () => location.reload());
                    } else {
                        mostrarModalMUA(data.mensaje || "No se pudo eliminar el artículo.", 'error');
                    }
                });
        });
    }
</script>
</body>
</html>