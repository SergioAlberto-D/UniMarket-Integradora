<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.unimarket.unimarketintegradora.model.Usuario" %>

<%
  String uri = request.getRequestURI();

  boolean paginaBuscar = uri.endsWith("/") || uri.contains("index.jsp");
  boolean paginaPublicar = uri.contains("publicar-articulo");
  boolean paginaMisArticulos = uri.contains("mis-articulos");

  Usuario usuarioHeader = null;

  if (session != null && session.getAttribute("usuario") != null) {
    usuarioHeader = (Usuario) session.getAttribute("usuario");
  }

  String inicialesHeader = "U";

  if (usuarioHeader != null) {
    inicialesHeader = "";

    if (usuarioHeader.getNombres() != null && !usuarioHeader.getNombres().isEmpty()) {
      inicialesHeader += usuarioHeader.getNombres().substring(0, 1).toUpperCase();
    }

    if (usuarioHeader.getApellidoPaterno() != null && !usuarioHeader.getApellidoPaterno().isEmpty()) {
      inicialesHeader += usuarioHeader.getApellidoPaterno().substring(0, 1).toUpperCase();
    }

    if (inicialesHeader.isEmpty()) {
      inicialesHeader = "U";
    }
  }
%>

<header class="mua-header">
  <div class="header-left">
    <a href="<%= request.getContextPath() %>/index.jsp" class="brand">
      <div class="brand-logo">MUA</div>
      <span>Mua</span>
    </a>

    <div class="search-box">
      <i class="bi bi-search"></i>
      <input type="text" placeholder="Buscar artículos...">
    </div>
  </div>

  <nav class="header-nav">
    <a href="<%= request.getContextPath() %>/index.jsp"
       class="<%= paginaBuscar ? "nav-active" : "nav-inactive" %>">
      <i class="bi bi-house-door-fill"></i>
      Buscar
    </a>

    <a href="<%= request.getContextPath() %>/mis-articulos.jsp"
       class="<%= paginaMisArticulos ? "nav-active" : "nav-inactive" %>">
      <i class="bi bi-archive-fill"></i>
      Mis artículos
    </a>

    <a href="<%= request.getContextPath() %>/publicar-articulo.jsp"
       class="<%= paginaPublicar ? "nav-active" : "nav-inactive" %>">
      <i class="bi bi-plus-lg"></i>
      Publicar
    </a>

    <div class="profile-circle">
      <%= inicialesHeader %>
    </div>
  </nav>
</header>
