<%--
  DOCUMENTACIÓN TÉCNICA — MUA
  Archivo: src/main/webapp/error/404.jsp
  Propósito: Recurso de vista JSP para el módulo 404. Integra HTML, JSTL y/o expresiones JSP según el contenido fuente.
  Integración: la vista recibe datos desde Servlets mediante request/session y utiliza recursos CSS/JS del proyecto.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>404 - Recurso no encontrado MUA</title>
    <link rel="stylesheet"  href="../assets/css/manageoferrors.css">/
</head>
<body>
<main class="error-card">
    <div class="error-code">404</div>
    <h1>Recurso no encontrado</h1>
    <p>
        El recurso o elemento que estás buscando no existe
        o se movió de lugar.
    </p>
    <a class="btn" href="${pageContext.request.contextPath}/inicio">Volver al inicio</a>
    <div class="hint">MUA · University Marketplace</div>
</main>
</body>
</html>
