<%--
  Created by IntelliJ IDEA.
  User: sheuko
  Date: 7/2/26
  Time: 9:51 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Registro - MUA</title>
  <link rel="stylesheet" href="assets/css/bootstrap.css">
  <link rel="stylesheet" href="assets/css/bi_s/bootstrap-icons.css">

  <link rel="stylesheet" href="assets/css/Login.css">
  <link rel="stylesheet" href="assets/css/Registro.css">
</head>
<body class="d-flex flex-column align-items-center justify-content-center min-vh-100 py-4">

<div class="container d-flex flex-column align-items-center">

  <div class="card shadow-sm border-0 card-custom card-registro p-4 p-md-5 mb-3">

    <img src="static/img/logoMUA.png" alt="Logo MUA" class="mb-4 shadow-sm" style="width: 55px; height: 55px; background-color: #C28455; border-radius: 12px; padding: 2px; object-fit: cover;">

    <h2 class="fw-bold text-dark mb-1 h3">Crear cuenta</h2>
    <p class="text-muted mb-4" style="font-size: 0.9rem;">Únete a la comunidad de compra y venta MUA</p>

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

    <!-- Barra de progreso -->
    <div class="progress mb-4" style="height: 8px; border-radius: 10px; background-color: #EAE2DC;">
      <div id="form-progress" class="progress-bar" role="progressbar" style="width: 0%; background-color: #753618;" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100"></div>
    </div>
    <p id="progress-text" class="text-end text-muted small mb-4">0% completado</p>

    <!-- Formulario -->
    <form id="registroForm" action="register" method="post">

      <!-- ================= PASO 1 ================= -->
      <div id="step-1">
        <div class="section-box">
          <div class="section-title">
            <div class="icon-box"><i class="bi bi-people"></i></div>
            Datos Personales y académicos
          </div>

          <div class="mb-3">
            <label for="txtNombre" class="form-label-custom">Nombre(s) *</label>
            <input value="${param.nombre}" type="text" class="form-control input-custom py-2 calc-progress" id="txtNombre" name="nombre" placeholder="Ejem: Marco Ivan" required>
          </div>

          <div class="row mb-3">
            <div class="col-6">
              <label for="txtApePat" class="form-label-custom">Apellido Paterno *</label>
              <input value="${param.apellidoPaterno}" type="text" class="form-control input-custom py-2 calc-progress" id="txtApePat" name="apellidoPaterno" placeholder="Ejem: Andres" required>
            </div>
            <div class="col-6">
              <label for="txtApeMat" class="form-label-custom">Apellido Materno *</label>
              <input value="${param.apellidoMaterno}" type="text" class="form-control input-custom py-2 calc-progress" id="txtApeMat" name="apellidoMaterno" placeholder="Ejem: Andres" required>
            </div>
          </div>

          <div class="mb-3">
            <label for="txtTel" class="form-label-custom">Número de teléfono</label>
            <input value="${param.telefono}" type="tel" class="form-control input-custom py-2" id="txtTel" name="telefono" placeholder="777 123 4567">
          </div>

          <div class="mb-2">
            <label for="txtCarrera" class="form-label-custom">Carrera *</label>
            <select class="form-select input-custom py-2 calc-progress" id="txtCarrera" name="carrera" required>
              <option value="" disabled selected>Selecciona tu carrera...</option>
              <option value="Terapia Física" ${param.carrera == 'Terapia Física' ? 'selected' : ''}>Terapia Física</option>
            </select>
          </div>
        </div>

        <!-- Botón Siguiente -->
        <button type="button" id="btnNext" class="btn btn-brown w-100 py-2 fw-semibold shadow-sm mb-3">
        Siguiente <i class="bi bi-arrow-right ms-1"></i>
      </button>
      </div>

      <!-- ================= PASO 2 ================= -->
      <div id="step-2" class="d-none">
        <div class="section-box">
          <div class="section-title">
            <div class="icon-box"><i class="bi bi-lock"></i></div>
            Correo y contraseña
          </div>

          <div class="mb-3">
            <label for="txtCorreo" class="form-label-custom">Correo institucional *</label>
            <input value="${param.email1}" type="email" class="form-control input-custom py-2 calc-progress" id="txtCorreo" name="email1" placeholder="Matricula@utez.edu.mx" required>
          </div>

          <div class="mb-3">
            <label for="txtPassword1" class="form-label-custom">Contraseña *</label>
            <input type="password" class="form-control input-custom py-2 calc-progress" id="txtPassword1" name="contra1" placeholder="Contraseña" required>

            <div id="password-rules" class="small mt-1 d-none" style="font-size: 0.8rem;">
              <div id="rule-length" class="text-danger mb-1"><i class="bi bi-x-circle icon-rule"></i>Mínimo 8 caracteres</div>
              <div id="rule-uppercase" class="text-danger mb-1"><i class="bi bi-x-circle icon-rule"></i>Al menos una mayúscula</div>
              <div id="rule-number" class="text-danger mb-1"><i class="bi bi-x-circle icon-rule"></i>Al menos un número</div>
              <div id="rule-special" class="text-danger"><i class="bi bi-x-circle icon-rule"></i>Al menos un carácter especial (@$!%*?&)</div>
            </div>
          </div>

          <div class="mb-2">
            <label for="txtPassword2" class="form-label-custom">Confirmar contraseña *</label>
            <input type="password" class="form-control input-custom py-2 calc-progress" id="txtPassword2" name="contra2" placeholder="Repite tu contraseña" required>

            <div id="match-rules" class="small mt-1 d-none" style="font-size: 0.8rem;">
              <div id="rule-match" class="text-danger"><i class="bi bi-x-circle icon-rule"></i>Las contraseñas deben coincidir</div>
            </div>
          </div>
        </div>

        <!-- Botones Atrás y Crear Cuenta -->
        <div class="d-flex gap-2 mb-3">
          <button type="button" id="btnBack" class="btn btn-outline-secondary w-50 py-2 fw-semibold shadow-sm">
            <i class="bi bi-arrow-left me-1"></i> Atrás
          </button>
          <button type="submit" id="btnSubmit" class="btn btn-brown w-50 py-2 fw-semibold shadow-sm">
            Crear Cuenta
          </button>
        </div>
      </div>

      <!-- Enlace Login -->
      <div class="text-center mt-2" style="font-size: 0.85rem;">
        <span class="text-muted">¿Ya tienes cuenta?</span>
        <a href="login.jsp" class="text-muted text-decoration-underline">Inicia sesión</a>
      </div>

    </form>
  </div>

  <div class="text-center px-3" style="max-width: 500px;">
    <p class="text-dark m-0" style="font-size: 0.85rem;">
      Al continuar, aceptas usar MUA como marketplace universitario responsablemente.
    </p>
  </div>

</div>

<script src="assets/js/bootstrap.js"></script>
<script src="static/js/registro.js"></script>
</body>
</html>