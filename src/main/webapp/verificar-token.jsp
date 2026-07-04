<%--
  Created by IntelliJ IDEA.
  User: sheuko
  Date: 7/3/26
  Time: 7:01 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Verificar Código - MUA</title>
  <link rel="stylesheet" href="assets/css/bootstrap.css">
  <link rel="stylesheet" href="assets/css/bi_s/bootstrap-icons.css">
  <link rel="stylesheet" href="assets/css/Login.css">
</head>
<body class="d-flex flex-column align-items-center justify-content-center vh-100">

<div class="container" style="max-width: 950px;">
  <div class="card shadow-lg border-0 card-custom flex-row flex-wrap flex-md-nowrap">

    <div class="col-12 col-md-6 p-4 p-md-5 bg-white" style="border-top-left-radius: 1rem; border-bottom-left-radius: 1rem;">

      <img src="static/img/logoMUA.png" alt="Logo MUA" class="mb-4 shadow-sm" style="width: 75px; height: 75px; background-color: #C28455; border-radius: 14px; padding: 2px; object-fit: cover;">

      <h2 class="fw-bold text-dark mb-1">Verifica tu identidad</h2>
      <p class="text-muted mb-4" style="font-size: 0.95rem;">Ingresa el código que enviamos a tu correo.</p>

      <form action="VerificarTokenServlet" method="post">
        <input type="hidden" name="correo" value="${param.correo}">

        <div class="mb-4">
          <label for="txtToken" class="form-label text-muted small mb-1">Código de seguridad</label>
          <input type="text" class="form-control input-custom py-3 text-center fw-bold fs-4" id="txtToken" name="token" placeholder="XXXX-XXXX" required style="letter-spacing: 3px;">
        </div>

        <button class="btn btn-brown w-100 py-2 fw-semibold shadow-sm mb-3" type="submit">
          Validar Código <i class="bi bi-check-circle ms-1"></i>
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
</body>
</html>