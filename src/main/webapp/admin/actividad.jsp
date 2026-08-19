<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel Admin - Actividad</title>

    <base href="${pageContext.request.contextPath}/">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-layout.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-actividad.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body>

<div class="sidebar-overlay" id="sidebarOverlay"></div>

<jsp:include page="/includes/sidebar.jsp">
    <jsp:param name="active" value="actividad" />
</jsp:include>

<main class="main-content">

    <!-- ================================================================
         TOPBAR
         ================================================================ -->
    <div class="topbar">
        <div class="topbar-left">
            <button type="button" class="btn-hamburger" id="btnHamburger">
                <i class="fa-solid fa-bars"></i>
            </button>
            <div>
                Administración &gt;
                <span style="color:#555;">Actividad reciente</span>
            </div>
        </div>
    </div>

    <!-- ================================================================
         USUARIOS EN ESPERA
         ================================================================ -->
    <div class="container mt-4">
        <div class="table-card">

            <div class="seccion-titulo">
                <i class="fa-solid fa-user-clock"></i>
                <h2 class="table-title" style="margin:0;">Usuarios pendientes de verificación</h2>
                <span class="contador-espera">${empty listaPeticiones ? 0 : listaPeticiones.size()}</span>
            </div>

            <div class="table-responsive">
                <table class="custom-table" id="tablaPeticiones">
                    <thead>
                    <tr>
                        <th style="width:15%;">Matrícula</th>
                        <th>Nombre completo</th>
                        <th>Correo institucional</th>
                        <th>Estado</th>
                        <th style="text-align:right;">Acciones</th>
                    </tr>
                    </thead>
                    <tbody id="cuerpoPeticiones">
                    <c:choose>
                        <c:when test="${empty listaPeticiones}">
                            <tr>
                                <td colspan="5" style="text-align:center; padding:35px; color:#888;">
                                    <i class="fa-solid fa-circle-check" style="font-size:25px; margin-bottom:10px;"></i>
                                    <br>
                                    No hay usuarios pendientes de verificación.
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="user" items="${listaPeticiones}">
                                <tr id="row-user-${user.matricula}">
                                    <td>
                                        <strong><c:out value="${user.matricula}"/></strong>
                                    </td>
                                    <td>
                                        <c:out value="${user.nombre} ${user.apellidoPaterno}"/>
                                    </td>
                                    <td>
                                        <c:out value="${user.correoInstitucional}"/>
                                    </td>
                                    <td>
                                        <span class="badge-usuario">EN ESPERA</span>
                                    </td>
                                    <td style="text-align:right;">
                                        <button type="button"
                                                class="btn-update btn-ver-usuario"
                                                data-matricula="${fn:escapeXml(user.matricula)}"
                                                data-nombre="${fn:escapeXml(user.nombre)} ${fn:escapeXml(user.apellidoPaterno)}"
                                                data-correo="${fn:escapeXml(user.correoInstitucional)}"
                                                data-foto-frente="${fn:escapeXml(user.fotoCredencialFrente)}"
                                                data-foto-reverso="${fn:escapeXml(user.fotoCredencialReverso)}">
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

    <!-- ================================================================
         ARTÍCULOS EN ESPERA
         ================================================================ -->
    <div class="container mt-4" style="margin-bottom:40px;">
        <div class="table-card">

            <div class="seccion-titulo">
                <i class="fa-solid fa-box-open"></i>
                <h2 class="table-title" style="margin:0;">Artículos pendientes de verificación</h2>
                <span class="contador-espera">${empty listaArticulosEspera ? 0 : listaArticulosEspera.size()}</span>
            </div>

            <div class="table-responsive">
                <table class="custom-table" id="tablaArticulosEspera">
                    <thead>
                    <tr>
                        <th style="width:42%;">Artículo</th>
                        <th>Vendedor</th>
                        <th>Precio</th>
                        <th>Estado</th>
                        <th style="text-align:right;">Acciones</th>
                    </tr>
                    </thead>
                    <tbody id="cuerpoArticulosEspera">
                    <c:choose>
                        <c:when test="${empty listaArticulosEspera}">
                            <tr>
                                <td colspan="5" style="text-align:center; padding:35px; color:#888;">
                                    <i class="fa-solid fa-circle-check" style="font-size:25px; margin-bottom:10px;"></i>
                                    <br>
                                    No hay artículos pendientes de verificación.
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="articulo" items="${listaArticulosEspera}">
                                <tr id="row-articulo-${articulo.idArticulo}">

                                    <!-- ARTÍCULO -->
                                    <td>
                                        <div class="articulo-info">
                                            <c:choose>
                                                <c:when test="${not empty articulo.imagenPrincipal}">
                                                    <img class="articulo-imagen"
                                                         src="${pageContext.request.contextPath}/${articulo.imagenPrincipal}"
                                                         alt="Imagen del artículo">
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="articulo-imagen-vacia">
                                                        <i class="fa-solid fa-image"></i>
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>

                                            <div>
                                                <div class="articulo-titulo">
                                                    <c:out value="${articulo.nombre}"/>
                                                </div>
                                                <div class="articulo-vendedor">
                                                    ID artículo: <strong>#${articulo.idArticulo}</strong>
                                                </div>
                                                <c:if test="${not empty articulo.nombreCategoria}">
                                                    <div class="articulo-vendedor">
                                                        Categoría: <c:out value="${articulo.nombreCategoria}"/>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </div>
                                    </td>

                                    <!-- VENDEDOR -->
                                    <td>
                                        <c:out value="${articulo.nombreUsuario}"/>
                                        <br>
                                        <small style="color:#888;">
                                            Matrícula: <c:out value="${articulo.idUsuarioFk}"/>
                                        </small>
                                    </td>

                                    <!-- PRECIO -->
                                    <td>
                                            <span class="articulo-precio">
                                                $<c:out value="${articulo.precio}"/> MXN
                                            </span>
                                    </td>

                                    <!-- ESTADO -->
                                    <td>
                                        <span class="badge-espera">ESPERA</span>
                                    </td>

                                    <!-- ACCIONES -->
                                    <td style="text-align:right;">
                                        <button type="button"
                                                class="btn-update btn-ver-articulo"
                                                data-id="${articulo.idArticulo}"
                                                data-nombre="${fn:escapeXml(articulo.nombre)}"
                                                data-descripcion="${fn:escapeXml(articulo.descripcion)}"
                                                data-categoria="${fn:escapeXml(articulo.nombreCategoria)}"
                                                data-vendedor="${fn:escapeXml(articulo.nombreUsuario)}"
                                                data-matricula="${fn:escapeXml(articulo.idUsuarioFk)}"
                                                data-precio="${articulo.precio}"
                                                data-imagenes="<c:forEach var='img' items='${articulo.imagenes}' varStatus='li'>${li.first ? '' : '|'}${fn:escapeXml(img)}</c:forEach>">
                                            <i class="fa-solid fa-eye"></i> Ver detalles
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

