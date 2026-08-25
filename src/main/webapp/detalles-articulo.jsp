<%--
  DOCUMENTACIÓN TÉCNICA — MUA
  Archivo: src/main/webapp/detalles-articulo.jsp
  Propósito: Recurso de vista JSP para el módulo detalles-articulo. Integra HTML, JSTL y/o expresiones JSP según el contenido fuente.
  Integración: la vista recibe datos desde Servlets mediante request/session y utiliza recursos CSS/JS del proyecto.
  Created by IntelliJ IDEA.
  User: sergio
  Date: 7/28/26
  Time: 9:00 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>${fn:escapeXml(articulo.nombre)} - MUA</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" href="${pageContext.request.contextPath}/static/img/logoMUA.png" type="image/png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bi_s/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/detalles-articulo.css">
</head>
<body>

<jsp:include page="components/header.jsp" />

<main class="details-container">
    <div class="back-badge-container">
        <a href="${pageContext.request.contextPath}/inicio" class="back-badge">Regresar</a>
    </div>

    <section class="main-card">
        <div class="product-gallery">
            <!-- Agregamos cursor: pointer y el evento onclick para abrir el Modal -->
            <div class="main-image-box" onclick="abrirModalZoom()" style="cursor: zoom-in;" title="Haz clic para ampliar">
                <c:choose>
                    <c:when test="${not empty imagenes}">
                        <img id="imagenPrincipal" src="${pageContext.request.contextPath}/${imagenes[0].urlImagen}" alt="Imagen del producto">
                    </c:when>
                    <c:otherwise>
                        <div class="placeholder-img"><i class="bi bi-image"></i></div>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="thumbnails">
                <%-- Agregamos status.index al onclick para saber qué número de imagen es --%>
                <c:forEach var="img" items="${imagenes}" varStatus="status">
                    <div class="thumb ${status.first ? 'active' : ''}" onclick="cambiarImagen(this, '${pageContext.request.contextPath}/${img.urlImagen}', ${status.index})">
                        <img src="${pageContext.request.contextPath}/${img.urlImagen}" alt="miniatura">
                    </div>
                </c:forEach>
            </div>
        </div>

        <div class="product-info">
            <h1>${fn:escapeXml(articulo.nombre)}</h1>
            <span class="category-tag">${nombreCategoria}</span>

            <h2 class="section-title">Descripción</h2>
            <p class="description-text">${fn:escapeXml(articulo.descripcion)}</p>

            <div class="seller-box">
                <a href="${pageContext.request.contextPath}/perfil-vendedor?matricula=${articulo.idUsuarioFk}" class="seller-box">
                    <span class="seller-label">vendedor</span>

                    <div class="seller-profile">
                        <div class="seller-avatar">
                            <c:choose>
                                <c:when test="${not empty articulo.fotoVendedor}">
                                    <img src="${pageContext.request.contextPath}/${articulo.fotoVendedor}"
                                         alt=""
                                         onerror="this.style.display='none'; this.parentNode.innerHTML='${fn:substring(articulo.nombreUsuario, 0, 2)}';">
                                </c:when>
                                <c:otherwise>
                                    ${fn:substring(articulo.nombreUsuario, 0, 2)}
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div class="seller-details">
                            <strong>${fn:escapeXml(articulo.nombreUsuario)}</strong>
                            <div class="seller-stars">
                                <i class="bi bi-star-fill text-warning"></i>
                                <span>${promedio}</span>
                            </div>
                        </div>
                    </div>
                </a>
            </div>

            <div class="action-buttons">
                <button class="btn-ofertar" onclick="document.getElementById('modalOferta').style.display = 'flex';">Ofertar</button>
                <button type="button" class="btn-comprar" onclick="abrirModalCompra()" style="border: none; cursor: pointer;">Comprar</button>
            </div>
        </div>
    </section>

    <section class="reviews-card">
        <h2>Reseñas de este vendedor</h2>

        <div class="reviews-summary">
            <div class="global-rating">
                <span class="big-score">${promedio}</span>
                <div class="stars">
                    <c:forEach begin="1" end="5" var="i">
                        <i class="bi bi-star-fill ${i <= promedio + 0.5 ? 'text-warning' : 'text-muted'}"></i>
                    </c:forEach>
                </div>
                <span class="review-count">${totalOpiniones} opiniones</span>
            </div>

            <div class="progress-bars">
                <c:forEach begin="1" end="5" var="i" step="1">
                    <c:set var="estrella" value="${6 - i}" />
                    <div class="bar-row">
                        <span>${estrella}</span> <i class="bi bi-star-fill"></i>
                        <div class="bar-track"><div class="bar-fill" style="width: ${porcentajes[estrella]}%;"></div></div>
                    </div>
                </c:forEach>
            </div>
        </div>

        <div class="comments-list">
            <c:choose>
                <c:when test="${not empty comentarios}">
                    <c:forEach var="comentario" items="${comentarios}">
                        <article class="comment-item">
                            <div class="comment-header">
                                <div class="comment-user">
                                    <div class="c-avatar" style="background:#4b6bf5;">
                                        <c:choose>
                                            <c:when test="${not empty comentario.fotoRemitente}">
                                                <img src="${pageContext.request.contextPath}/${comentario.fotoRemitente}" style="width:100%; height:100%; border-radius:50%; object-fit:cover;">
                                            </c:when>
                                            <c:otherwise><i class="bi bi-person-fill text-white"></i></c:otherwise>
                                        </c:choose>
                                    </div>
                                    <strong>${fn:escapeXml(comentario.nombreRemitente)}</strong>
                                </div>
                                <span class="c-date">${comentario.fechaFormateada}</span>
                            </div>
                            <div class="c-stars">
                                <c:forEach begin="1" end="5" var="i">
                                    <i class="bi ${i <= comentario.calificacion ? 'bi-star-fill text-warning' : 'bi-star text-muted'}"></i>
                                </c:forEach>
                            </div>
                            <p>${fn:escapeXml(comentario.comentario)}</p>
                        </article>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p style="color: #666; font-style: italic;">Este vendedor aún no tiene reseñas.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </section>
