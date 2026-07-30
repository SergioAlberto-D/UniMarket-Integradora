<%--
  Created by IntelliJ IDEA.
  User: sheuko
  Date: 7/29/26
  Time: 9:30 PM
  To change this template use File | Settings | File Templates.
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mi Perfil - MUA</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" href="${pageContext.request.contextPath}/static/img/logoMUA.png" type="image/png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bi_s/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/mi-perfil.css">
</head>
<body>

<jsp:include page="components/header.jsp" />

<main class="profile-container">
    <header class="profile-header">
        <h1>Perfil de usuario</h1>
        <p>Consulta y administra tu información personal</p>
    </header>

    <%-- TARJETA DE RESUMEN DEL USUARIO --%>
    <section class="user-summary-card">
        <div class="user-info-section">
            <%-- AVATAR CON BOTÓN DE LÁPIZ ESTILO WHATSAPP --%>
            <div class="avatar-container-profile">
                <div class="user-avatar" id="avatarPreviewBox">
                    <c:choose>
                        <c:when test="${not empty sessionScope.usuario.fotoPerfil}">
                            <img src="${pageContext.request.contextPath}/${sessionScope.usuario.fotoPerfil}" alt="Avatar">
                        </c:when>
                        <c:otherwise>
                            ${fn:substring(sessionScope.usuario.nombre, 0, 2)}
                        </c:otherwise>
                    </c:choose>
                </div>
                <%-- Botón flotante del lápiz en la esquina inferior izquierda --%>
                <button type="button" class="btn-edit-avatar" onclick="document.getElementById('inputFotoPerfil').click()" title="Cambiar foto de perfil">
                    <i class="bi bi-pencil-fill"></i>
                </button>
                <%-- Input oculto que abre el explorador de archivos --%>
                <input type="file" id="inputFotoPerfil" accept="image/png, image/jpeg, image/jpg" style="display: none;" onchange="abrirEditorFoto(this)">
            </div>

            <div class="user-details">
                <h2>${fn:escapeXml(sessionScope.usuario.nombre)} ${fn:escapeXml(sessionScope.usuario.apellidoPaterno)}</h2>
                <ul>
                    <li><i class="bi bi-envelope"></i> ${sessionScope.usuario.correoInstitucional}</li>
                    <li><i class="bi bi-mortarboard"></i> ${nombreDivision}</li>
                    <li><i class="bi bi-telephone"></i> ${not empty sessionScope.usuario.numeroCelular ? sessionScope.usuario.numeroCelular : 'No registrado'}</li>
                </ul>
            </div>
        </div>

        <%-- GRILLA DE ESTADÍSTICAS SEGÚN ROL --%>
        <div class="stats-grid">

            <%-- CUADRITOS PARA COMPRADOR (Rol 2) --%>
            <c:if test="${sessionScope.usuario.idRolFk == 2}">
                <div class="stat-box">
                    <i class="bi bi-arrow-left-right"></i>
                    <span class="stat-num">${transaccionesCompletadas}</span>
                    <span class="stat-label">Transacciones<br>completadas</span>
                </div>
                <div class="stat-box">
                    <i class="bi bi-chat-dots"></i>
                    <span class="stat-num">${comentariosRealizados}</span>
                    <span class="stat-label">Comentarios<br>realizados</span>
                </div>
            </c:if>

            <%-- CUADRITOS PARA VENDEDOR (Rol 3): Incluye sus propios comentarios realizados --%>
            <c:if test="${sessionScope.usuario.idRolFk == 3}">
                <div class="stat-box">
                    <i class="bi bi-box-seam"></i>
                    <span class="stat-num">${articulosPublicados}</span>
                    <span class="stat-label">Artículos<br>publicados</span>
                </div>
                <div class="stat-box">
                    <i class="bi bi-arrow-left-right"></i>
                    <span class="stat-num">${transaccionesCompletadas}</span>
                    <span class="stat-label">Transacciones<br>completadas</span>
                </div>
                <div class="stat-box">
                    <i class="bi bi-chat-square-text"></i>
                    <span class="stat-num">${totalComentariosRecibidos}</span>
                    <span class="stat-label">Comentarios<br>recibidos</span>
                </div>
                <div class="stat-box">
                    <i class="bi bi-chat-dots"></i>
                    <span class="stat-num">${comentariosRealizados}</span>
                    <span class="stat-label">Comentarios<br>realizados</span>
                </div>
                <div class="stat-box">
                    <i class="bi bi-star-fill"></i>
                    <span class="stat-num">${promedioCalificacion}</span>
                    <span class="stat-label">Calificación<br>promedio</span>
                </div>
            </c:if>

        </div>
    </section>

    <%-- SECCIÓN DOBLE: OFERTAS Y CONFIGURACIÓN --%>
    <div class="dashboard-columns">

        <%-- COLUMNA IZQUIERDA: OFERTAS --%>
        <section class="panel-card">
            <div class="panel-header">
                <div class="header-title">
                    <i class="bi bi-bell"></i>
                    <div>
                        <h2>${sessionScope.usuario.idRolFk == 3 ? 'Ofertas Pendientes/Realizadas' : 'Mis Ofertas Realizadas'}</h2>
                        <p>${sessionScope.usuario.idRolFk == 3 ? 'Revisa y responde las propuestas de compradores' : 'Revisa el estado de tus propuestas de compra'}</p>
                    </div>
                </div>

                <%-- BOTONES DE PESTAÑA: SOLO SE MUESTRAN SI ERES VENDEDOR (Rol 3) --%>
                <c:if test="${sessionScope.usuario.idRolFk == 3}">
                    <div class="tab-buttons">
                        <button id="btnRecibidas" class="tab-btn active" onclick="cambiarPestaña('recibidas')">Recibidas</button>
                        <button id="btnHechas" class="tab-btn" onclick="cambiarPestaña('hechas')">Hechas por mí</button>
                    </div>
                </c:if>
            </div>

            <%-- VISTA 1: OFERTAS RECIBIDAS (EXCLUSIVO VENDEDOR - ROL 3) --%>
            <c:if test="${sessionScope.usuario.idRolFk == 3}">
                <div id="vistaRecibidas" class="ofertas-list">
                    <c:choose>
                        <c:when test="${not empty ofertasRecibidas}">
                            <c:forEach var="of" items="${ofertasRecibidas}">
                                <div class="oferta-item">
                                    <div class="oferta-img">
                                        <img src="${pageContext.request.contextPath}/${of.imagenArticulo}" alt="Producto">
                                    </div>
                                    <div class="oferta-info">
                                        <strong>${fn:escapeXml(of.nombreArticulo)}</strong>
                                        <span>Oferta de: ${fn:escapeXml(of.nombreUsuario)}</span>
                                        <span class="monto">$${of.monto}</span>
                                    </div>
                                    <div class="oferta-actions">
                                        <div class="btn-group-vertical">
                                            <button class="btn-primary-action">Aceptar</button>
                                            <button class="btn-secondary-action">Rechazar</button>
                                        </div>
                                        <div class="dropdown-hamburguesa">
                                            <button class="btn-icon-hamburguesa" onclick="toggleMenu(this)">
                                                <i class="bi bi-list"></i>
                                            </button>
                                            <div class="menu-flotante">
                                                <a href="#"><i class="bi bi-pencil"></i> Editar</a>
                                                <a href="#"><i class="bi bi-check-circle"></i> Vendido</a>
                                                <a href="#" class="text-danger"><i class="bi bi-trash"></i> Eliminar</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <p class="empty-msg">No has recibido ofertas pendientes.</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>

            <%-- VISTA 2: OFERTAS HECHAS POR MÍ (VISIBLE PARA AMBOS ROLES) --%>
            <div id="vistaHechas" class="ofertas-list" style="${sessionScope.usuario.idRolFk == 3 ? 'display: none;' : 'display: flex;'}">
                <c:choose>
                    <c:when test="${not empty ofertasHechas}">
                        <c:forEach var="of" items="${ofertasHechas}">
                            <div class="oferta-item">
                                <div class="oferta-img">
                                    <img src="${pageContext.request.contextPath}/${of.imagenArticulo}" alt="Producto">
                                </div>
                                <div class="oferta-info">
                                    <strong>${fn:escapeXml(of.nombreArticulo)}</strong>
                                    <span>Vendedor: ${fn:escapeXml(of.nombreUsuario)}</span>
                                    <span class="monto">$${of.monto}</span>
                                </div>
                                <div class="oferta-actions">
                                    <div class="btn-group-vertical">
                                        <button class="btn-primary-action">Editar</button>
                                        <button class="btn-secondary-action">Cancelar</button>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <p class="empty-msg">No has realizado ninguna oferta aún.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>

        <%-- COLUMNA DERECHA: CONFIGURACIÓN DE CUENTA (CON ACCIONES A LOS MODALES) --%>
        <section class="panel-card">
            <div class="panel-header">
                <div class="header-title">
                    <i class="bi bi-gear"></i>
                    <div>
                        <h2>Configuración de cuenta</h2>
                        <p>Acciones rápidas para administrar tu cuenta</p>
                    </div>
                </div>
            </div>

            <div class="config-list">
                <a href="historial-actividad.jsp" class="config-item">
                    <i class="bi bi-clock-history"></i>
                    <div>
                        <strong>Historial de Actividad</strong>
                        <span>Revisa tus Ultimas Compras</span>
                    </div>
                    <i class="bi bi-arrow-right arrow-icon"></i>
                </a>

                <a href="javascript:void(0)" class="config-item" onclick="abrirModal('modalTelefono')">
                    <i class="bi bi-telephone"></i>
                    <div>
                        <strong>Cambiar teléfono</strong>
                        <span>Actualiza tu número de contacto</span>
                    </div>
                    <i class="bi bi-arrow-right arrow-icon"></i>
                </a>

                <a href="cambiar-password-perfil.jsp" class="config-item">
                    <i class="bi bi-lock"></i>
                    <div>
                        <strong>Cambiar contraseña</strong>
                        <span>Mantén tu cuenta segura</span>
                    </div>
                    <i class="bi bi-arrow-right arrow-icon"></i>
                </a>

                <a href="javascript:void(0)" class="config-item" onclick="abrirModal('modalCerrarSesion')">
                    <i class="bi bi-box-arrow-right"></i>
                    <div>
                        <strong>Cerrar sesión</strong>
                        <span>Finaliza tu sesión actual</span>
                    </div>
                    <i class="bi bi-arrow-right arrow-icon"></i>
                </a>

                <a href="javascript:void(0)" class="config-item danger-item" onclick="abrirModal('modalEliminarCuenta')">
                    <i class="bi bi-trash3"></i>
                    <div>
                        <strong>Eliminar cuenta</strong>
                        <span>Esta acción no se puede deshacer</span>
                    </div>
                    <i class="bi bi-arrow-right arrow-icon"></i>
                </a>
            </div>
        </section>
    </div>
