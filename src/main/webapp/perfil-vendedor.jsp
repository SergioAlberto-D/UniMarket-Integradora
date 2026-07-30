<%--
  Created by IntelliJ IDEA.
  User: sheuko
  Date: 7/28/26
  Time: 9:30 PM
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

    <header class="profile-header">
        <h1>Perfil de usuario</h1>
        <p>Consulta y administra tu información personal</p>
    </header>

    <section class="profile-card">
        <div class="user-info-section">
            <div class="user-avatar">
                <c:choose>
                    <c:when test="${not empty vendedor.fotoPerfil}">
                        <img src="${pageContext.request.contextPath}/${vendedor.fotoPerfil}" alt="Avatar de ${vendedor.nombre}">
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
                    <i class="bi bi-star-fill"></i> ${promedio} puntuacion
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
            <button class="btn-comentar" onclick="abrirModalComentario()">Comentar <i class="bi bi-plus"></i></button>
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
                                <span class="c-date">Reciente</span>
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

<script>
    function abrirModalComentario() {
        alert("Aquí abriremos el formulario para que el usuario escriba su reseña (próximo paso).");
    }
</script>

</body>
</html>