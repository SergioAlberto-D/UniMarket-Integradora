<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar artículo - MUA</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" href="${pageContext.request.contextPath}/static/img/logoMUA.png" type="image/png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bi_s/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/publicar-articulo.css">
</head>
<body>

<jsp:include page="components/header.jsp" />

<main class="publish-page">

    <c:if test="${not empty error}">
        <div class="error-alert">
            <i class="bi bi-exclamation-triangle"></i>
            <span>${error}</span>
        </div>
    </c:if>

    <section class="publish-title">
        <div class="title-icon">
            <i class="bi bi-pencil-square"></i>
        </div>
        <div>
            <h1>Editar artículo</h1>
            <p>Modifica la información de tu producto. Si dejas los campos en blanco, se mantendrá la información anterior.</p>
        </div>
    </section>

    <form action="${pageContext.request.contextPath}/editar-articulo" method="post" enctype="multipart/form-data" class="publish-layout">

        <!-- ID oculto para identificar el registro a actualizar en el POST -->
        <input type="hidden" name="idArticulo" value="${articulo.idArticulo}">

        <section class="images-card">
            <div class="card-title">
                <div class="card-icon"><i class="bi bi-image"></i></div>
                <div>
                    <h2>Actualizar imágenes (Opcional)</h2>
                    <p>Si no adjuntas archivos, se conservarán tus imágenes anteriores</p>
                </div>
            </div>

            <label for="imagenes" class="upload-zone" id="dropZone">
                <div class="upload-icon">
                    <i class="bi bi-upload"></i>
                </div>
                <h3>Arrastra nuevas imágenes aquí</h3>
                <p>o selecciona del dispositivo para eliminar y sustituir las anteriores</p>
                <span class="btn-seleccionar">Seleccionar archivos</span>
            </label>

            <input id="imagenes" name="imagenes" type="file" accept="image/png, image/jpeg, image/jpg" multiple style="opacity: 0; position: absolute; z-index: -1; width: 1px; height: 1px;">

            <div class="image-help">
                <h3>Hasta un máximo de 3 imágenes</h3>
                <p>JPG o PNG</p>
            </div>

            <div class="preview-grid" id="previewGrid">
                <c:choose>
                    <c:when test="${not empty imagenes}">
                        <c:forEach var="img" items="${imagenes}" varStatus="status">
                            <div class="preview-item" id="caja-img-${img.idImagen}" style="position: relative; width: 100%; height: 100%; border-radius: 10px; overflow: hidden; background: #eae0d7;">
                                <!-- Imagen cargada desde tu servidor Linux -->
                                <img src="${pageContext.request.contextPath}/${img.urlImagen}" style="width: 100%; height: 100%; object-fit: cover;" alt="Imagen previa">

                                <!-- Botón "X" para eliminar la imagen del servidor -->
                                <button type="button" class="btn-eliminar-foto"
                                        onclick="eliminarImagenServidor(${img.idImagen}, this)"
                                        style="position: absolute; top: 6px; right: 6px; background: rgba(0,0,0,0.65); color: #fff; border: none; border-radius: 50%; width: 26px; height: 26px; display: flex; align-items: center; justify-content: center; cursor: pointer; z-index: 10;">
                                    <i class="bi bi-x"></i>
                                </button>

                                <!-- Etiqueta inferior con el número -->
                                <span style="position: absolute; bottom: 6px; left: 6px; background: rgba(0,0,0,0.6); color: #fff; font-size: 11px; padding: 2px 8px; border-radius: 10px;">
                                        ${status.first ? 'Principal' : (status.index + 1)}
                                </span>
                            </div>
                        </c:forEach>

                        <%-- Si tiene menos de 3 imágenes, rellenamos con espacios vacíos --%>
                        <c:forEach begin="${fn:length(imagenes) + 1}" end="3" var="i">
                            <div class="preview-empty"><i class="bi bi-image"></i><span>${i}</span></div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="preview-empty"><i class="bi bi-image"></i><span>1</span></div>
                        <div class="preview-empty"><i class="bi bi-image"></i><span>2</span></div>
                        <div class="preview-empty"><i class="bi bi-image"></i><span>3</span></div>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>

        <section class="info-card">
            <div class="card-title info-title">
                <div class="card-icon"><i class="bi bi-card-list"></i></div>
                <div>
                    <h2>Información del producto</h2>
                    <p>Escribe solo los datos que deseas cambiar</p>
                </div>
            </div>

            <div class="form-group">
                <label for="titulo">Nombre del artículo</label>
                <!-- Placeholder mostrando el nombre anterior -->
                <input id="titulo" name="titulo" type="text"
                       placeholder="${fn:escapeXml(articulo.nombre)}">
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="precio">Precio ($ MXN)</label>
                    <!-- Placeholder mostrando el precio anterior -->
                    <input id="precio" name="precio" type="number" step="0.01" min="0"
                           placeholder="${articulo.precio}">
                </div>
                <div class="form-group">
                    <label for="categoria">Categoría</label>
                    <select id="categoria" name="idCategoria">
                        <option value="1" ${articulo.idCategoriaFk == 1 ? 'selected' : ''}>Electrónica y Gadgets</option>
                        <option value="2" ${articulo.idCategoriaFk == 2 ? 'selected' : ''}>Libros</option>
                        <option value="3" ${articulo.idCategoriaFk == 3 ? 'selected' : ''}>Ropa</option>
                        <option value="4" ${articulo.idCategoriaFk == 4 ? 'selected' : ''}>Accesorios</option>
                        <option value="5" ${articulo.idCategoriaFk == 5 ? 'selected' : ''}>Material escolar</option>
                        <option value="6" ${articulo.idCategoriaFk == 6 ? 'selected' : ''}>Otros</option>
                    </select>
                </div>
            </div>

            <div class="form-group">
                <label for="descripcion">Descripción</label>
                <textarea id="descripcion" name="descripcion" rows="3"
                          placeholder="${fn:escapeXml(articulo.descripcion)}"></textarea>
            </div>

            <div class="form-actions mt-4">
                <a href="mis-articulos" class="btn cancel-button text-decoration-none">Cancelar</a>
                <button type="submit" class="btn publish-button">Guardar cambios</button>
            </div>
        </section>

    </form>