</main>

<!-- ==========================================
     MODALES FLOTANTES INTEGRADOS
     ========================================== -->

<!-- Modal: Cambiar Número Celular -->
<div class="modal-overlay" id="modalTelefono">
    <div class="floating-modal phone-modal">
        <h2>Cambiar número celular</h2>
        <p>Acción necesaria para poder ocupar el sistema de compra y venta</p>
        <form action="${pageContext.request.contextPath}/ActualizarTelefonoServlet" method="post">
            <label for="telefonoNuevo">Teléfono</label>
            <input id="telefonoNuevo" name="telefono" type="tel" placeholder="Ejem: (777)-222-2222" required>
            <div class="phone-modal-actions" style="margin-top: 20px; display: flex; gap: 10px; justify-content: flex-end;">
                <button type="button" class="btn btn-secondary" onclick="cerrarModal('modalTelefono')">Cancelar</button>
                <button type="submit" class="btn btn-primary" style="background: #7c3f1d; border: none;">Cambiar número</button>
            </div>
        </form>
    </div>
</div>

<!-- Modal: Eliminar Cuenta -->
<div class="modal-overlay" id="modalEliminarCuenta">
    <div class="floating-modal confirm-modal">
        <h2>Eliminar cuenta</h2>
        <p>Esta acción no se puede deshacer una vez se acepte. ¿Estás seguro de eliminar tu cuenta?</p>
        <div class="modal-actions" style="margin-top: 20px; display: flex; gap: 10px; justify-content: flex-end;">
            <button type="button" class="btn btn-secondary" onclick="cerrarModal('modalEliminarCuenta')">Cancelar</button>
            <button type="button" class="btn btn-danger" onclick="cerrarModal('modalEliminarCuenta')">Eliminar</button>
        </div>
    </div>
