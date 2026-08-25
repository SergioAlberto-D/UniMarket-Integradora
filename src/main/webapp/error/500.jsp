<%--
  DOCUMENTACIÓN TÉCNICA — MUA
  Archivo: src/main/webapp/error/500.jsp
  Propósito: Recurso de vista JSP para el módulo 500. Integra HTML, JSTL y/o expresiones JSP según el contenido fuente.
  Integración: la vista recibe datos desde Servlets mediante request/session y utiliza recursos CSS/JS del proyecto.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>500 - Error del sistema  MUA</title>
    <link rel="stylesheet"  href="../assets/css/manageoferrors.css">
</head>
<body>
<main class="error-card">
    <div class="error-code">500</div>
    <h1>Ocurrió un error en el sistema</h1>
    <p>
        Algo inesperado ocurrió mientras procesábamos tu solicitud.
        Por favor, inténtalo nuevamente. Si el problema continúa,
        ponte en contacto con soporte.
    </p>
    <a class="btn" href="${pageContext.request.contextPath}/inicio">Volver al inicio</a>
    <div class="hint">MUA · University Marketplace · Código de error: 500</div>
</main>
</body>
</html>
