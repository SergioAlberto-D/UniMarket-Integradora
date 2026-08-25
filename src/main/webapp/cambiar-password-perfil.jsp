<%--
  DOCUMENTACIÓN TÉCNICA — MUA
  Archivo: src/main/webapp/cambiar-password-perfil.jsp
  Propósito: Recurso de vista JSP para el módulo cambiar-password-perfil. Integra HTML, JSTL y/o expresiones JSP según el contenido fuente.
  Integración: la vista recibe datos desde Servlets mediante request/session y utiliza recursos CSS/JS del proyecto.
  Created by IntelliJ IDEA.
  User: sergio
  Date: 7/3/26
  Time: 7:00 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Cambiar contraseña - MUA</title>

  <link rel="icon" href="${pageContext.request.contextPath}/static/img/logoMUA.png" type="image/png">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bi_s/bootstrap-icons.css">

  <!-- Reutilizamos tu Login.css para aprovechar la tarjeta, inputs y la imagen lateral -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/Login.css">
</head>
<body class="login-page d-flex flex-column align-items-center justify-content-center">

<div class="container login-container px-0 px-sm-2">
  <div class="card shadow-lg border-0 card-custom login-card flex-row flex-wrap flex-md-nowrap">

    <!-- Imagen superior en móvil -->
    <div class="col-12 d-md-none login-image-mobile"></div>

    <!-- Columna Izquierda: Formulario -->
    <div class="col-12 col-md-6 p-4 p-md-5 bg-white login-form-column">

      <!-- LOGO MUA -->
      <img src="${pageContext.request.contextPath}/static/img/logoMUA.png" alt="Logo MUA" class="login-logo mb-4 shadow-sm">

      <h2 class="fw-bold text-dark mb-1">Cambiar contraseña</h2>
      <p class="text-muted mb-4 login-subtitle">Actualiza la contraseña de tu cuenta por seguridad.</p>

      <!-- Alertas de éxito o error -->
      <c:if test="${not empty error}">
        <div class="alert alert-danger d-flex align-items-center py-2" role="alert">
          <i class="bi bi-exclamation-triangle-fill me-2"></i>
          <div class="small">${error}</div>
        </div>
      </c:if>

      <c:if test="${not empty mensaje}">
        <div class="alert alert-info d-flex align-items-center py-2" role="alert">
          <i class="bi bi-info-circle-fill me-2"></i>
          <div class="small">${mensaje}</div>
        </div>
      </c:if>

      <form action="${pageContext.request.contextPath}/CambiarPasswordPerfilServlet" method="post">

        <!-- Contraseña Actual -->
        <div class="mb-3">
          <label for="passwordActual" class="form-label text-muted small mb-1">Contraseña actual</label>
          <div class="position-relative">
            <input type="password" class="form-control input-custom py-2 pe-5" id="passwordActual" name="passwordActual" placeholder="Tu contraseña actual" required>
            <i class="bi bi-eye-slash position-absolute top-50 end-0 translate-middle-y me-3 cursor-pointer toggle-password" data-target="passwordActual"></i>
          </div>
        </div>

        <!-- Nueva Contraseña -->
        <div class="mb-3">
          <label for="passwordNueva" class="form-label text-muted small mb-1">Nueva contraseña</label>
          <div class="position-relative">
            <input type="password" class="form-control input-custom py-2 pe-5" id="passwordNueva" name="passwordNueva" placeholder="Mínimo 8 caracteres" required>
            <i class="bi bi-eye-slash position-absolute top-50 end-0 translate-middle-y me-3 cursor-pointer toggle-password" data-target="passwordNueva"></i>
          </div>

          <!-- Validaciones visuales (Integradas desde nueva-password) -->
          <div id="password-rules" class="small mt-1 d-none" style="font-size: 0.8rem;">
            <div id="rule-length" class="text-danger mb-1"><i class="bi bi-x-circle icon-rule me-1"></i>Mínimo 8 caracteres</div>
            <div id="rule-uppercase" class="text-danger mb-1"><i class="bi bi-x-circle icon-rule me-1"></i>Al menos una mayúscula</div>
            <div id="rule-number" class="text-danger mb-1"><i class="bi bi-x-circle icon-rule me-1"></i>Al menos un número</div>
            <div id="rule-special" class="text-danger"><i class="bi bi-x-circle icon-rule me-1"></i>Al menos un carácter especial (@$!%*?&)</div>
          </div>
        </div>

        <!-- Confirmar Contraseña -->
        <div class="mb-4">
          <label for="passwordConfirmar" class="form-label text-muted small mb-1">Confirmar nueva contraseña</label>
          <div class="position-relative">
            <input type="password" class="form-control input-custom py-2 pe-5" id="passwordConfirmar" name="passwordConfirmar" placeholder="Repite tu nueva contraseña" required>
            <i class="bi bi-eye-slash position-absolute top-50 end-0 translate-middle-y me-3 cursor-pointer toggle-password" data-target="passwordConfirmar"></i>
          </div>
          <div id="match-rules" class="small mt-1 d-none" style="font-size: 0.8rem;">
            <div id="rule-match" class="text-danger"><i class="bi bi-x-circle icon-rule me-1"></i>Las contraseñas no coinciden</div>
          </div>
        </div>

        <!-- Botones -->
        <div class="d-flex gap-2 mb-3 mt-4">
          <a href="${pageContext.request.contextPath}/mi-perfil" class="btn btn-peach w-50 py-2 fw-semibold d-flex justify-content-center align-items-center text-decoration-none">
            <i class="bi bi-arrow-left me-1"></i> Cancelar
          </a>
          <button type="submit" class="btn btn-brown w-50 py-2 fw-semibold shadow-sm">
            Restablecer
          </button>
        </div>

      </form>
    </div>

    <!-- Columna Derecha: Imagen del Águila -->
    <div class="col-12 col-md-6 d-none d-md-block bg-image-side"></div>

  </div>

  <!-- Texto inferior fuera de la tarjeta -->
  <div class="text-center mt-3 mt-md-4 px-2 px-sm-3">
    <p class="text-dark m-0 login-footer-text">
      Al continuar, aceptas usar MUA como marketplace universitario responsablemente.
    </p>
  </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/bootstrap.js"></script>
<!-- Script centralizado para validaciones de contraseñas y ojito -->
<script src="${pageContext.request.contextPath}/static/js/cambiar-password.js"></script>
</body>
</html>