</div>

<!-- Modal: Cerrar Sesión -->
<div class="modal-overlay" id="modalCerrarSesion">
    <div class="floating-modal confirm-modal">
        <h2>Cerrar sesión</h2>
        <p>¿Estás seguro de cerrar tu sesión?</p>
        <div class="modal-actions" style="margin-top: 20px; display: flex; gap: 10px; justify-content: flex-end;">
            <button type="button" class="btn btn-secondary" onclick="cerrarModal('modalCerrarSesion')">Cancelar</button>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger">Cerrar sesión</a>
        </div>
    </div>
</div>

<!-- Modal: Editor de Foto Estilo WhatsApp -->
<div class="modal-overlay" id="modalEditorFoto">
    <div class="floating-modal photo-editor-modal">
        <div class="editor-header">
            <button type="button" class="btn-close-editor" onclick="cerrarModal('modalEditorFoto')"><i class="bi bi-x-lg"></i></button>
            <span>Arrastra la imagen para ajustarla.</span>
            <button type="button" class="btn-change-photo" onclick="document.getElementById('inputFotoPerfil').click()"><i class="bi bi-arrow-counterclockwise"></i> Subir otra</button>
        </div>

        <div class="editor-canvas-area" id="editorArea">
            <canvas id="canvasEditor"></canvas>
            <%-- Máscara redonda para simular cómo quedará el corte --%>
            <div class="circular-overlay"></div>
            <%-- Controles flotantes de Zoom --%>
            <div class="zoom-controls">
                <button type="button" onclick="cambiarZoom(0.1)"><i class="bi bi-plus-lg"></i></button>
                <button type="button" onclick="cambiarZoom(-0.1)"><i class="bi bi-dash-lg"></i></button>
            </div>
        </div>

        <div class="editor-footer">
            <button type="button" class="btn-confirm-photo" onclick="guardarFotoPerfil()">
                <i class="bi bi-check-lg"></i>
            </button>
        </div>
    </div>