</main>

<%-- MODAL PARA ZOOM DE IMAGEN --%>
<div id="imageModal" class="modal-overlay" onclick="cerrarModalZoom(event)">
    <span class="close-modal">&times;</span>
    <div class="modal-content">
        <img id="modalImage" src="" alt="Zoom" onmousemove="hacerZoom(event)" onmouseleave="resetearZoom()">
    </div>
</div>

<%-- MODAL FLOTANTE: HAZ UNA OFERTA --%>
<div class="modal-overlay" id="modalOferta" style="display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); z-index: 2000; align-items: center; justify-content: center;">
    <div class="floating-modal" style="background: #fff; border-radius: 18px; width: 90%; max-width: 440px; padding: 24px; box-shadow: 0 10px 30px rgba(0,0,0,0.2); position: relative; text-align: left;">

        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; border-bottom: 1px solid #f0e6e1; padding-bottom: 12px;">
            <h3 style="margin: 0; font-size: 20px; font-weight: 800; color: #111;">Haz una oferta</h3>
            <button type="button" onclick="cerrarModalOferta()" style="background: none; border: none; font-size: 22px; cursor: pointer; color: #555;">&times;</button>
        </div>

        <%-- RESUMEN DEL PRODUCTO --%>
        <div style="background: #f7efe9; border: 1px solid #e8cdbd; border-radius: 12px; padding: 12px; display: flex; gap: 14px; align-items: center; margin-bottom: 20px;">
            <div style="width: 55px; height: 55px; border-radius: 8px; overflow: hidden; background: #e0d0c8; flex-shrink: 0;">
                <img src="${pageContext.request.contextPath}/${not empty imagenes ? imagenes[0].urlImagen : ''}" style="width: 100%; height: 100%; object-fit: cover;" alt="">
            </div>
            <div>
                <strong style="display: block; font-size: 14px; color: #111; margin-bottom: 3px;">${fn:escapeXml(articulo.nombre)}</strong>
                <span style="font-size: 13px; color: #555;">Precio actual: <strong style="color: #111;">$${articulo.precio} MXN</strong></span>
            </div>
        </div>

        <%-- INPUT DE OFERTA --%>
        <label style="display: block; font-size: 13px; font-weight: 700; color: #111; margin-bottom: 6px;">Tu oferta (MXN)</label>
        <input type="number" id="inputMontoOferta" placeholder="$00000.0000" min="1" step="1" style="width: 100%; height: 45px; border: none; background: #f6efe9; border-radius: 10px; padding: 0 14px; font-size: 15px; box-sizing: border-box; outline: none; margin-bottom: 8px;">
        <p style="font-size: 12px; color: #666; margin: 0 0 24px;">&#9432; El vendedor revisará tu oferta y podrá aceptarla o rechazarla.</p>

        <%-- BOTÓN DE ENVIAR --%>
        <div style="background: #f7efe9; padding: 12px; border-radius: 12px;">
            <button type="button" onclick="enviarOfertaAjax('${articulo.idArticulo}', '${articulo.idUsuarioFk}')" style="width: 100%; height: 44px; background: #7c3f1d; color: #fff; border: none; border-radius: 10px; font-weight: 700; font-size: 15px; cursor: pointer;">Enviar oferta</button>
        </div>

    </div>
