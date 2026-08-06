<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mua - Actividad Reciente</title>

    <base href="${pageContext.request.contextPath}/">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-layout.css">
</head>
<body>

<div class="sidebar-overlay" id="sidebarOverlay"></div>

<aside class="sidebar" id="sidebar">
    <div class="brand-section">
        <img src="static/img/logoMUA.png" alt="Logo Mua" class="brand-logo">
        <h1 class="brand-name">Mua</h1>
        <p class="brand-sub">Administración</p>
    </div>

    <ul class="menu-list">
        <li class="menu-item active"><a href="adminactividad">Actividad reciente</a></li>
        <li class="menu-item"><a href="adminpublicaciones">Publicaciones</a></li>
        <li class="menu-item"><a href="adminusuarios">Usuarios</a></li>
        <li class="menu-item"><a href="adminreportes">Reportes</a></li>
        <li class="menu-item"><a href="admincategorias">Categorías</a></li>
    </ul>

    <div class="sidebar-footer">
        <div class="user-profile-summary">
            <div class="avatar-circle">RH</div>
            <div class="profile-info">
                <h4>Rafael Hurtado</h4>
                <p>rafaelhurtado@utez.edu.mx</p>
            </div>
        </div>
        <form action="LogoutServlet" method="POST">
            <button type="submit" class="btn-logout">Cerrar sesión</button>
        </form>
    </div>
</aside>

<main class="main-content">
    <div class="topbar">
        <div class="topbar-left">
            <button class="btn-hamburger" id="btnHamburger">&#9776;</button>
            <div>Administración &gt; <span style="color:#555;">Actividad reciente</span></div>
        </div>
        <div class="right-avatar">RH</div>
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
        </div>

        <!-- Tabla de Actividad Reciente -->
        <div class="table-card">
            <h2 class="table-title">Actividad reciente</h2>

            <div class="table-responsive">
                <table class="custom-table">
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
                    <!-- Bucle que recorre la lista enviada por el Servlet -->
                    <c:forEach var="act" items="${listaActividad}">
                        <tr>
                            <td><c:out value="${act.usuario}"/></td>
                            <td><c:out value="${act.correo}"/></td>
                            <td><span class="badge-categoria"><c:out value="${act.modulo}"/></span></td>
                            <td><c:out value="${act.accion}"/></td>
                            <td><c:out value="${act.fecha}"/></td>
                        </tr>
                    </c:forEach>

                    <!-- Mensaje si no hay registros -->
                    <c:if test="${empty listaActividad}">
                        <tr>
                            <td colspan="5" style="text-align: center; padding: 30px; color: #888;">
                                No hay actividad registrada por el momento.
                            </td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>

    </div>
</main>

<script>
    // Script para soporte del menú colapsable en móviles
    const btnHamburger = document.getElementById('btnHamburger');
    const sidebar = document.getElementById('sidebar');
    const sidebarOverlay = document.getElementById('sidebarOverlay');

    if (btnHamburger && sidebar && sidebarOverlay) {
        btnHamburger.addEventListener('click', () => {
            sidebar.classList.toggle('active');
            sidebarOverlay.classList.toggle('active');
        });

        sidebarOverlay.addEventListener('click', () => {
            sidebar.classList.remove('active');
            sidebarOverlay.classList.remove('active');
        });
    }
</script>

</body>
</html>