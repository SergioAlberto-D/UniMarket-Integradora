<%--
  DOCUMENTACIÓN TÉCNICA — MUA
  Archivo: src/main/webapp/publicar-articulo.jsp
  Propósito: Recurso de vista JSP para el módulo publicar-articulo. Integra HTML, JSTL y/o expresiones JSP según el contenido fuente.
  Integración: la vista recibe datos desde Servlets mediante request/session y utiliza recursos CSS/JS del proyecto.
  Created by IntelliJ IDEA.
  User: sergio
  Date: 7/3/26
  Time: 7:00 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!doctype html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Publicar artículo - MUA</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="icon" href="${pageContext.request.contextPath}/static/img/logoMUA.png" type="image/png">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bi_s/bootstrap-icons.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/header.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/publicar-articulo.css">
</head>
<body>

<%-- Inyectamos el header --%>
<jsp:include page="components/header.jsp" />

<main class="publish-page">

  <%-- Usamos JSTL para leer el parámetro de la URL (?exito=true) --%>
  <c:if test="${param.exito == 'true'}">
    <div class="success-toast">
      <i class="bi bi-check-lg"></i>
      <div>
        <h3>Artículo añadido</h3>
        <p>El artículo fue publicado con éxito</p>
      </div>
    </div>
  </c:if>

  <%-- Usamos JSTL para leer si el Servlet nos mandó un error --%>
  <c:if test="${not empty error}">
    <div class="error-alert">
      <i class="bi bi-exclamation-triangle"></i>
      <span>${error}</span>
    </div>
  </c:if>

  <section class="publish-title">
    <div class="title-icon">
      <i class="bi bi-plus-lg"></i>
    </div>
    <div>
      <h1>Publicar artículo</h1>
      <p>Agrega un producto para venderlo en la universidad</p>
    </div>
  </section>

  <form action="${pageContext.request.contextPath}/publicar-articulo" method="post" enctype="multipart/form-data" class="publish-layout">

    <section class="images-card">
      <div class="card-title">
        <div class="card-icon"><i class="bi bi-image"></i></div>
        <div>
          <h2>Imágenes del artículo</h2>
          <p>Agrega hasta 3 imágenes del producto</p>
        </div>
      </div>

      <label for="imagenes" class="upload-zone" id="dropZone">
        <div class="upload-icon">
          <i class="bi bi-upload"></i>
        </div>
        <h3>Arrastra imágenes aquí</h3>
        <p>o selecciona una imagen desde el dispositivo</p>
        <span class="btn-seleccionar">Seleccionar archivos</span>
      </label>

      <input id="imagenes" name="imagenes" type="file" accept="image/png, image/jpeg, image/jpg" multiple style="opacity: 0; position: absolute; z-index: -1; width: 1px; height: 1px;">

      <div class="image-help">
        <h3>Hasta un máximo de 3 imágenes</h3>
        <p>JPG o PNG</p>
      </div>

      <div class="preview-grid" id="previewGrid">
        <div class="preview-empty"><i class="bi bi-image"></i><span>1</span></div>
        <div class="preview-empty"><i class="bi bi-image"></i><span>2</span></div>
        <div class="preview-empty"><i class="bi bi-image"></i><span>3</span></div>
      </div>
    </section>

    <section class="info-card">
      <div class="card-title info-title">
        <div class="card-icon"><i class="bi bi-card-list"></i></div>
        <div><h2>Información del producto</h2><p>Completa los datos solicitados</p></div>
      </div>

      <div class="form-group">
        <label for="titulo">Nombre del artículo</label>
        <input id="titulo" name="titulo" type="text" placeholder="Ej. Calculadora Científica Casio" required>
      </div>

      <div class="form-row">
        <div class="form-group">
          <label for="precio">Precio</label>
          <input id="precio" name="precio" type="number" step="0.01" min="0" placeholder="450.00" required>
        </div>
        <div class="form-group">
          <label for="categoria">Categoría</label>
          <select id="categoria" name="idCategoria" required>
            <option value="" disabled ${empty param.idCategoria ? 'selected' : ''}>Selecciona una categoría</option>
            <c:forEach var="cat" items="${categorias}">
              <option value="${cat.idCategoria}" ${param.idCategoria == cat.idCategoria ? 'selected' : ''}>
                  ${fn:escapeXml(cat.categoria)}
              </option>
            </c:forEach>
          </select>
        </div>
      </div>

      <div class="form-group">
        <label for="descripcion">Descripción</label>
        <textarea id="descripcion" name="descripcion" rows="3" placeholder="Detalla el producto..."></textarea>
      </div>

      <div class="form-actions mt-4">
        <a href="inicio" class="btn cancel-button text-decoration-none">Cancelar</a>
        <button type="submit" class="btn publish-button">Publicar artículo</button>
      </div>
    </section>

  </form>
</main>

<script src="${pageContext.request.contextPath}/static/js/drag-drop.js"></script>
</body>
</html>
