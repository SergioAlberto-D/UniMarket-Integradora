<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.unimarket.unimarketintegradora.model.Usuario" %>

<%
  Usuario usuario = null;

  if (session != null && session.getAttribute("usuario") != null) {
    usuario = (Usuario) session.getAttribute("usuario");
  }

  if (usuario == null) {
    response.sendRedirect("login.jsp");
    return;
  }

  String iniciales = "";

  if (usuario.getNombre() != null && !usuario.getNombre().isEmpty()) {
    iniciales += usuario.getNombre().substring(0, 1).toUpperCase();
  }

  if (usuario.getApellidoPaterno() != null && !usuario.getApellidoPaterno().isEmpty()) {
    iniciales += usuario.getApellidoPaterno().substring(0, 1).toUpperCase();
  }

  if (iniciales.isEmpty()) {
    iniciales = "U";
  }

  boolean exito = "true".equals(request.getParameter("exito"));
%>

<!doctype html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Publicar artículo - MUA</title>

  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="icon" href="<%= request.getContextPath() %>/static/img/logoMUA.png" type="image/png">
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/bootstrap.css">
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/bi_s/bootstrap-icons.css">
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/publicar-articulo.css">
</head>

<body>

<header class="mua-header">
  <div class="header-left">
    <a href="index.jsp" class="brand">
      <div class="brand-logo">MUA</div>
      <span>Mua</span>
    </a>

    <div class="search-box">
      <i class="bi bi-search"></i>
      <input type="text" placeholder="Buscar artículos...">
    </div>
  </div>

  <nav class="header-nav">
    <a href="index.jsp">
      <i class="bi bi-house-door-fill"></i>
      Buscar
    </a>

    <a href="#" class="disabled-link">
      <i class="bi bi-archive-fill"></i>
      Mis artículos
    </a>

    <a href="publicar-articulo.jsp" class="active">
      <i class="bi bi-plus-lg"></i>
      Publicar
    </a>

    <div class="profile-circle"><%= iniciales %></div>
  </nav>
</header>