</div>

<%-- MODAL FLOTANTE: CONFIRMA TU COMPRA --%>
<div class="modal-overlay" id="modalCompra" style="display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); z-index: 2000; align-items: center; justify-content: center;">
    <div class="floating-modal" style="background: #fff; border-radius: 18px; width: 90%; max-width: 480px; padding: 24px; box-shadow: 0 10px 30px rgba(0,0,0,0.2); position: relative; text-align: left;">

        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; border-bottom: 1px solid #f0e6e1; padding-bottom: 12px;">
            <h3 style="margin: 0; font-size: 20px; font-weight: 800; color: #111;">Confirma tu compra</h3>
            <button type="button" onclick="cerrarModalCompra()" style="background: none; border: none; font-size: 22px; cursor: pointer; color: #555;">&times;</button>
        </div>

        <div style="background: #f7efe9; border: 1px solid #e8cdbd; border-radius: 12px; padding: 12px; display: flex; gap: 14px; align-items: center; margin-bottom: 20px;">
            <div style="width: 65px; height: 65px; border-radius: 8px; overflow: hidden; background: #e0d0c8; flex-shrink: 0;">
                <img src="${pageContext.request.contextPath}/${not empty imagenes ? imagenes[0].urlImagen : ''}" style="width: 100%; height: 100%; object-fit: cover;" alt="">
            </div>
            <div>
                <strong style="display: block; font-size: 15px; color: #111; margin-bottom: 3px;">${fn:escapeXml(articulo.nombre)}</strong>
                <span style="font-size: 13px; color: #555;">Precio actual: <strong style="color: #9a3d1e;">$${articulo.precio} MXN</strong></span>
            </div>
        </div>

        <label style="display: block; font-size: 13px; font-weight: 700; color: #111; margin-bottom: 6px;">Escribe un mensaje para el Vendedor (Opcional)</label>
        <textarea id="inputMensajeComprador" placeholder="Escribe aquí tu mensaje....." style="width: 100%; height: 110px; border: 1px solid #e8cdbd; background: #fcf9f7; border-radius: 10px; padding: 12px; font-size: 14px; box-sizing: border-box; outline: none; resize: none; margin-bottom: 20px;"></textarea>

        <label style="display: block; font-size: 13px; font-weight: 700; color: #111; margin-bottom: 10px;">¿Cómo quieres contactar al vendedor?</label>

        <div style="display: flex; gap: 10px;">
            <button type="button" onclick="ejecutarCompraAjax('whatsapp', '${articulo.idArticulo}')" style="flex: 1; height: 48px; background: #25D366; color: #fff; border: none; border-radius: 10px; font-weight: 700; font-size: 14px; cursor: pointer; box-shadow: 0 4px 12px rgba(37, 211, 102, 0.25); display: flex; align-items: center; justify-content: center; gap: 8px;">
                <i class="bi bi-whatsapp"></i> WhatsApp
            </button>
            <button type="button" onclick="ejecutarCompraAjax('gmail', '${articulo.idArticulo}')" style="flex: 1; height: 48px; background: #7c3f1d; color: #fff; border: none; border-radius: 10px; font-weight: 700; font-size: 14px; cursor: pointer; box-shadow: 0 4px 12px rgba(124, 63, 29, 0.25); display: flex; align-items: center; justify-content: center; gap: 8px;">
                <i class="bi bi-envelope"></i> Gmail
            </button>
        </div>

    </div>
</div>

<script src="${pageContext.request.contextPath}/static/js/detalles-articulo.js"></script>

</body>
</html>
