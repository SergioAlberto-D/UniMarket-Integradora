<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mua - Administración de Usuarios</title>

    <base href="${pageContext.request.contextPath}/">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-layout.css">
    <!-- FontAwesome para cargar la hamburguesa y la lupa -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body>

<div class="sidebar-overlay" id="sidebarOverlay"></div>

<!-- AQUÍ SE INCLUYE EL SIDEBAR -->
<jsp:include page="/includes/sidebar.jsp">
    <jsp:param name="active" value="usuarios" />
</jsp:include>

<main class="main-content">
    <div class="topbar">
        <div class="topbar-left">
            <button type="button" class="btn-hamburger" id="btnHamburger">
                <i class="fa-solid fa-bars"></i>
            </button>
            <div>Administración &gt; <span style="color:#555;">Usuarios</span></div>
        </div>
    </div>

    <div class="container">
        <div class="search-container">
            <i class="fa-solid fa-magnifying-glass search-icon"></i>
            <input type="text" id="inputBuscar" placeholder="Buscar">
        </div>

        <div class="table-card">
            <h2 class="table-title">Usuarios</h2>

            <div class="table-responsive">
                <table class="custom-table" id="tablaDatos">
                    <thead>
                    <tr>
                        <th>Usuario</th>
                        <th>Correo</th>
                        <th>Teléfono</th>
                        <th>División</th>
                        <th style="text-align: right;">Acción</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty listaUsuarios}">
                            <tr>
                                <td colspan="5" style="text-align: center; padding: 40px; color: #888;">
                                    No hay usuarios registrados por el momento.
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="usuario" items="${listaUsuarios}">
                                <tr>
                                    <td>
                                        <c:out value="${usuario.nombre} ${usuario.apellidoPaterno} ${usuario.apellidoMaterno}"/>
                                    </td>
                                    <td><c:out value="${usuario.correoInstitucional}"/></td>
                                    <td><c:out value="${usuario.numeroCelular}"/></td>
                                    <td>
                                        <span class="badge-categoria">
                                            <!-- Muestra el nombre real de la división obtenido desde el mapa de la base de datos -->
                                            <c:out value="${mapaDivisiones[usuario.idDivisionAcademicaFk]}" default="Otra"/>
                                        </span>
                                    </td>

                                    <td style="text-align: right;">
                                        <form action="${pageContext.request.contextPath}/adminusuarios" method="POST" style="margin:0;">
                                            <input type="hidden" name="accion" value="eliminar">
                                            <input type="hidden" name="matricula" value="${usuario.matricula}">
                                            <button type="submit" class="btn-delete" onclick="return confirm('¿Deseas dar de baja a este usuario?');">
                                                Eliminar
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</main>

<!-- JS para el menú hamburguesa -->
<script src="${pageContext.request.contextPath}/assets/js/sidebar.js"></script>
<!-- JS para el buscador de la tabla -->
<script src="${pageContext.request.contextPath}/assets/js/buscador.js"></script>

</body>
</html>si