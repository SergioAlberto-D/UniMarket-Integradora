<%--
  DOCUMENTACIÓN TÉCNICA — MUA
  Archivo: src/main/webapp/admin/categoria.jsp
  Propósito: Recurso de vista JSP para el módulo categoria. Integra HTML, JSTL y/o expresiones JSP según el contenido fuente.
  Integración: la vista recibe datos desde Servlets mediante request/session y utiliza recursos CSS/JS del proyecto.
  Created by IntelliJ IDEA.
  User: Dulce
  Date: 8/10/26
  Time: 8:45 AM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mua - Administración de Categorías</title>

    <base href="${pageContext.request.contextPath}/">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-layout.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body>

<div class="sidebar-overlay" id="sidebarOverlay"></div>

<jsp:include page="/includes/sidebar.jsp">
    <jsp:param name="active" value="categorias" />
</jsp:include>

<main class="main-content">
    <div class="topbar">
        <div class="topbar-left">
            <button type="button" class="btn-hamburger" id="btnHamburger">
                <i class="fa-solid fa-bars"></i>
            </button>
            <div>Administración &gt; <span style="color:#555;">Categorías</span></div>
        </div>
    </div>

    <div class="container">

        <!-- Formulario Agregar Categoría -->
        <div class="table-card" style="margin-bottom: 25px;">
            <h2 class="table-title">Agregar Nueva Categoría</h2>
            <form id="formAgregarCategoria" style="display: flex; gap: 15px; align-items: center; margin-top: 15px;">
                <input type="hidden" name="accion" value="agregar">

                <input type="text"
                       id="inputNombreCategoria"
                       name="nombreCategoria"
                       placeholder="Nombre de la categoría"
                       required
                       style="flex: 1; padding: 10px 15px; border: 1px solid #ccc; border-radius: 6px; outline: none; font-size: 14px;">

                <button type="submit"
                        class="btn-delete"
                        style="background-color: #8B5E3C; color: white; cursor: pointer; padding: 10px 20px;">
                    <i class="fa-solid fa-plus"></i> Guardar
                </button>
            </form>
        </div>

        <!-- Buscador -->
        <div class="search-container">
            <i class="fa-solid fa-magnifying-glass search-icon"></i>
            <input type="text" id="inputBuscar" placeholder="Buscar categoría">
        </div>

        <!-- Tabla de Categorías -->
        <div class="table-card">
            <h2 class="table-title">Listado de Categorías</h2>

            <div class="table-responsive">
                <table class="custom-table" id="tablaDatos">
                    <thead>
                    <tr>
                        <th style="width: 10%;">#</th>
                        <th>Nombre de Categoría</th>
                        <th style="text-align: right; width: 30%;">Acciones</th>
                    </tr>
                    </thead>
                    <tbody id="cuerpoTablaCategorias">
                    <c:choose>
                        <c:when test="${empty listaCategorias}">
                            <tr id="rowSinDatos">
                                <td colspan="3" style="text-align: center; padding: 40px; color: #888;">
                                    No hay categorías registradas.
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="cat" items="${listaCategorias}" varStatus="loop">
                                <tr data-id="${cat.idCategoria}">
                                    <!-- Numeración consecutiva continua sin huecos -->
                                    <td class="col-numero"><strong><c:out value="${loop.count}"/></strong></td>
                                    <td class="col-nombre"><strong><c:out value="${cat.categoria}"/></strong></td>
                                    <td style="text-align: right;">
                                        <div style="display: flex; gap: 8px; justify-content: flex-end;">
                                            <!-- BOTÓN EDITAR (Abre el modal) -->
                                            <button type="button"
                                                    class="btn-update btn-editar-categoria"
                                                    data-id="${cat.idCategoria}"
                                                    data-nombre="${fn:escapeXml(cat.categoria)}">
                                                Editar
                                            </button>

                                            <!-- BOTÓN ELIMINAR -->
                                            <button type="button"
                                                    class="btn-delete btn-eliminar-categoria"
                                                    data-id="${cat.idCategoria}"
                                                    data-nombre="${fn:escapeXml(cat.categoria)}">
                                                Eliminar
                                            </button>
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

<!-- VENTANA FLOTANTE (MODAL EDITAR) -->
<div id="modalEditar" class="modal-overlay">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Editar Categoría</h3>
            <button type="button" class="btn-close-modal" id="btnCerrarModal">&times;</button>
        </div>

        <form id="formEditarCategoria">
            <input type="hidden" name="accion" value="editar">
            <input type="hidden" id="modalIdCategoria" name="idCategoria">

            <div class="modal-body-group">
                <label for="modalNombreCategoria">Nombre de la Categoría</label>
                <input type="text" id="modalNombreCategoria" name="nombreCategoria" required>
            </div>

            <div class="modal-actions">
                <button type="button" class="btn-cancelar" id="btnCancelarModal">Cancelar</button>
                <button type="submit" class="btn-guardar-modal">Guardar Cambios</button>
            </div>
        </form>
    </div>
</div>

<script>
    window.MUA_CTX = {
        contextPath: '${pageContext.request.contextPath}'
    };
</script>
<script src="${pageContext.request.contextPath}/assets/js/sidebar.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/buscador.js"></script>
<script src="${pageContext.request.contextPath}/static/js/categoria.js"></script>

</body>
</html>
