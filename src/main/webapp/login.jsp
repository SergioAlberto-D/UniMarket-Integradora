<%--
  DOCUMENTACIÓN TÉCNICA — MUA
  Archivo: src/main/webapp/login.jsp
  Propósito: Recurso de vista JSP para el módulo login. Integra HTML, JSTL y/o expresiones JSP según el contenido fuente.
  Integración: la vista recibe datos desde Servlets mediante request/session y utiliza recursos CSS/JS del proyecto.
  Created by IntelliJ IDEA.
  User: sergio
  Date: 7/2/26
  Time: 8:28 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Iniciar sesión - MUA</title>
    <link rel="icon" href="<%= request.getContextPath() %>/static/img/logoMUA.png" type="image/png">
    <link rel="stylesheet" href="assets/css/bootstrap.css">
    <link rel="stylesheet" href="assets/css/bi_s/bootstrap-icons.css">
    <link rel="stylesheet" href="assets/css/Login.css">
</head>
<body class="login-page d-flex flex-column align-items-center justify-content-center">

<div class="container login-container px-0 px-sm-2">
    <div class="card shadow-lg border-0 card-custom login-card flex-row flex-wrap flex-md-nowrap">

        <!-- Imagen superior en movil -->
        <div class="col-12 d-md-none login-image-mobile"></div>

        <!-- Columna Izquierda: Formulario -->
        <div class="col-12 col-md-6 p-4 p-md-5 bg-white login-form-column">

            <!-- LOGO MUA -->
            <img src="static/img/logoMUA.png" alt="Logo MUA" class="login-logo mb-4 shadow-sm">

            <h2 class="fw-bold text-dark mb-1">Bienvenidos</h2>
            <p class="text-muted mb-4 login-subtitle">Inicia sesión con tu correo institucional</p>

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
                    <input type="text" class="form-control input-custom py-2" id="txtUsuario" name="email" placeholder="matricula@utez.edu.mx" required>
                </div>

                <!-- Contraseña -->
                <div class="mb-1">
                    <label for="txtPassword" class="form-label text-muted small mb-1">Contraseña</label>
                    <div class="position-relative">
                        <input type="password" class="form-control input-custom py-2 pe-5" id="txtPassword" name="password" placeholder="***********" required>
                        <i class="bi bi-eye-slash position-absolute top-50 end-0 translate-middle-y me-3 cursor-pointer toggle-password" data-target="txtPassword"></i>
                    </div>
                </div>

                <!-- Recuperar Contraseña -->
                <div class="text-end mb-4 mt-2">
                    <a href="recuperar-password.jsp" class="login-link login-forgot-link">¿Olvidaste tu contraseña?</a>
                </div>

                <!-- Botón Iniciar Sesión -->
                <button class="btn btn-brown w-100 py-2 fw-semibold shadow-sm mb-3" type="submit">
                    Iniciar Sesion
                </button>

                <!-- Registro -->
                <div class="text-center login-register-text mt-2">
                    <span class="text-muted">¿No tienes cuenta?</span>
                    <a href="registro.jsp" class="login-link ms-1">Regístrate</a>
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

<c:if test="${not empty cuentaVerificada or not empty errorVerificacion}">
    <div class="modal fade" id="modalVerificacion" tabindex="-1" aria-labelledby="modalVerificacionLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content border-0 shadow-lg" style="border-radius: 1rem;">
                <div class="modal-body text-center p-5">

                    <c:choose>
                        <%-- Si la cuenta fue verificada --%>
                        <c:when test="${not empty cuentaVerificada}">
                            <i class="bi bi-check-circle-fill text-success" style="font-size: 4.5rem;"></i>
                            <h3 class="fw-bold text-dark mt-3 mb-2">¡Cuenta Verificada!</h3>
                            <p class="text-muted mb-4 fs-6">${cuentaVerificada}</p>
                            <button type="button" class="btn btn-brown w-100 py-3 fw-semibold shadow-sm fs-5" data-bs-dismiss="modal">
                                Excelente, quiero entrar
                            </button>
                        </c:when>

                        <%-- Si hubo un error --%>
                        <c:when test="${not empty errorVerificacion}">
                            <i class="bi bi-x-circle-fill text-danger" style="font-size: 4.5rem;"></i>
                            <h3 class="fw-bold text-dark mt-3 mb-2">Ups, algo salió mal</h3>
                            <p class="text-muted mb-4 fs-6">${errorVerificacion}</p>
                            <button type="button" class="btn btn-peach w-100 py-3 fw-semibold shadow-sm fs-5" data-bs-dismiss="modal">
                                Cerrar
                            </button>
                        </c:when>
                    </c:choose>

                </div>
            </div>
        </div>
    </div>
</c:if>

<script src="assets/js/bootstrap.js"></script>
<script src="static/js/login.js"></script>
</body>
</html>