</main>

<script src="${pageContext.request.contextPath}/static/js/drag-drop.js"></script>
<script>
    // Función para eliminar una foto YA EXISTENTE en el servidor usando el modal estilizado MUA
    function eliminarImagenServidor(idImagen, botonElemento) {
        // 1. Abrimos el modal de confirmación MUA en lugar de confirm() nativo
        mostrarModalMUA("¿Estás seguro de que deseas eliminar esta imagen?", "info", () => {

            fetch('${pageContext.request.contextPath}/eliminar-imagen-articulo?id=' + idImagen, {
                method: 'POST'
            })
                .then(response => response.json())
                .then(data => {
                    if (data.exito) {
                        // 2. Obtenemos el contenedor de la imagen que se acaba de borrar
                        const caja = botonElemento.closest('.preview-item');
                        if (caja) {
                            // Lo convertimos visualmente en una caja vacía en lugar de romper el diseño
                            caja.className = 'preview-empty';
                            caja.style = '';
                            caja.innerHTML = '<i class="bi bi-image"></i><span>Vacío</span>';
                        }
                        // Opcional: confirmamos con el modal de éxito de MUA
                        mostrarModalMUA("Imagen eliminada correctamente.", "exito");
                    } else {
                        mostrarModalMUA(data.mensaje || "No se pudo eliminar la imagen.", "error");
                    }
                })
                .catch(error => {
                    console.error("Error al eliminar imagen:", error);
                    mostrarModalMUA("Error de conexión al intentar eliminar la imagen.", "error");
                });

        });
    }
</script>
</body>
</html>