</div>
<!-- ==========================================
     SCRIPTS DE FUNCIONAMIENTO Y FORMATEO
     ========================================== -->
<script>
    // --- CONTROL DE MODALES ---
    function abrirModal(id) {
        const el = document.getElementById(id);
        if (el) el.classList.add("show-modal");
    }

    function cerrarModal(id) {
        const el = document.getElementById(id);
        if (el) el.classList.remove("show-modal");
    }

    // Cerrar el modal al dar clic en el fondo oscuro
    window.addEventListener('click', function(e) {
        if (e.target.classList.contains('modal-overlay')) {
            e.target.classList.remove('show-modal');
        }
    });

    // --- CONMUTADOR DE PESTAÑAS ---
    function cambiarPestaña(tipo) {
        const vistaRecibidas = document.getElementById('vistaRecibidas');
        const vistaHechas = document.getElementById('vistaHechas');
        const btnRecibidas = document.getElementById('btnRecibidas');
        const btnHechas = document.getElementById('btnHechas');

        if (tipo === 'recibidas') {
            if(vistaRecibidas) vistaRecibidas.style.display = 'flex';
            if(vistaHechas) vistaHechas.style.display = 'none';
            if(btnRecibidas) btnRecibidas.classList.add('active');
            if(btnHechas) btnHechas.classList.remove('active');
        } else {
            if(vistaRecibidas) vistaRecibidas.style.display = 'none';
            if(vistaHechas) vistaHechas.style.display = 'flex';
            if(btnRecibidas) btnRecibidas.classList.remove('active');
            if(btnHechas) btnHechas.classList.add('active');
        }
    }

    // --- MENÚ HAMBURGUESA ---
    function toggleMenu(boton) {
        document.querySelectorAll('.menu-flotante.show').forEach(menu => {
            if (menu !== boton.nextElementSibling) {
                menu.classList.remove('show');
            }
        });
        const menu = boton.nextElementSibling;
        menu.classList.toggle('show');
    }

    window.addEventListener('click', function(e) {
        if (!e.target.closest('.dropdown-hamburguesa')) {
            document.querySelectorAll('.menu-flotante.show').forEach(m => m.classList.remove('show'));
        }
    });

    // --- MÁSCARA PARA EL NÚMERO DE TELÉFONO ---
    const formatearTelefono = (event) => {
        let input = event.target;
        let valor = input.value.replace(/\D/g, ''); // Elimina letras y símbolos

        if (valor.length > 10) {
            valor = valor.substring(0, 10);
        }

        let valorFormateado = '';
        if (valor.length > 0) {
            valorFormateado = '(' + valor.substring(0, 3);
        }
        if (valor.length >= 4) {
            valorFormateado += ')-' + valor.substring(3, 6);
        }
        if (valor.length >= 7) {
            valorFormateado += '-' + valor.substring(6, 10);
        }

        input.value = valorFormateado;
    };

    const inputTelefono = document.getElementById('telefonoNuevo');
    if (inputTelefono) {
        inputTelefono.addEventListener('input', formatearTelefono);
    }
