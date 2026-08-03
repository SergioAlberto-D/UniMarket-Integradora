<%--
  Created by IntelliJ IDEA.
  User: sheuko
  Date: 7/28/26
  Time: 9:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Perfil de ${fn:escapeXml(vendedor.nombre)} - MUA</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" href="${pageContext.request.contextPath}/static/img/logoMUA.png" type="image/png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bi_s/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/perfil-vendedor.css">

</head>
<body>

<jsp:include page="components/header.jsp" />

<main class="profile-container">

    <%-- SE ELIMINÓ EL ENCABEZADO SUPERIOR <header class="profile-header"> --%>

    <section class="profile-card">
        <div class="user-info-section">
            <div class="user-avatar">
                <c:choose>
                    <c:when test="${not empty vendedor.fotoPerfil}">
                        <img src="${pageContext.request.contextPath}/${vendedor.fotoPerfil}" alt="" onerror="this.style.display='none'; this.parentNode.innerHTML='<i class=\'bi bi-person-fill\'></i>';">
                    </c:when>
                    <c:otherwise>
                        <i class="bi bi-person-fill"></i>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="user-details">
                <h2>${fn:escapeXml(vendedor.nombre)}</h2>
                <ul>
                    <li><i class="bi bi-envelope"></i> ${vendedor.correoInstitucional}</li>
                    <li><i class="bi bi-mortarboard"></i> ${nombreDivision}</li>
                    <li><i class="bi bi-telephone"></i> ${not empty vendedor.numeroCelular ? vendedor.numeroCelular : 'No registrado'}</li>
                </ul>
                <div class="rating-badge">
                    <i class="bi bi-star-fill text-warning"></i> ${promedio} puntuacion
                </div>
            </div>
        </div>

        <div class="stats-section">
            <div class="stat-box">
                <i class="bi bi-box-seam"></i>
                <span class="stat-number">${articulosPublicados}</span>
                <span class="stat-label">Artículos<br>publicados</span>
            </div>
            <div class="stat-box">
                <i class="bi bi-arrow-left-right"></i>
                <span class="stat-number">0</span>
                <span class="stat-label">Transacciones<br>completadas</span>
            </div>
            <div class="stat-box">
                <i class="bi bi-chat-square-text"></i>
                <span class="stat-number">${totalOpiniones}</span>
                <span class="stat-label">Comentarios<br>recibidos</span>
            </div>
            <div class="stat-box">
                <i class="bi bi-star-fill"></i>
                <span class="stat-number">${promedio}</span>
                <span class="stat-label">Calificación<br>promedio</span>
            </div>
        </div>
    </section>

    <section class="reviews-card">
        <div class="reviews-header">
            <h2>Reseñas de este vendedor</h2>
            <c:if test="${not empty sessionScope.usuario and sessionScope.usuario.idUsuario ne vendedor.idUsuario}">
                <button class="btn-comentar" onclick="abrirModalComentario()">Comentar <i class="bi bi-plus"></i></button>
            </c:if>
        </div>

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
                            <div class="comment-item-header">
                                <div class="comment-user">
                                    <div class="c-avatar">
                                        <c:choose>
                                            <c:when test="${not empty comentario.fotoRemitente}">
                                                <img src="${pageContext.request.contextPath}/${comentario.fotoRemitente}" alt="" style="width:100%; height:100%; border-radius:50%; object-fit:cover;" onerror="this.style.display='none'; this.parentNode.innerHTML='<i class=\'bi bi-person-fill\'></i>';">
                                            </c:when>
                                            <c:otherwise>
                                                <i class="bi bi-person-fill"></i>
                                            </c:otherwise>
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

