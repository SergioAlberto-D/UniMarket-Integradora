<%--
  Created by IntelliJ IDEA.
  User: sheuko
  Date: 7/28/26
  Time: 9:00 PM
  To change this template use File | Settings | File Templates.
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
            <div class="main-image-box" onclick="abrirModal()" style="cursor: zoom-in;" title="Haz clic para ampliar">
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
                                    <img src="${pageContext.request.contextPath}/${articulo.fotoVendedor}" alt="Avatar">
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
                <button class="btn-ofertar">Ofertar</button>
                <button class="btn-comprar">Comprar</button>
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

<%-- MODAL PARA ZOOM DE IMAGEN --%>
<div id="imageModal" class="modal-overlay" onclick="cerrarModal(event)">
    <span class="close-modal">&times;</span>
    <div class="modal-content">
        <img id="modalImage" src="" alt="Zoom" onmousemove="hacerZoom(event)" onmouseleave="resetearZoom()">
    </div>
</div>

<script>
    // --- VARIABLES GLOBALES ---
    let indiceImagenActual = 0;
    const miniaturas = document.querySelectorAll(".thumb");
    const totalImagenes = miniaturas.length;
    let carruselIntervalo;

    // --- FUNCIONES DEL CARRUSEL ---
    function cambiarImagen(elemento, rutaImagen, indice) {
        document.getElementById("imagenPrincipal").src = rutaImagen;
        miniaturas.forEach(m => m.classList.remove("active"));
        elemento.classList.add("active");

        if(indice !== undefined) {
            indiceImagenActual = indice;
        }
    }

    function avanzarCarrusel() {
        if (totalImagenes <= 1) return;
        indiceImagenActual = (indiceImagenActual + 1) % totalImagenes;
        // Simulamos un click en la siguiente miniatura
        miniaturas[indiceImagenActual].click();
    }

    function iniciarCarrusel() {
        if (totalImagenes > 1) {
            carruselIntervalo = setInterval(avanzarCarrusel, 15000); // Cambia cada 3.5 segundos
        }
    }

    function detenerCarrusel() {
        clearInterval(carruselIntervalo);
    }

    // Iniciamos el carrusel y lo pausamos si el usuario tiene el mouse encima de la galería
    iniciarCarrusel();
    const galeria = document.querySelector('.product-gallery');
    if (galeria) {
        galeria.addEventListener('mouseenter', detenerCarrusel);
        galeria.addEventListener('mouseleave', iniciarCarrusel);
    }

    // --- FUNCIONES DEL MODAL Y ZOOM ---
    function abrirModal() {
        const modal = document.getElementById("imageModal");
        const modalImg = document.getElementById("modalImage");
        const imagenActualSrc = document.getElementById("imagenPrincipal").src;

        modalImg.src = imagenActualSrc;
        modal.style.display = "flex";
        detenerCarrusel(); // Pausamos el carrusel al abrir el zoom
    }

    function cerrarModal(event) {
        // Cierra solo si le dan clic al fondo negro o a la "X"
        if (event.target.id === "imageModal" || event.target.className === "close-modal") {
            document.getElementById("imageModal").style.display = "none";
            resetearZoom();
            iniciarCarrusel(); // Reanudamos el carrusel
        }
    }

    function hacerZoom(event) {
        const img = document.getElementById("modalImage");
        // Calculamos la posición del ratón respecto al tamaño de la imagen
        const x = (event.offsetX / img.offsetWidth) * 100;
        const y = (event.offsetY / img.offsetHeight) * 100;

        // Movemos el punto de origen de la transformación (el "foco" de la lupa)
        img.style.transformOrigin = `${x}% ${y}%`;
        img.style.transform = "scale(2.5)"; // Nivel de zoom (2.5x)
    }

    function resetearZoom() {
        const img = document.getElementById("modalImage");
        img.style.transformOrigin = "center center";
        img.style.transform = "scale(1)";
    }
</script>

</body>
</html>