<%--
  Created by IntelliJ IDEA.
  User: sheuko
  Date: 7/3/26
  Time: 7:00 PM
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
<body class="login-page d-flex flex-column align-items-center justify-content-center">

<div class="container login-container px-0 px-sm-2">
    <div class="card shadow-lg border-0 card-custom login-card flex-row flex-wrap flex-md-nowrap">

        <!-- Imagen superior en móvil -->
        <div class="col-12 d-md-none login-image-mobile"></div>

        <!-- Columna Izquierda: Formulario -->
        <div class="col-12 col-md-6 p-4 p-md-5 bg-white login-form-column">

            <!-- LOGO MUA -->
            <img src="static/img/logoMUA.png" alt="Logo MUA" class="login-logo mb-4 shadow-sm">

            <h2 class="fw-bold text-dark mb-1">Recuperar cuenta</h2>
            <p class="text-muted mb-4 login-subtitle">Ingresa tu correo institucional para recibir un código.</p>

            <c:if test="${not empty error}">
                <div class="alert alert-danger d-flex align-items-center py-2" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i>
                    <div class="small">${error}</div>
                </div>
            </c:if>

            <form action="SolicitarRecuperacionServlet" method="post">
                <div class="mb-4">
                    <label for="txtCorreo" class="form-label text-muted small mb-1">Correo institucional</label>
                    <input type="email" class="form-control input-custom py-2" id="txtCorreo" name="correo" value="${param.correo}" placeholder="matricula@utez.edu.mx" required>
                </div>

                <!-- Botón Enviar -->
                <button class="btn btn-brown w-100 py-2 fw-semibold shadow-sm mb-3" type="submit">
                    Enviar Código <i class="bi bi-send ms-1"></i>
                </button>

                <!-- Enlace Regresar -->
                <div class="text-center mt-2 login-register-text">
                    <a href="login.jsp" class="login-link"><i class="bi bi-arrow-left"></i> Volver al inicio de sesión</a>
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

<script src="assets/js/bootstrap.js"></script>
</body>
</html>