<main class="publish-page">

  <% if (exito) { %>
  <div class="success-toast">
    <i class="bi bi-check-lg"></i>
    <div>
      <h3>Artículo añadido</h3>
      <p>El artículo fue publicado con éxito</p>
    </div>
  </div>
  <% } %>

  <% if (request.getAttribute("error") != null) { %>
  <div class="error-alert">
    <i class="bi bi-exclamation-triangle"></i>
    <span><%= request.getAttribute("error") %></span>
  </div>
  <% } %>

  <section class="publish-title">
    <div class="title-icon">
      <i class="bi bi-plus-lg"></i>
    </div>

    <div>
      <h1>Publicar artículo</h1>
      <p>Agrega un producto para venderlo en la universidad</p>
    </div>
  </section>

  <section class="tip-card">
    <div class="tip-icon">
      <i class="bi bi-lightbulb"></i>
    </div>

    <div>
      <h3>Consejo al publicar</h3>
      <p>Toma fotos claras, información detallada y sé justo con el precio</p>
    </div>
  </section>

  <form action="<%= request.getContextPath() %>/publicar-articulo"
        method="post"
        enctype="multipart/form-data"
        class="publish-layout">

    <section class="images-card">
      <div class="card-title">
        <div class="card-icon">
          <i class="bi bi-image"></i>
        </div>

        <div>
          <h2>Imágenes del artículo</h2>
          <p>Agrega hasta 3 imágenes del producto</p>
        </div>
      </div>

      <label for="imagenes" class="upload-zone">
        <div class="upload-icon">
          <i class="bi bi-upload"></i>
        </div>

        <h3>Arrastra imágenes aquí</h3>
        <p>o selecciona una imagen desde el dispositivo</p>

        <span>Seleccionar archivos</span>
      </label>

      <input id="imagenes"
             name="imagenes"
             type="file"
             accept=".jpg,.jpeg,.png"
             multiple
             class="hidden-file">

      <div class="image-help">
        <h3>Hasta un máximo de 3 imágenes</h3>
        <p>JPG o PNG</p>
      </div>

      <div class="preview-grid" id="previewGrid">
        <div class="preview-empty">
          <i class="bi bi-image"></i>
          <span>1</span>
        </div>

        <div class="preview-empty">
          <i class="bi bi-image"></i>
          <span>2</span>
        </div>

        <div class="preview-empty">
          <i class="bi bi-image"></i>
          <span>3</span>
        </div>
      </div>
    </section>

    <section class="info-card">
      <div class="card-title info-title">
        <div class="card-icon">
          <i class="bi bi-card-list"></i>
        </div>

        <div>
          <h2>Información del producto</h2>
          <p>Completa los datos solicitados</p>
        </div>
      </div>

      <h3 class="form-section-title">Información básica</h3>

      <div class="form-group">
        <label for="titulo">Nombre del artículo</label>
        <input id="titulo"
               name="titulo"
               type="text"
               placeholder="Gameboy Advance SP"
               maxlength="120"
               required>
      </div>

      <div class="form-row">
        <div class="form-group">
          <label for="precio">Precio</label>
          <input id="precio"
                 name="precio"
                 type="number"
                 step="0.01"
                 min="0"
                 placeholder="450.00"
                 required>
        </div>

        <div class="form-group">
          <label for="categoria">Categoría</label>
          <select id="categoria" name="idCategoria" required>
            <option value="">Selecciona una categoría</option>
            <!-- Los values ahora son números simulando los IDs de tu tabla CATEGORIA -->
            <option value="1">Electrónica y Gadgets</option>
            <option value="2">Libros</option>
            <option value="3">Ropa</option>
            <option value="4">Accesorios</option>
            <option value="5">Material escolar</option>
            <option value="6">Otros</option>
          </select>
        </div>
      </div>

      <div class="form-group">
        <label for="descripcion">Descripción</label>
        <textarea id="descripcion"
                  name="descripcion"
                  rows="3"
                  placeholder="Detalla el producto, para mantener una buena transparencia con los compradores"
                  maxlength="1000"
                  required></textarea>
      </div>

      <div class="form-group">
        <label for="lugarEncuentro">Lugar de encuentro</label>
        <input id="lugarEncuentro"
               name="lugarEncuentro"
               type="text"
               placeholder="Delante de D2"
               maxlength="200"
               required>
      </div>

      <div class="form-actions">
        <a href="index.jsp" class="cancel-button">Cancelar</a>
        <button type="submit" class="publish-button">Publicar artículo</button>
      </div>
    </section>

  </form>

</main>

<script>
  const inputImagenes = document.getElementById("imagenes");
  const previewGrid = document.getElementById("previewGrid");

  inputImagenes.addEventListener("change", function () {
    const archivos = Array.from(this.files).slice(0, 3);

    previewGrid.innerHTML = "";

    for (let i = 0; i < 3; i++) {
      const archivo = archivos[i];

      if (archivo) {
        const lector = new FileReader();

        lector.onload = function (e) {
          const item = document.createElement("div");
          item.className = "preview-item";

          item.innerHTML =
                  '<img src="' + e.target.result + '" alt="Vista previa">' +
                  '<span>' + (i === 0 ? 'Principal' : (i + 1)) + '</span>';

          previewGrid.appendChild(item);
        };

        lector.readAsDataURL(archivo);
      } else {
        const empty = document.createElement("div");
        empty.className = "preview-empty";
        empty.innerHTML = '<i class="bi bi-image"></i><span>' + (i + 1) + '</span>';
        previewGrid.appendChild(empty);
      }
    }

    if (this.files.length > 3) {
      alert("Solo puedes subir máximo 3 imágenes.");
      this.value = "";
    }
  });
</script>

</body>
</html>