</script>
<script>
    // --- LÓGICA DEL EDITOR DE FOTOS ESTILO WHATSAPP ---
    let imagenActual = new Image();
    let zoomLevel = 1.0;
    let offsetX = 0, offsetY = 0;
    let isDragging = false, startX = 0, startY = 0;

    function abrirEditorFoto(input) {
        if (input.files && input.files[0]) {
            const reader = new FileReader();
            reader.onload = function(e) {
                imagenActual.onload = function() {
                    zoomLevel = 1.0;
                    offsetX = 0;
                    offsetY = 0;
                    dibujarCanvas();
                    abrirModal('modalEditorFoto');
                }
                imagenActual.src = e.target.result;
            }
            reader.readAsDataURL(input.files[0]);
        }
    }

    function dibujarCanvas() {
        const canvas = document.getElementById('canvasEditor');
        const ctx = canvas.getContext('2d');
        canvas.width = 300;
        canvas.height = 300;

        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.save();

        // Centrar y dibujar imagen con zoom y arrastre
        const cx = canvas.width / 2;
        const cy = canvas.height / 2;
        ctx.translate(cx + offsetX, cy + offsetY);
        ctx.scale(zoomLevel, zoomLevel);

        // Mantener proporción cubriendo el área
        let ratio = Math.max(canvas.width / imagenActual.width, canvas.height / imagenActual.height);
        let dw = imagenActual.width * ratio;
        let dh = imagenActual.height * ratio;

        ctx.drawImage(imagenActual, -dw / 2, -dh / 2, dw, dh);
        ctx.restore();
    }

    function cambiarZoom(delta) {
        zoomLevel = Math.max(0.5, Math.min(3.0, zoomLevel + delta));
        dibujarCanvas();
    }

    // Eventos de arrastre en el canvas
    const editorArea = document.getElementById('editorArea');
    if (editorArea) {
        editorArea.addEventListener('mousedown', e => {
            isDragging = true;
            startX = e.clientX - offsetX;
            startY = e.clientY - offsetY;
        });
        window.addEventListener('mousemove', e => {
            if (!isDragging) return;
            offsetX = e.clientX - startX;
            offsetY = e.clientY - startY;
            dibujarCanvas();
        });
        window.addEventListener('mouseup', () => isDragging = false);
    }

    function guardarFotoPerfil() {
        const canvas = document.getElementById('canvasEditor');
        // Exportar como imagen Base64 para enviar por AJAX
        const base64Image = canvas.toDataURL('image/jpeg', 0.9);

        fetch('${pageContext.request.contextPath}/actualizar-foto-perfil', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: 'fotoBase64=' + encodeURIComponent(base64Image)
        })
            .then(response => response.json())
            .then(data => {
                if (data.exito) {
                    location.reload(); // Recarga para mostrar la foto nueva en el header y perfil
                } else {
                    alert('No se pudo actualizar la foto de perfil.');
                }
            })
            .catch(error => console.error('Error al subir la foto:', error));
    }
</script>

</body>
</html>