<%-- MODAL: AGREGAR COMENTARIO --%>
<div class="modal-comentario-overlay" id="modalComentar">
    <div class="modal-comentario-card">
        <h3>Agregar comentario</h3>
        <p class="sub">Puedes agregar un comentario de valoración hacia este vendedor.</p>

        <%-- INFORMACIÓN DEL USUARIO ACTIVO (QUIEN COMENTA) --%>
        <div class="commenting-user-info">
            <div class="c-avatar">
                <c:choose>
                    <c:when test="${not empty sessionScope.usuario.fotoPerfil}">
                        <img src="${pageContext.request.contextPath}/${sessionScope.usuario.fotoPerfil}" alt="" style="width:100%; height:100%; object-fit:cover;">
                    </c:when>
                    <c:otherwise>
                        ${fn:substring(sessionScope.usuario.nombre, 0, 2)}
                    </c:otherwise>
                </c:choose>
            </div>
            <strong>${fn:escapeXml(sessionScope.usuario.nombre)} ${fn:escapeXml(sessionScope.usuario.apellidoPaterno)}.</strong>
        </div>

        <%-- SELECTOR INTERACTIVO DE ESTRELLAS (Inician deshabilitadas/grises) --%>
        <div class="interactive-stars" id="interactiveStars">
            <i class="bi bi-star-fill" data-value="1"></i>
            <i class="bi bi-star-fill" data-value="2"></i>
            <i class="bi bi-star-fill" data-value="3"></i>
            <i class="bi bi-star-fill" data-value="4"></i>
            <i class="bi bi-star-fill" data-value="5"></i>
        </div>

        <%-- INPUT OCULTO CON EL VALOR SELECCIONADO (Inicia en 0) --%>
        <input type="hidden" id="calificacionSeleccionada" value="0">

        <%-- CAJA DE COMENTARIO --%>
        <textarea id="textoComentarioInput" class="textarea-comment" placeholder="Aquí va tu comentario. Recuerda ser respetuoso con el vendedor y describir con claridad tu experiencia de compra..."></textarea>

        <div class="modal-buttons">
            <button type="button" class="btn-cancel-modal" onclick="cerrarModalComentario()">Cancelar</button>
            <button type="button" class="btn-submit-modal" onclick="enviarComentario()">Comentar</button>
        </div>
    </div>
</div>

<script>
    let calificacionActual = 0; // Empieza en 0 (sin estrellas seleccionadas)

    // Abrir / Cerrar el Modal
    function abrirModalComentario() {
        document.getElementById('modalComentar').style.display = 'flex';
    }
    function cerrarModalComentario() {
        document.getElementById('modalComentar').style.display = 'none';
    }

    // Interactividad en las Estrellas
    const contenedorEstrellas = document.getElementById('interactiveStars');
    if (contenedorEstrellas) {
        const estrellas = contenedorEstrellas.querySelectorAll('i');

        estrellas.forEach(estrella => {
            // Hover momentáneo: aplica el color tenue (.hovered) sólo a las que están por encima del mouse
            estrella.addEventListener('mouseover', function() {
                const valHover = parseInt(this.getAttribute('data-value'));
                estrellas.forEach(e => {
                    const val = parseInt(e.getAttribute('data-value'));
                    e.classList.toggle('hovered', val <= valHover);
                });
            });

            // Fijar selección al hacer click
            estrella.addEventListener('click', function() {
                calificacionActual = parseInt(this.getAttribute('data-value'));
                document.getElementById('calificacionSeleccionada').value = calificacionActual;
                estrellas.forEach(e => {
                    const val = parseInt(e.getAttribute('data-value'));
                    e.classList.toggle('active', val <= calificacionActual);
                });
            });
        });

        // Limpiar el hover al salir del contenedor
        contenedorEstrellas.addEventListener('mouseleave', function() {
            estrellas.forEach(e => e.classList.remove('hovered'));
        });
    }

    // Enviar comentario vía Ajax
    function enviarComentario() {
        const comentario = document.getElementById('textoComentarioInput').value.trim();
        const calificacion = parseInt(document.getElementById('calificacionSeleccionada').value);
        const matriculaReceptor = "${vendedor.idUsuario}";

        if (calificacion === 0) {
            mostrarModalMUA("Por favor, selecciona una puntuación en estrellas antes de comentar.", 'error');
            return;
        }
        if (!comentario) {
            mostrarModalMUA("Por favor, redacta un comentario antes de continuar.", 'error');
            return;
        }

        const datos = new URLSearchParams();
        datos.append('matriculaReceptor', matriculaReceptor);
        datos.append('comentario', comentario);
        datos.append('calificacion', calificacion);

        fetch('${pageContext.request.contextPath}/comentar-vendedor', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: datos
        })
            .then(response => response.json())
            .then(data => {
                if (data.exito) {
                    cerrarModalComentario();
                    mostrarModalMUA("¡Comentario publicado con éxito!", 'exito', () => {
                        location.reload();
                    });
                } else {
                    // CORRECCIÓN: Cerramos el modal de comentario antes de mostrar el error del servidor
                    cerrarModalComentario();
                    mostrarModalMUA(data.mensaje || "Error al registrar el comentario.", 'error');
                }
            })
            .catch(error => {
                console.error("Error en la solicitud:", error);
                cerrarModalComentario();
                mostrarModalMUA("No se pudo conectar con el servidor.", 'error');
            });
    }
</script>

</body>
</html>