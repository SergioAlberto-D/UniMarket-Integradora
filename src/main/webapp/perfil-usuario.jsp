<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.unimarket.unimarketintegradora.model.Usuario" %>

<%
    Usuario usuario = null;

    if (session != null && session.getAttribute("usuario") != null) {
        usuario = (Usuario) session.getAttribute("usuario");
    }

    if (usuario == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String nombres = usuario.getNombres() != null ? usuario.getNombres() : "";
    String apellidoPaterno = usuario.getApellidoPaterno() != null ? usuario.getApellidoPaterno() : "";
    String apellidoMaterno = usuario.getApellidoMaterno() != null ? usuario.getApellidoMaterno() : "";

    String nombreCompleto = (nombres + " " + apellidoPaterno + " " + apellidoMaterno).trim();

    if (nombreCompleto.isEmpty()) {
        nombreCompleto = "Usuario";
    }

    String correo = usuario.getCorreoInstitucional() != null ? usuario.getCorreoInstitucional() : "Correo no registrado";
    String telefono = usuario.getTelefono() != null ? usuario.getTelefono() : "Teléfono no registrado";
    String carrera = usuario.getCarrera() != null ? usuario.getCarrera() : "Carrera no registrada";

    String iniciales = "";

    if (!nombres.isEmpty()) {
        iniciales += nombres.substring(0, 1).toUpperCase();
    }

    if (!apellidoPaterno.isEmpty()) {
        iniciales += apellidoPaterno.substring(0, 1).toUpperCase();
    }

    if (iniciales.isEmpty()) {
        iniciales = "U";
    }
%>

<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Perfil de usuario - MUA</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" href="<%= request.getContextPath() %>/static/img/logoMUA.png" type="image/png">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/bootstrap.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/bi_s/bootstrap-icons.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/perfil-usuario.css?v=3">
</head>

<body>

<main class="profile-page">

    <section class="profile-title">
        <h1>Perfil de usuario</h1>
        <p>Consulta y administra tu información personal</p>
    </section>

    <section class="profile-summary">

        <div class="profile-avatar">
            <%= iniciales %>
        </div>

        <div class="profile-info">
            <h2><%= nombreCompleto %></h2>

            <p>
                <i class="bi bi-mortarboard"></i>
                <%= carrera %>
            </p>

            <p>
                <i class="bi bi-telephone"></i>
                <%= telefono %>
            </p>

            <p>
                <i class="bi bi-envelope"></i>
                <%= correo %>
            </p>
        </div>

        <div class="profile-stats">
            <article>
                <i class="bi bi-arrow-left-right"></i>
                <strong>0</strong>
                <span>Compras<br>Completadas</span>
            </article>

            <article>
                <i class="bi bi-chat-left-text"></i>
                <strong>0</strong>
                <span>Comentarios<br>Realizados</span>
            </article>
        </div>

    </section>

    <section class="profile-grid">

        <section class="profile-card offers-card">

            <div class="card-heading">
                <div class="card-icon">
                    <i class="bi bi-bell"></i>
                </div>

                <div>
                    <h2>Ofertas Realizadas</h2>
                    <p>Revisa y responde las propuestas de compradores.</p>
                </div>
            </div>

            <div class="empty-state">
                <i class="bi bi-inbox"></i>
                <h3>Aún no tienes ofertas</h3>
                <p>Cuando algún comprador haga una oferta por tus artículos, aparecerá aquí.</p>
            </div>

        </section>

        <section class="profile-card settings-card">

            <div class="card-heading">
                <div class="card-icon">
                    <i class="bi bi-gear"></i>
                </div>

                <div>
                    <h2>Configuración de cuenta</h2>
                    <p>Acciones rápidas para administrar tu cuenta.</p>
                </div>
            </div>

            <div class="settings-list">

                <a href="historial-actividad.jsp" class="setting-option">
                    <div class="setting-left">
                        <i class="bi bi-clock"></i>

                        <span>
                            <strong>Historial de Actividad</strong>
                            <small>Revisa tus últimas compras</small>
                        </span>
                    </div>

                    <i class="bi bi-arrow-right option-arrow"></i>
                </a>

                <button type="button" class="setting-option" onclick="abrirModal('modalTelefono')">
                    <div class="setting-left">
                        <i class="bi bi-telephone"></i>

                        <span>
                            <strong>Cambiar teléfono</strong>
                            <small>Actualiza tu número de contacto</small>
                        </span>
                    </div>

                    <i class="bi bi-arrow-right option-arrow"></i>
                </button>

                <a href="cambiar-password-perfil.jsp" class="setting-option">
                    <div class="setting-left">
                        <i class="bi bi-lock"></i>

                        <span>
                            <strong>Cambiar contraseña</strong>
                            <small>Mantén tu cuenta segura</small>
                        </span>
                    </div>

                    <i class="bi bi-arrow-right option-arrow"></i>
                </a>

                <button type="button" class="setting-option" onclick="abrirModal('modalCerrarSesion')">
                    <div class="setting-left">
                        <i class="bi bi-box-arrow-right"></i>

                        <span>
                            <strong>Cerrar sesión</strong>
                            <small>Finaliza tu sesión actual</small>
                        </span>
                    </div>

                    <i class="bi bi-arrow-right option-arrow"></i>
                </button>

                <button type="button" class="setting-option danger-option" onclick="abrirModal('modalEliminarCuenta')">
                    <div class="setting-left">
                        <i class="bi bi-trash"></i>

                        <span>
                            <strong>Eliminar cuenta</strong>
                            <small>Esta acción no se puede deshacer</small>
                        </span>
                    </div>

                    <i class="bi bi-arrow-right option-arrow"></i>
                </button>

            </div>

        </section>

    </section>

</main>

<div class="modal-overlay" id="modalTelefono">
    <div class="floating-modal phone-modal">

        <h2>Cambiar número celular</h2>
        <p>Acción necesaria para poder ocupar el sistema de compra y venta</p>

        <label for="telefonoNuevo">Teléfono</label>
        <input id="telefonoNuevo" type="text" placeholder="Ejem: 777-222-2222">

        <div class="phone-modal-actions">
            <button type="button" class="modal-cancel-button" onclick="cerrarModal('modalTelefono')">
                Cancelar
            </button>

            <button type="button" class="brown-button" onclick="cerrarModal('modalTelefono')">
                Cambiar número
            </button>
        </div>

    </div>
</div>

<div class="modal-overlay" id="modalEliminarCuenta">
    <div class="floating-modal confirm-modal">

        <h2>Eliminar cuenta</h2>
        <p>Esta acción no se puede deshacer una vez se acepte. ¿Estás seguro de eliminar tu cuenta?</p>

        <div class="modal-actions">
            <button type="button" class="green-button" onclick="cerrarModal('modalEliminarCuenta')">
                Cancelar
            </button>

            <button type="button" class="red-button" onclick="cerrarModal('modalEliminarCuenta')">
                Eliminar
            </button>
        </div>

    </div>
</div>

<div class="modal-overlay" id="modalCerrarSesion">
    <div class="floating-modal confirm-modal">

        <h2>Cerrar sesión</h2>
        <p>¿Estás seguro de cerrar tu sesión?</p>

        <div class="modal-actions">
            <button type="button" class="green-button" onclick="cerrarModal('modalCerrarSesion')">
                Cancelar
            </button>

            <a href="<%= request.getContextPath() %>/logout" class="red-button">
                Cerrar sesión
            </a>
        </div>

    </div>
</div>

<script>
    function abrirModal(id) {
        document.getElementById(id).classList.add("show-modal");
    }

    function cerrarModal(id) {
        document.getElementById(id).classList.remove("show-modal");
    }
</script>

</body>
</html>