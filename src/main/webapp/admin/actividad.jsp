<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mua - Actividad Reciente</title>

    <base href="${pageContext.request.contextPath}/">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-layout.css">
    <!-- FontAwesome para cargar la hamburguesa y la lupa -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body>

<div class="sidebar-overlay" id="sidebarOverlay"></div>

<!-- INCLUSIÓN DINÁMICA DEL SIDEBAR -->
<jsp:include page="/includes/sidebar.jsp">
    <jsp:param name="active" value="actividad" />
</jsp:include>

<main class="main-content">
    <div class="topbar">
        <div class="topbar-left">
            <button type="button" class="btn-hamburger" id="btnHamburger">
                <i class="fa-solid fa-bars"></i>
            </button>
            <div>Administración &gt; <span style="color:#555;">Actividad reciente</span></div>
        </div>
        <div class="right-avatar">
            <c:out value="${fn:substring(sessionScope.adminLogueado.nombre, 0, 1)}${fn:substring(sessionScope.adminLogueado.apellidoPaterno, 0, 1)}" default="AD"/>
        </div>
    </div>

    <div class="container">

        <!-- Tarjetas KPI -->
        <div class="kpi-container">
            <div class="kpi-card">
                <h2><c:out value="${totalPublicaciones}" default="0"/></h2>
                <p>Artículos<br>publicados</p>
            </div>

            <div class="kpi-card">
                <h2><c:out value="${totalUsuarios}" default="0"/></h2>
                <p>Usuarios<br>registrados</p>
            </div>
        </div>

        <!-- Buscador -->
        <div class="search-container">
            <i class="fa-solid fa-magnifying-glass search-icon"></i>
            <input type="text" id="inputBuscar" placeholder="Buscar actividad...">
        </div>

        <!-- Tabla de Actividad Reciente -->
        <div class="table-card">
            <h2 class="table-title">Actividad reciente</h2>

            <div class="table-responsive">
                <table class="custom-table" id="tablaDatos">
                    <thead>
                    <tr>
                        <th>Usuario</th>
                        <th>Correo</th>
                        <th>Módulo</th>
                        <th>Acción</th>
                        <th>Fecha</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty listaActividad}">
                            <tr>
                                <td colspan="5" style="text-align: center; padding: 40px; color: #888;">
                                    No hay actividad registrada por el momento.
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="act" items="${listaActividad}">
                                <tr>
                                    <td><c:out value="${act.usuario}"/></td>
                                    <td><c:out value="${act.correo}"/></td>
                                    <td><span class="badge-categoria"><c:out value="${act.modulo}"/></span></td>
                                    <td><c:out value="${act.accion}"/></td>
                                    <td><c:out value="${act.fecha}"/></td>
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
</html>