<%--
  Created by IntelliJ IDEA.
  User: sheuko
  Date: 7/3/26
  Time: 7:01 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Validando Token - MUA</title>
  <link rel="icon" href="<%= request.getContextPath() %>/static/img/logoMUA.png" type="image/png">
  <link rel="stylesheet" href="assets/css/bootstrap.css">
  <link rel="stylesheet" href="assets/css/bi_s/bootstrap-icons.css">
  <link rel="stylesheet" href="assets/css/Login.css">
  <style>
    .spinner-custom {
      color: #1a1a1a;
      width: 2.5rem;
      height: 2.5rem;
      border-width: 0.25rem;
    }
  </style>
</head>
<body class="login-page d-flex flex-column align-items-center justify-content-center vh-100">

<div class="container" style="max-width: 950px;">
  <div class="card shadow-lg border-0 card-custom flex-row flex-wrap flex-md-nowrap">

    <!-- Columna Izquierda: Loader -->
    <div class="col-12 col-md-6 p-4 p-md-5 bg-white position-relative d-flex flex-column justify-content-center align-items-center text-center" style="border-top-left-radius: 1rem; border-bottom-left-radius: 1rem; min-height: 400px;">

      <!-- Logo Flotante -->
      <img src="static/img/logoMUA.png" alt="Logo MUA" class="position-absolute shadow-sm d-none d-md-block" style="top: 2.5rem; left: 2.5rem; width: 45px; height: 45px; background-color: #C28455; border-radius: 10px; padding: 2px; object-fit: cover;">

      <!-- Si ocurre un error al validar el token, mostramos el mensaje y detenemos la animación -->
      <c:if test="${not empty error}">
        <h2 class="fw-bold text-dark mb-2">Error de validación</h2>
        <div class="text-danger fw-semibold mt-3 mb-4 fs-5">
          <i class="bi bi-x-circle-fill me-1"></i> ${error}
        </div>
        <a href="recuperar-password.jsp" class="btn btn-brown py-2 px-4 shadow-sm fw-semibold">
          Solicitar nuevo código
        </a>
      </c:if>

      <!-- Pantalla de Carga (Solo si no hay error previo) -->
      <c:if test="${empty error}">
        <h2 class="fw-bold text-dark mb-2" style="letter-spacing: -0.5px;">Validando Token</h2>
        <p class="text-muted mb-4 fs-6" id="status-text">Comprobando validez</p>

        <!-- Spinner animado -->
        <div id="loader" class="spinner-border spinner-custom mb-4" role="status">
          <span class="visually-hidden">Cargando...</span>
        </div>

        <!-- Mensaje de éxito (Oculto por defecto) -->
        <div id="success-msg" class="d-none text-dark fw-semibold fs-6">
          <i class="bi bi-check-circle-fill fs-5 align-middle me-1" style="color: #1a1a1a;"></i> token válido, redirigiendo
        </div>

        <!-- Formulario invisible con los datos de la URL -->
        <form id="tokenForm" action="VerificarTokenServlet" method="post" class="d-none">
          <input type="hidden" name="correo" value="${param.correo}">
          <input type="hidden" name="token" value="${param.token}">
        </form>
      </c:if>

    </div>

    <!-- Columna Derecha: Imagen del Águila -->
    <div class="col-12 col-md-6 d-none d-md-block bg-image-side"></div>
  </div>

  <div class="text-center mt-4 px-3">
    <p class="text-dark m-0" style="font-size: 0.9rem;">
      Al continuar, aceptas usar MUA como marketplace universitario responsable.
    </p>
  </div>
</div>

<script src="assets/js/bootstrap.js"></script>

<!-- Script de Automatización importado de un js externo -->
<c:if test="${empty error}">
  <script src="static/js/verificar-token.js"></script>
</c:if>

</body>
</html>