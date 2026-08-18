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
        <div class="right-avatar">
            <c:out value="${fn:substring(sessionScope.adminLogueado.nombre, 0, 1)}${fn:substring(sessionScope.adminLogueado.apellidoPaterno, 0, 1)}" default="AD"/>
        </div>
    </div>

    <div class="container">

        <!-- Formulario Agregar Categoría -->
        <div class="table-card" style="margin-bottom: 25px;">
            <h2 class="table-title">Agregar nueva categoría</h2>
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
                     Guardar
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
            <h2 class="table-title">Listado de categorías</h2>

            <div class="table-responsive">
                <table class="custom-table" id="tablaDatos">
                    <thead>
                    <tr>
                        <th style="width: 10%;">#</th>
                        <th>Nombre de categoría</th>
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
                                <tr>
                                    <!-- Numeración consecutiva continua sin huecos -->
                                    <td><strong><c:out value="${loop.count}"/></strong></td>
                                    <td><strong><c:out value="${cat.categoria}"/></strong></td>
                                    <td style="text-align: right;">
                                        <div style="display: flex; gap: 8px; justify-content: flex-end;">
                                            <!-- BOTÓN EDITAR (Abre el modal) -->
                                            <button type="button"
                                                    class="btn-update"
                                                    onclick="abrirModal('${cat.idCategoria}', '${cat.categoria}')">
                                                Editar
                                            </button>

                                            <!-- BOTÓN ELIMINAR -->
                                            <form action="${pageContext.request.contextPath}/admincategorias" method="POST" style="margin:0;">
                                                <input type="hidden" name="accion" value="eliminar">
                                                <input type="hidden" name="idCategoria" value="${cat.idCategoria}">
                                                <button type="submit"
                                                        class="btn-delete"
                                                        onclick="return confirm('Al eliminar la categoría \'${cat.categoria}\', sus productos asociados pasarán a la categoría \'Otros\'. ¿Deseas continuar?');">
                                                    Eliminar
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

<!-- VENTANA FLOTANTE (MODAL EDITAR) -->
<div id="modalEditar" class="modal-overlay">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Editar categoría</h3>
            <button type="button" class="btn-close-modal" onclick="cerrarModal()">&times;</button>
        </div>

        <form action="${pageContext.request.contextPath}/admincategorias" method="POST">
            <input type="hidden" name="accion" value="editar">
            <input type="hidden" id="modalIdCategoria" name="idCategoria">

            <div class="modal-body-group">
                <label for="modalNombreCategoria">Nombre de la categoría</label>
                <input type="text" id="modalNombreCategoria" name="nombreCategoria" required>
            </div>

            <div class="modal-actions">
                <button type="button" class="btn-cancelar" onclick="cerrarModal()">Cancelar</button>
                <button type="submit" class="btn-guardar-modal">Guardar Cambios</button>
            </div>
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/sidebar.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/buscador.js"></script>

<script>
    // Guardar Categoría vía AJAX
    document.getElementById('formAgregarCategoria').addEventListener('submit', function(e) {
        e.preventDefault();

        const inputNombre = document.getElementById('inputNombreCategoria');
        const nombreVal = inputNombre.value.trim();
        if (!nombreVal) return;

        const formData = new URLSearchParams();
        formData.append('accion', 'agregar');
        formData.append('nombreCategoria', nombreVal);

        fetch('${pageContext.request.contextPath}/admincategorias', {
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

    // Control del Modal de Edición
    function abrirModal(id, nombre) {
        document.getElementById('modalIdCategoria').value = id;
        document.getElementById('modalNombreCategoria').value = nombre;
        document.getElementById('modalEditar').style.display = 'flex';
    }

    function cerrarModal() {
        document.getElementById('modalEditar').style.display = 'none';
    }

    window.onclick = function(event) {
        const modal = document.getElementById('modalEditar');
        if (event.target === modal) {
            cerrarModal();
        }
    }
</script>

</body>
</html>