<!-- ====================================================================
     MODAL USUARIO
     ==================================================================== -->
<div id="modalPeticion" class="modal-overlay">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Detalles de Verificación de Identidad</h3>
            <button type="button" class="btn-close-modal" id="btnCerrarModalPeticion">&times;</button>
        </div>

        <div class="modal-body-group">
            <p><strong>Matrícula:</strong> <span id="mod-matricula" style="color:#222;"></span></p>
            <p><strong>Nombre:</strong> <span id="mod-nombre" style="color:#222;"></span></p>
            <p><strong>Correo:</strong> <span id="mod-correo" style="color:#222;"></span></p>

            <div style="display:flex; gap:15px; margin-top:20px;">
                <div style="flex:1; text-align:center;">
                    <h6>Credencial Frente</h6>
                    <img id="img-frente" src="" alt="Frente"
                         style="width:100%; height:180px; object-fit:contain; border:1px solid #ccc; border-radius:8px; background:#fafafa;">
                </div>
                <div style="flex:1; text-align:center;">
                    <h6>Credencial Reverso</h6>
                    <img id="img-reverso" src="" alt="Reverso"
                         style="width:100%; height:180px; object-fit:contain; border:1px solid #ccc; border-radius:8px; background:#fafafa;">
                </div>
            </div>
        </div>

        <div class="modal-actions">
            <button type="button" class="btn-delete" id="btn-rechazar">Rechazar petición</button>
            <button type="button" class="btn-guardar-modal" id="btn-aceptar">Aceptar petición</button>
        </div>
    </div>
</div>

<!-- ====================================================================
     MODAL ARTÍCULO
     ==================================================================== -->
<div id="modalArticulo" class="modal-overlay">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Detalles del Artículo</h3>
            <button type="button" class="btn-close-modal" id="btnCerrarModalArticulo">&times;</button>
        </div>

        <div class="modal-body-group">
            <p><strong>Nombre:</strong> <span id="art-nombre" style="color:#222;"></span></p>
            <p><strong>Categoría:</strong> <span id="art-categoria" style="color:#222;"></span></p>
            <p>
                <strong>Vendedor:</strong> <span id="art-vendedor" style="color:#222;"></span>
                (Matrícula: <span id="art-matricula" style="color:#222;"></span>)
            </p>
            <p><strong>Precio:</strong> $<span id="art-precio" style="color:#222;"></span> MXN</p>
            <p><strong>Descripción:</strong></p>
            <p id="art-descripcion" style="color:#555; white-space:pre-wrap;"></p>

            <p><strong>Imágenes:</strong></p>
            <div id="art-imagenes" class="articulo-galeria"></div>
        </div>

        <div class="modal-actions">
            <button type="button" class="btn-delete" id="btn-rechazar-articulo-modal">Rechazar artículo</button>
            <button type="button" class="btn-guardar-modal" id="btn-aprobar-articulo-modal">Aprobar artículo</button>
        </div>
    </div>
</div>

<!-- ====================================================================
     JAVASCRIPT
     ==================================================================== -->
<script>
    window.MUA_CTX = {
        contextPath: '${pageContext.request.contextPath}'
    };
</script>
<script src="${pageContext.request.contextPath}/assets/js/sidebar.js"></script>
<script src="${pageContext.request.contextPath}/static/js/admin-actividad.js"></script>

</body>
</html>
