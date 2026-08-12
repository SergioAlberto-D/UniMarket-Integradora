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
        <!-- Se eliminó right-avatar -->
    </div>

    <div class="container">

        <!-- Formulario con ID para JavaScript -->
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
                        <th style="width: 20%;">ID</th>
                        <th>Nombre de Categoría</th>
                        <th style="text-align: right; width: 20%;">Acciones</th>
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
                            <c:forEach var="cat" items="${listaCategorias}">
                                <tr>
                                    <td><c:out value="${cat.idCategoria}"/></td>
                                    <td><strong><c:out value="${cat.categoria}"/></strong></td>
                                    <td style="text-align: right;">
                                        <form action="admincategorias" method="POST" style="margin:0;">
                                            <input type="hidden" name="accion" value="eliminar">
                                            <input type="hidden" name="idCategoria" value="${cat.idCategoria}">
                                            <button type="submit"
                                                    class="btn-delete"
                                                    onclick="return confirm('¿Seguro que deseas eliminar la categoría \'${cat.categoria}\'?');">
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

<script src="${pageContext.request.contextPath}/assets/js/sidebar.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/buscador.js"></script>

<!-- Script AJAX para guardar sin recargar la página -->
<script>
    document.getElementById('formAgregarCategoria').addEventListener('submit', function(e) {
        e.preventDefault();

        const inputNombre = document.getElementById('inputNombreCategoria');
        const nombreVal = inputNombre.value.trim();
        if (!nombreVal) return;

        const formData = new URLSearchParams();
        formData.append('accion', 'agregar');
        formData.append('nombreCategoria', nombreVal);

        fetch('admincategorias', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: formData
        })
            .then(response => {
                if (!response.ok) throw new Error('Error en el servidor');
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    // Recarga la página actualizando la tabla con los datos de Oracle
                    window.location.reload();
                } else {
                    alert('Error al guardar la categoría.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ocurrió un error al procesar la solicitud.');
            });
    });
</script>

</body>
</html>