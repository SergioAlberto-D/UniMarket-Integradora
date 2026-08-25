<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mua - Administración de Reportes</title>

    <base href="${pageContext.request.contextPath}/">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-layout.css">
</head>
<body>

<!-- SIDEBAR REUTILIZABLE (Marcamos la opción active como 'reportes') -->
<jsp:include page="/includes/sidebar.jsp">
    <jsp:param name="active" value="reportes" />
</jsp:include>

<main class="main-content">
    <div class="topbar">
        <div class="topbar-left">
            <button type="button" class="btn-hamburger" id="btnHamburger">
                <i class="fa-solid fa-bars"></i>
            </button>
            <div>Administración &gt; <span style="color:#555;">Reportes</span></div>
        </div>
    </div>

    <div class="container">
        <div class="search-container">
            <i class="fa-solid fa-magnifying-glass search-icon"></i>
            <input type="text" id="inputBuscar" placeholder="Buscar por denunciante, denunciado o motivo...">
        </div>

        <div class="table-card">
            <h2 class="table-title">Reportes de Usuarios</h2>

            <div class="table-responsive">
                <table class="custom-table" id="tablaDatos">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Denunciante</th>
                        <th>Denunciado</th>
                        <th>Motivo</th>
                        <th>Estado</th>
                        <th style="text-align: right;">Acciones</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <%-- Caso sin reportes --%>
                        <c:when test="${empty listaReportes}">
                            <tr>
                                <td colspan="6" style="text-align: center; padding: 40px; color: #888;">
                                    No hay reportes registrados todavía.
                                </td>
                            </tr>
                        </c:when>

                        <%-- Listado de reportes --%>
                        <c:otherwise>
                            <c:forEach var="item" items="${listaReportes}">
                                <tr>
                                    <td>#<c:out value="${item.idReporte}"/></td>

                                    <td>
                                        <strong><c:out value="${item.idUsuarioDenuncianteFk}"/></strong>
                                    </td>

                                    <td>
                                        <span class="user-denunciado">
                                            <c:out value="${item.idUsuarioDenunciadoFk}"/>
                                        </span>
                                    </td>

                                    <td>
                                        <div class="motivo-box" title="${item.motivo}">
                                            <c:out value="${item.motivo}"/>
                                        </div>
                                    </td>

                                    <td>
                                        <c:choose>
                                            <c:when test="${item.estadoReporte eq 'Atendido'}">
                                                <span class="badge-estado badge-atendido">Atendido</span>
                                            </c:when>
                                            <c:when test="${item.estadoReporte eq 'Desestimado'}">
                                                <span class="badge-estado badge-desestimado">Desestimado</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge-estado badge-pendiente">Pendiente</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>

                                    <td style="text-align: right;">
                                        <div class="actions-wrapper">
                                            <form action="adminreportes" method="POST" style="display:inline;">
                                                <input type="hidden" name="accion" value="atender">
                                                <input type="hidden" name="idReporte" value="${item.idReporte}">
                                                <button type="submit" class="btn-action btn-atender" title="Marcar como Atendido">
                                                    <i class="fa-solid fa-check"></i>
                                                </button>
                                            </form>

                                            <form action="adminreportes" method="POST" style="display:inline;">
                                                <input type="hidden" name="accion" value="desestimar">
                                                <input type="hidden" name="idReporte" value="${item.idReporte}">
                                                <button type="submit" class="btn-action btn-desestimar" title="Desestimar reporte">
                                                    <i class="fa-solid fa-xmark"></i>
                                                </button>
                                            </form>
                                        </div>
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

<script src="${pageContext.request.contextPath}/assets/js/sidebar.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/buscador.js"></script>

</body>
</html>