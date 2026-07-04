<%--
  Created by IntelliJ IDEA.
  User: sheuko
  Date: 7/3/26
  Time: 7:00 PM
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
    <title>Recuperar Contraseña - MUA</title>
    <link rel="stylesheet" href="assets/css/bootstrap.css">
    <link rel="stylesheet" href="assets/css/bi_s/bootstrap-icons.css">
    <link rel="stylesheet" href="assets/css/Login.css">
</head>
<body class="d-flex flex-column align-items-center justify-content-center vh-100">

<div class="container" style="max-width: 950px;">
    <div class="card shadow-lg border-0 card-custom flex-row flex-wrap flex-md-nowrap">

        <div class="col-12 col-md-6 p-4 p-md-5 bg-white" style="border-top-left-radius: 1rem; border-bottom-left-radius: 1rem;">

            <img src="static/img/logoMUA.png" alt="Logo MUA" class="mb-4 shadow-sm" style="width: 75px; height: 75px; background-color: #C28455; border-radius: 14px; padding: 2px; object-fit: cover;">

            <h2 class="fw-bold text-dark mb-1">Recuperar cuenta</h2>
            <p class="text-muted mb-4" style="font-size: 0.95rem;">Ingresa tu correo institucional para recibir un código.</p>

            <c:if test="${not empty error}">
                <div class="alert alert-danger d-flex align-items-center py-2" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i>
                    <div class="small">${error}</div>
                </div>
            </c:if>

            <form action="SolicitarRecuperacionServlet" method="post">
                <div class="mb-4">
                    <label for="txtCorreo" class="form-label text-muted small mb-1">Correo institucional</label>
                    <input type="email" class="form-control input-custom py-2" id="txtCorreo" name="correo" value="${param.correo}" placeholder="usuario@utez.edu.mx" required>
                </div>

                <button class="btn btn-brown w-100 py-2 fw-semibold shadow-sm mb-3" type="submit">
                    Enviar Código <i class="bi bi-send ms-1"></i>
                </button>

                <div class="text-center small mt-2">
                    <a href="login.jsp" class="text-muted text-decoration-underline"><i class="bi bi-arrow-left"></i> Volver al inicio de sesión</a>
                </div>
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