<%--
  DOCUMENTACIÓN TÉCNICA — MUA
  Archivo: src/main/webapp/admin/publicaciones.jsp
  Propósito: Recurso de vista JSP para el módulo publicaciones. Integra HTML, JSTL y/o expresiones JSP según el contenido fuente.
  Integración: la vista recibe datos desde Servlets mediante request/session y utiliza recursos CSS/JS del proyecto.
  Created by IntelliJ IDEA.
  User: Dulce
  Date: 8/10/26
  Time: 8:45 AM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mua - Administración de Publicaciones</title>

    <base href="${pageContext.request.contextPath}/">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-layout.css">
    <!--  para la hamburguesa y la lupa -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body>

<div class="sidebar-overlay" id="sidebarOverlay"></div>

<!-- Sidebar  -->
<jsp:include page="/includes/sidebar.jsp">
    <jsp:param name="active" value="publicaciones" />
</jsp:include>

<main class="main-content">
    <div class="topbar">
        <div class="topbar-left">
            <button type="button" class="btn-hamburger" id="btnHamburger">
                <i class="fa-solid fa-bars"></i>
            </button>
            <div>Administración &gt; <span style="color:#555;">Publicaciones</span></div>
        </div>
        <div class="right-avatar">
            <c:out value="${fn:substring(sessionScope.adminLogueado.nombre, 0, 1)}${fn:substring(sessionScope.adminLogueado.apellidoPaterno, 0, 1)}" default="AD"/>
        </div>
    </div>

    <div class="container">
        <div class="search-container">
            <i class="fa-solid fa-magnifying-glass search-icon"></i>
            <input type="text" id="inputBuscar" placeholder="Buscar">
        </div>

        <div class="table-card">
            <h2 class="table-title">Publicaciones</h2>

            <div class="table-responsive">
                <table class="custom-table" id="tablaDatos">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Artículo</th>
                        <th>Precio</th>
                        <th>Categoría</th>
                        <th>Vendedor</th>
                        <th style="text-align: right;">Acción</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty listaArticulos}">
                            <tr>
                                <td colspan="6" style="text-align: center; padding: 40px; color: #888;">
                                    No hay publicaciones registradas por el momento.
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="art" items="${listaArticulos}">
                                <tr>
                                    <!-- Propiedad: art.idArticulo -->
                                    <td><c:out value="${art.idArticulo}"/></td>

                                    <!-- Propiedad: art.nombre -->
                                    <td><c:out value="${art.nombre}"/></td>

                                    <!-- Propiedad: art.precio -->
                                    <td>$<fmt:formatNumber value="${art.precio}" pattern="#,##0.00"/> MXN</td>

                                    <!-- Propiedad: art.idCategoriaFk -->
                                    <td>
                                        <span class="badge-categoria">
                                            Cat. <c:out value="${art.idCategoriaFk}"/>
                                        </span>
                                    </td>

                                    <!-- Muestra art.nombreUsuario si el DAO lo trajo, o cae de respaldo a art.idUsuarioFk -->
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty art.nombreUsuario}">
                                                <c:out value="${art.nombreUsuario}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${art.idUsuarioFk}"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>

                                    <!-- Acción enviada al Servlet via POST -->
                                    <td style="text-align: right;">
                                        <form action="adminpublicaciones" method="POST" style="margin:0;">
                                            <input type="hidden" name="accion" value="eliminar">
                                            <input type="hidden" name="idArticulo" value="${art.idArticulo}">
                                            <button type="submit" class="btn-delete" onclick="return confirm('¿Deseas dar de baja esta publicación?');">
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

<!-- Scripts -->
<script src="${pageContext.request.contextPath}/assets/js/sidebar.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/buscador.js"></script>

</body>
</html>
