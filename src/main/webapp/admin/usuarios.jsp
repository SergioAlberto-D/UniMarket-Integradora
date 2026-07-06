<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Mua - Administración de Usuarios</title>

  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/Usuarios.css">


</head>
<body>

<aside class="sidebar">
  <div class="brand-section">
    <img src="${pageContext.request.contextPath}/static/img/logoMUA.png" alt="Logo Mua"
         class="brand-logo">
    <h1 class="brand-name">Mua</h1>
    <p class="brand-sub">Administración</p>
  </div>

    <ul class="menu-list">
      <li class="menu-item"><a href="adminactividad">Actividad reciente</a></li>
      <li class="menu-item"><a href="adminpublicaciones">Publicaciones</a></li>
      <li class="menu-item active"><a href="adminusuarios">Usuarios</a></li>
      <li class="menu-item"><a href="adminreportes">Reportes</a></li>
      <li class="menu-item"><a href="admincategorias">Categorías</a></li>
    </ul>
  </div>

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
    <div>Administración &gt; <span style="color:#555;">Usuarios</span></div>
    <div class="right-avatar">RH</div>
  </div>

  <div class="container">
    <div class="search-container">
      <i class="fa-solid fa-magnifying-glass"></i>
      <input type="text" placeholder="Buscar">
    </div>

    <div class="table-card">
      <h2 class="table-title">Usuarios</h2>

      <table class="custom-table">
        <thead>
        <tr>
          <th>Usuario</th>
          <th>Correo</th>
          <th>Teléfono</th>
          <th>Carrera</th>
          <th>Contraseña</th>
          <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="usuario" items="${listaUsuarios}">
          <tr>
            <td>
              <c:out value="${usuario.nombres} ${usuario.apellidoPaterno} ${usuario.apellidoMaterno}"/>
            </td>

            <td><c:out value="${usuario.correoInstitucional}"/></td>

            <td><c:out value="${usuario.telefono}"/></td>

            <td>
        <span class="badge-carrera">
          <c:out value="${usuario.carrera}"/>
        </span>
            </td>

            <td>######</td>
            <td style="text-align: right;">
              <form action="${pageContext.request.contextPath}/EliminarUsuarioServlet" method="POST" style="margin:0;">
                <input type="hidden" name="idUsuario" value="${usuario.idUsuario}">
                <button type="submit" class="btn-delete" onclick="return confirm('¿Eliminar usuario?');">
                  Eliminar
                </button>
              </form>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </div>
  </div>
</main>

</body>
</html>