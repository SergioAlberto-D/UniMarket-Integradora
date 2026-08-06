<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="sidebar-overlay" id="sidebarOverlay"></div>

<aside class="sidebar" id="sidebar">
  <div class="brand-section">
    <img src="static/img/logoMUA.png" alt="Logo Mua" class="brand-logo">
    <h1 class="brand-name">Mua</h1>
    <p class="brand-sub">Administración</p>
  </div>

  <ul class="menu-list">
    <li class="menu-item ${param.active == 'actividad' ? 'active' : ''}">
      <a href="adminactividad">Actividad reciente</a>
    </li>
    <li class="menu-item ${param.active == 'publicaciones' ? 'active' : ''}">
      <a href="adminpublicaciones">Publicaciones</a>
    </li>
    <li class="menu-item ${param.active == 'usuarios' ? 'active' : ''}">
      <a href="adminusuarios">Usuarios</a>
    </li>
    <li class="menu-item ${param.active == 'categorias' ? 'active' : ''}">
      <a href="admincategorias">Categorías</a>
    </li>
  </ul>

  <div class="sidebar-footer">
    <div class="user-profile-summary">
      <div class="avatar-circle">
        <c:out value="${fn:substring(sessionScope.adminLogueado.nombre, 0, 1)}${fn:substring(sessionScope.adminLogueado.apellidoPaterno, 0, 1)}" default="RH"/>
      </div>
      <div class="profile-info">
        <h4><c:out value="${sessionScope.adminLogueado.nombre} ${sessionScope.adminLogueado.apellidoPaterno}" default="Rafael Hurtado"/></h4>
        <p><c:out value="${sessionScope.adminLogueado.correoInstitucional}" default="rafaelhurtado@utez.edu.mx"/></p>
      </div>
    </div>
    <form action="LogoutServlet" method="POST">
      <button type="submit" class="btn-logout">Cerrar sesión</button>
    </form>
  </div>
</aside>