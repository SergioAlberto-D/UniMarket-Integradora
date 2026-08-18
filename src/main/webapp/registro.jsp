<%--
  Created by IntelliJ IDEA.
  User: sheuko
  Date: 7/2/26
  Time: 9:51 PM
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
  <link rel="icon" href="<%= request.getContextPath() %>/static/img/logoMUA.png" type="image/png">
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
    <form id="registroForm" action="register" method="post" enctype="multipart/form-data">

      <!-- ================= PASO 1 ================= -->
      <div id="step-1">
        <div class="section-box">
          <div class="section-title">
            <div class="icon-box"><i class="bi bi-people"></i></div>
            Datos Personales y académicos
          </div>

          <div class="mb-3">
            <label for="txtNombre" class="form-label-custom">Nombre(s) <span class="text-danger">*</span></label>
            <input value="${param.nombre}" type="text" class="form-control input-custom py-2 calc-progress" id="txtNombre" name="nombre" placeholder="" required>
          </div>

          <div class="row mb-3">
            <div class="col-6">
              <label for="txtApePat" class="form-label-custom">Apellido Paterno <span class="text-danger">*</span></label>
              <input value="${param.apellidoPaterno}" type="text" class="form-control input-custom py-2 calc-progress" id="txtApePat" name="apellidoPaterno" placeholder="" required>
            </div>
            <div class="col-6">
              <label for="txtApeMat" class="form-label-custom">Apellido Materno <span class="text-danger">*</span></label>
              <input value="${param.apellidoMaterno}" type="text" class="form-control input-custom py-2 calc-progress" id="txtApeMat" name="apellidoMaterno" placeholder="" required>
            </div>
          </div>

          <div class="mb-3">
            <label for="txtTel" class="form-label-custom">Número de teléfono <span class="text-danger">*</span></label>
            <input value="${param.telefono}" type="tel" class="form-control input-custom py-2 calc-progress" id="txtTel" name="telefono" placeholder="(777)-123-4567" required>
          </div>

          <div class="mb-2">
            <label for="txtCarrera" class="form-label-custom">División Académica<span class="text-danger">*</span></label>
            <select class="form-select input-custom py-2 calc-progress" id="txtCarrera" name="idDivision" required>
              <option value="" disabled selected>Selecciona tu División Académica...</option>
              <option value="1">DATID</option>
              <option value="2">DAMI</option>
              <option value="3">DACEA</option>
              <option value="4">DATEFI</option>
            </select>
          </div>
        </div> <!-- AQUÍ VA EL CIERRE DEL SECTION-BOX QUE FALTABA -->

        <!-- Botón Siguiente -->
        <button type="button" id="btnNext" class="btn btn-brown w-100 py-2 fw-semibold shadow-sm mb-3" disabled>
          Siguiente <i class="bi bi-arrow-right ms-1"></i>
        </button>
      </div> <!-- AQUÍ SE CIERRA EL STEP-1 CORRECTAMENTE -->

      <!-- ================= PASO 2 ================= -->
      <div id="step-2" class="d-none">
        <div class="section-box">
          <div class="section-title">
            <div class="icon-box"><i class="bi bi-lock"></i></div>
            Correo y contraseña
          </div>

          <div class="mb-3">
            <label for="txtMatricula" class="form-label-custom">Matrícula Institucional <span class="text-danger">*</span></label>
            <div class="input-group">
              <input value="${param.matricula}" type="text" class="form-control input-custom py-2 calc-progress" id="txtMatricula" name="matricula" placeholder="Ej. 20223TN000" style="border-top-right-radius: 0; border-bottom-right-radius: 0;" required>
              <span class="input-group-text" style="background-color: #F3ECE8; border: none; color: #6c757d; border-top-right-radius: 6px; border-bottom-right-radius: 6px; font-weight: 500;">@utez.edu.mx</span>
            </div>
            <div class="form-text mt-1" style="font-size: 0.8rem;">Solo ingresa tu matrícula, nosotros agregamos el resto.</div>
          </div>

          <div class="mb-3">
            <label for="txtPassword1" class="form-label-custom">Contraseña <span class="text-danger">*</span></label>
            <div class="position-relative">
              <input type="password" class="form-control input-custom py-2 pe-5 calc-progress" id="txtPassword1" name="contra1" placeholder="Contraseña" required>
              <i class="bi bi-eye-slash position-absolute top-50 end-0 translate-middle-y me-3 cursor-pointer toggle-password" data-target="txtPassword1"></i>
            </div>

            <div id="password-rules" class="small mt-1 d-none" style="font-size: 0.8rem;">
              <div id="rule-length" class="text-danger mb-1"><i class="bi bi-x-circle icon-rule"></i>Mínimo 8 caracteres</div>
              <div id="rule-uppercase" class="text-danger mb-1"><i class="bi bi-x-circle icon-rule"></i>Al menos una mayúscula</div>
              <div id="rule-number" class="text-danger mb-1"><i class="bi bi-x-circle icon-rule"></i>Al menos un número</div>
              <div id="rule-special" class="text-danger"><i class="bi bi-x-circle icon-rule"></i>Al menos un carácter especial (@$!%*?&)</div>
            </div>
          </div>

          <div class="mb-2">
            <label for="txtPassword2" class="form-label-custom">Confirmar contraseña <span class="text-danger">*</span></label>
            <div class="position-relative">
              <input type="password" class="form-control input-custom py-2 pe-5 calc-progress" id="txtPassword2" name="contra2" placeholder="Repite tu contraseña" required>
              <i class="bi bi-eye-slash position-absolute top-50 end-0 translate-middle-y me-3 cursor-pointer toggle-password" data-target="txtPassword2"></i>
            </div>

            <div id="match-rules" class="small mt-1 d-none" style="font-size: 0.8rem;">
              <div id="rule-match" class="text-danger"><i class="bi bi-x-circle icon-rule"></i>Las contraseñas deben coincidir</div>
            </div>
          </div>
        </div> <!-- FIN DE LA SECTION-BOX DEL PASO 2 -->

        <!-- Botones Atrás y Siguiente -->
        <div class="d-flex gap-2 mb-3">
          <button type="button" id="btnBack" class="btn btn-peach w-50 py-2 fw-semibold">
            <i class="bi bi-arrow-left me-1"></i> Atrás
          </button>
          <button type="button" id="btnNext2" class="btn btn-brown w-50 py-2 fw-semibold shadow-sm" disabled>
            Siguiente <i class="bi bi-arrow-right ms-1"></i>
          </button>
        </div>
      </div> <!-- FIN DEL PASO 2 -->

      <!-- ================= PASO 3 ================= -->
      <div id="step-3" class="d-none">
        <div class="section-box">
          <div class="section-title">
            <div class="icon-box"><i class="bi bi-card-image"></i></div>
            Identificación escolar
          </div>
          <p class="text-muted mb-3" style="font-size: 0.85rem;">
            Sube una foto de tu credencial escolar (frente y reverso). Esto nos ayuda a verificar que perteneces a la comunidad universitaria.
          </p>

          <div class="mb-3">
            <label for="fileCredencialFrente" class="form-label-custom">Credencial escolar - Frente <span class="text-danger">*</span></label>
            <input type="file" class="form-control input-custom py-2 calc-progress" id="fileCredencialFrente" name="credencialFrente" accept="image/png, image/jpeg, image/jpg" required>
            <img id="previewFrente" src="#" alt="Vista previa frente" class="d-none mt-2" style="max-width: 100%; max-height: 160px; border-radius: 10px; border: 1px solid #dee2e6; object-fit: cover;">
          </div>

          <div class="mb-2">
            <label for="fileCredencialReverso" class="form-label-custom">Credencial escolar - Reverso <span class="text-danger">*</span></label>
            <input type="file" class="form-control input-custom py-2 calc-progress" id="fileCredencialReverso" name="credencialReverso" accept="image/png, image/jpeg, image/jpg" required>
            <img id="previewReverso" src="#" alt="Vista previa reverso" class="d-none mt-2" style="max-width: 100%; max-height: 160px; border-radius: 10px; border: 1px solid #dee2e6; object-fit: cover;">
          </div>
        </div> <!-- FIN DE LA SECTION-BOX DEL PASO 3 -->

        <!-- Botones Atrás y Crear Cuenta -->
        <div class="d-flex gap-2 mb-3">
          <button type="button" id="btnBack2" class="btn btn-peach w-50 py-2 fw-semibold">
            <i class="bi bi-arrow-left me-1"></i> Atrás
          </button>
          <button type="submit" id="btnSubmit" class="btn btn-brown w-50 py-2 fw-semibold shadow-sm">
            Crear Cuenta
          </button>
        </div>
      </div> <!-- FIN DEL PASO 3 -->

      <!-- Enlace Login -->
      <div class="text-center mt-2" style="font-size: 0.85rem;">
        <span class="text-muted">¿Ya tienes cuenta?</span>
        <a href="login.jsp" class="login-link ms-1">Inicia sesión</a>
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
