<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel Admin - Peticiones de Registro</title>

    <base href="${pageContext.request.contextPath}/">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-layout.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body>

<!-- Overlay para responsive -->
<div class="sidebar-overlay" id="sidebarOverlay"></div>

<!-- Sidebar del Administrador -->
<jsp:include page="/includes/sidebar.jsp">
    <jsp:param name="active" value="actividad" />
</jsp:include>

<!-- Contenido Principal -->
<main class="main-content">

    <!-- Barra Superior -->
    <div class="topbar">
        <div class="topbar-left">
            <button type="button" class="btn-hamburger" id="btnHamburger">
                <i class="fa-solid fa-bars"></i>
            </button>
            <div>Administración &gt; <span style="color:#555;">Peticiones de Registro</span></div>
        </div>
    </div>

    <!-- Contenedor de la Tabla -->
    <div class="container mt-4">
        <div class="table-card">
            <h2 class="table-title">Usuarios Pendientes de Verificación</h2>

            <div class="table-responsive">
                <table class="custom-table" id="tablaPeticiones">
                    <thead>
                    <tr>
                        <th style="width: 15%;">Matrícula</th>
                        <th>Nombre Completo</th>
                        <th>Correo Institucional</th>
                        <th style="text-align: right; width: 15%;">Acciones</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty listaPeticiones}">
                            <tr>
                                <td colspan="4" style="text-align: center; padding: 40px; color: #888;">
                                    No hay peticiones de registro pendientes en este momento.
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="user" items="${listaPeticiones}">
                                <tr id="row-${user.matricula}">
                                    <td><strong><c:out value="${user.matricula}"/></strong></td>
                                    <td><c:out value="${user.nombre} ${user.apellidoPaterno}"/></td>
                                    <td><c:out value="${user.correoInstitucional}"/></td>
                                    <td style="text-align: right;">
                                        <button type="button"
                                                class="btn-update"
                                                onclick="abrirModalPeticion('${user.matricula}', '${user.nombre} ${user.apellidoPaterno}', '${user.correoInstitucional}', '${user.fotoCredencialFrente}', '${user.fotoCredencialReverso}')">
                                            Ver detalles
                                        </button>
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

<!-- MODAL FLOTANTE (Usando tu admin-layout.css) -->
<div id="modalPeticion" class="modal-overlay">
    <div class="modal-content" style="max-width: 650px;">
        <div class="modal-header">
            <h3>Detalles de Verificación de Identidad</h3>
            <button type="button" class="btn-close-modal" onclick="cerrarModal()">&times;</button>
        </div>

        <div class="modal-body-group" style="color: #555;">
            <p style="margin-bottom: 5px;"><strong>Matrícula:</strong> <span id="mod-matricula" style="color: #222;"></span></p>
            <p style="margin-bottom: 5px;"><strong>Nombre:</strong> <span id="mod-nombre" style="color: #222;"></span></p>
            <p style="margin-bottom: 15px;"><strong>Correo:</strong> <span id="mod-correo" style="color: #222;"></span></p>

            <div style="display: flex; gap: 15px; margin-top: 15px;">
                <div style="flex: 1; text-align: center;">
                    <h6 style="margin-bottom: 10px; color: #333; font-size: 14px;">Credencial (Frente)</h6>
                    <img id="img-frente" src="" alt="Frente" style="width: 100%; border: 1px solid #ccc; border-radius: 8px; object-fit: contain; background: #fafafa; height: 180px;">
                </div>
                <div style="flex: 1; text-align: center;">
                    <h6 style="margin-bottom: 10px; color: #333; font-size: 14px;">Credencial (Reverso)</h6>
                    <img id="img-reverso" src="" alt="Reverso" style="width: 100%; border: 1px solid #ccc; border-radius: 8px; object-fit: contain; background: #fafafa; height: 180px;">
                </div>
            </div>
        </div>

        <div class="modal-actions" style="justify-content: space-between; margin-top: 25px;">
            <button type="button" class="btn-delete" id="btn-rechazar">Rechazar Petición</button>
            <button type="button" class="btn-guardar-modal" id="btn-aceptar" style="background-color: #2E7D32;">Aceptar Petición</button>
        </div>
    </div>
</div>

<!-- Scripts -->
<script src="${pageContext.request.contextPath}/assets/js/sidebar.js"></script>
<script>
    const contextPath = "${pageContext.request.contextPath}";
    let matriculaActual = '';
    let correoActual = '';

    // Función para abrir el modal usando tu diseño CSS
    function abrirModalPeticion(matricula, nombre, correo, fotoFrente, fotoReverso) {
        matriculaActual = matricula;
        correoActual = correo;

        document.getElementById('mod-matricula').textContent = matricula;
        document.getElementById('mod-nombre').textContent = nombre;
        document.getElementById('mod-correo').textContent = correo;

        document.getElementById('img-frente').src = contextPath + "/" + fotoFrente;
        document.getElementById('img-reverso').src = contextPath + "/" + fotoReverso;

        document.getElementById('modalPeticion').style.display = 'flex';
    }

    // Función para cerrar el modal
    function cerrarModal() {
        document.getElementById('modalPeticion').style.display = 'none';
    }

    // Cerrar al hacer clic fuera del modal
    window.onclick = function(event) {
        const modal = document.getElementById('modalPeticion');
        if (event.target === modal) {
            cerrarModal();
        }
    }

    // Función AJAX para Aceptar o Rechazar
    function procesarPeticion(accion) {
        let mensajeConfirmacion = accion === 'aceptar'
            ? '¿Estás seguro de que la identidad es válida? Se activará la cuenta de este usuario y se le enviará un correo.'
            : '¿Estás seguro de rechazar y ELIMINAR PERMANENTEMENTE esta petición?';

        if(!confirm(mensajeConfirmacion)) return;

        fetch(contextPath + '/adminactividad', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: new URLSearchParams({
                'accion': accion,
                'matricula': matriculaActual,
                'correo': correoActual
            })
        })
            .then(response => response.json())
            .then(data => {
                if(data.exito) {
                    alert(data.mensaje);
                    cerrarModal();
                    // Eliminar visualmente la fila de la tabla sin recargar toda la página
                    const fila = document.getElementById('row-' + matriculaActual);
                    if(fila) fila.remove();
                } else {
                    alert(data.mensaje);
                }
            })
            .catch(err => {
                console.error('Error:', err);
                alert("Ocurrió un error al procesar la petición. Revisa la consola.");
            });
    }

    document.getElementById('btn-aceptar').addEventListener('click', () => procesarPeticion('aceptar'));
    document.getElementById('btn-rechazar').addEventListener('click', () => procesarPeticion('rechazar'));
</script>

</body>
</html>