<%--
  DOCUMENTACIÓN TÉCNICA — MUA
  Archivo: src/main/webapp/nueva-password.jsp
  Propósito: Recurso de vista JSP para el módulo nueva-password. Integra HTML, JSTL y/o expresiones JSP según el contenido fuente.
  Integración: la vista recibe datos desde Servlets mediante request/session y utiliza recursos CSS/JS del proyecto.
  Created by IntelliJ IDEA.
  User: sergio
  Date: 7/3/26
  Time: 7:01 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Nueva Contraseña - MUA</title>
  <link rel="icon" href="<%= request.getContextPath() %>/static/img/logoMUA.png" type="image/png">
  <link rel="stylesheet" href="assets/css/bootstrap.css">
  <link rel="stylesheet" href="assets/css/bi_s/bootstrap-icons.css">
  <link rel="stylesheet" href="assets/css/Login.css">
  <link rel="stylesheet" href="assets/css/Registro.css"> </head>
<body class="d-flex flex-column align-items-center justify-content-center vh-100">

<div class="container" style="max-width: 950px;">
  <div class="card shadow-lg border-0 card-custom flex-row flex-wrap flex-md-nowrap">

    <div class="col-12 col-md-6 p-4 p-md-5 bg-white" style="border-top-left-radius: 1rem; border-bottom-left-radius: 1rem;">

      <img src="static/img/logoMUA.png" alt="Logo MUA" class="mb-4 shadow-sm" style="width: 75px; height: 75px; background-color: #C28455; border-radius: 14px; padding: 2px; object-fit: cover;">

      <h2 class="fw-bold text-dark mb-1">Nueva contraseña</h2>
      <p class="text-muted mb-4" style="font-size: 0.95rem;">Crea una nueva contraseña segura para tu cuenta.</p>

      <form action="ActualizarPasswordServlet" method="post">
        <input type="hidden" name="correo" value="${param.correo}">
        <input type="hidden" name="token" value="${param.token}">

        <div class="mb-3">
          <label for="txtPassword1" class="form-label text-muted small mb-1">Nueva contraseña</label>
          <input type="password" class="form-control input-custom py-2" id="txtPassword1" name="contra1" placeholder="Contraseña" required>

          <div id="password-rules" class="small mt-1 d-none" style="font-size: 0.8rem;">
            <div id="rule-length" class="text-danger mb-1"><i class="bi bi-x-circle icon-rule me-1"></i>Mínimo 8 caracteres</div>
            <div id="rule-uppercase" class="text-danger mb-1"><i class="bi bi-x-circle icon-rule me-1"></i>Al menos una mayúscula</div>
            <div id="rule-number" class="text-danger mb-1"><i class="bi bi-x-circle icon-rule me-1"></i>Al menos un número</div>
            <div id="rule-special" class="text-danger"><i class="bi bi-x-circle icon-rule me-1"></i>Al menos un carácter especial (@$!%*?&)</div>
          </div>
        </div>

        <div class="mb-4">
          <label for="txtPassword2" class="form-label text-muted small mb-1">Confirmar contraseña</label>
          <input type="password" class="form-control input-custom py-2" id="txtPassword2" name="contra2" placeholder="Repite tu contraseña" required>

          <div id="match-rules" class="small mt-1 d-none" style="font-size: 0.8rem;">
            <div id="rule-match" class="text-danger"><i class="bi bi-x-circle icon-rule me-1"></i>Las contraseñas no coinciden</div>
          </div>
        </div>

        <button class="btn btn-brown w-100 py-2 fw-semibold shadow-sm mb-3" type="submit">
          Guardar Cambios <i class="bi bi-check2-circle ms-1"></i>
        </button>
      </form>
    </div>

    <div class="col-12 col-md-6 d-none d-md-block bg-image-side"></div>
  </div>

  <div class="text-center mt-4 px-3">
    <p class="text-dark m-0" style="font-size: 0.9rem;">
      Al continuar, aceptas usar MUA como marketplace universitario responsablemente.
    </p>
  </div>
</div>

<script src="assets/js/bootstrap.js"></script>
<script src="static/js/nueva-password.js"></script> </body>
</html>
