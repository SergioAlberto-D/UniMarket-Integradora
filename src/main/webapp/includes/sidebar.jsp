<%--
  DOCUMENTACIÓN TÉCNICA — MUA
  Archivo: src/main/webapp/includes/sidebar.jsp
  Propósito: Recurso de vista JSP para el módulo sidebar. Integra HTML, JSTL y/o expresiones JSP según el contenido fuente.
  Integración: la vista recibe datos desde Servlets mediante request/session y utiliza recursos CSS/JS del proyecto.
  Created by IntelliJ IDEA.
  User: Dulce
  Date: 8/10/26
  Time: 8:45 AM
--%>
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
        <c:out value="${fn:toUpperCase(fn:substring(sessionScope.admin.nombre, 0, 1))}" default="A"/>
      </div>
      <div class="profile-info">
        <p><c:out value="${sessionScope.admin.correo}" default="admin@utez.edu.mx"/></p>
      </div>
    </div>
    <form action="logout" method="POST">
      <button type="submit" class="btn-logout">Cerrar sesión</button>
    </form>
  </div>
</aside>
