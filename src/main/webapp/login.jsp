<%--
  Created by IntelliJ IDEA.
  User: sheuko
  Date: 7/2/26
  Time: 8:28 PM
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
    <title>Iniciar sesión - MUA</title>
    <link rel="stylesheet" href="assets/css/bootstrap.css">
    <link rel="stylesheet" href="assets/css/bi_s/bootstrap-icons.css">
    <link rel="stylesheet" href="assets/css/Login.css">
</head>
<body class="d-flex flex-column align-items-center justify-content-center vh-100">

<div class="container" style="max-width: 950px;">
    <div class="card shadow-lg border-0 card-custom flex-row flex-wrap flex-md-nowrap">

        <!-- Columna Izquierda: Formulario -->
        <div class="col-12 col-md-6 p-4 p-md-5 bg-white" style="border-top-left-radius: 1rem; border-bottom-left-radius: 1rem;">

            <!-- LOGO MUA -->
            <img src="static/img/logoMUA.png" alt="Logo MUA" class="mb-4 shadow-sm"
                 style="width: 75px; height: 75px; background-color: #C28455; border-radius: 14px; padding: 2px; object-fit: cover;">

            <h2 class="fw-bold text-dark mb-1">Bienvenidos</h2>
            <p class="text-muted mb-4" style="font-size: 0.95rem;">Inicia sesión con tu correo institucional</p>

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

            <form action="login" method="post">
                <!-- Correo Institucional -->
                <div class="mb-3">
                    <label for="txtUsuario" class="form-label text-muted small mb-1">Correo institucional</label>
                    <input type="text" class="form-control input-custom py-2" id="txtUsuario" name="email" placeholder="usuario@utez.edu.mx" required>
                </div>

                <!-- Contraseña -->
                <div class="mb-1">
                    <label for="txtPassword" class="form-label text-muted small mb-1">Contraseña</label>
                    <input type="password" class="form-control input-custom py-2" id="txtPassword" name="contra" placeholder="12345678" required>
                </div>



                <!-- Recuperar Contraseña -->
                <div class="text-end mb-4 mt-2">
                    <a href="recuperar-contra.jsp" class="text-decoration-none text-muted" style="font-size: 0.75rem;">¿Olvidaste tu contraseña?</a>
                </div>

                <!-- Botón Iniciar Sesión -->
                <button class="btn btn-brown w-100 py-2 fw-semibold shadow-sm mb-3" type="submit">
                    Iniciales
                </button>

                <!-- Registro -->
                <div class="text-center small mt-2">
                    <span class="text-muted">¿No tienes cuenta?</span>
                    <a href="registro.jsp" class="text-muted text-decoration-underline">Regístrate</a>
                </div>
            </form>
        </div>

        <!-- Columna Derecha: Imagen del Águila -->
        <div class="col-12 col-md-6 d-none d-md-block bg-image-side"></div>

    </div>

    <!-- Texto inferior fuera de la tarjeta -->
    <div class="text-center mt-4 px-3">
        <p class="text-dark m-0" style="font-size: 0.9rem;">
            Al continuar, aceptas usar MUA como marketplace universitario responsablemente.
        </p>
    </div>
</div>

<script src="assets/js/bootstrap.js"></script>
</body>
</html>