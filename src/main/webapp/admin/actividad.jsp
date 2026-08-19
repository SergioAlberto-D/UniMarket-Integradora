<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Panel Admin - Actividad</title>

    <base href="${pageContext.request.contextPath}/">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/admin-layout.css">

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">

    <style>

        /*
         * =========================================================
         * BADGES
         * =========================================================
         */

        .badge-espera {
            display: inline-block;
            padding: 5px 12px;
            border-radius: 20px;
            background-color: #FFF3E0;
            color: #E65100;
            font-size: 12px;
            font-weight: 700;
        }

        .badge-usuario {
            display: inline-block;
            padding: 5px 12px;
            border-radius: 20px;
            background-color: #E3F2FD;
            color: #1565C0;
            font-size: 12px;
            font-weight: 700;
        }

        .badge-articulo {
            display: inline-block;
            padding: 5px 12px;
            border-radius: 20px;
            background-color: #F3E5F5;
            color: #7B1FA2;
            font-size: 12px;
            font-weight: 700;
        }

        /*
         * =========================================================
         * ARTÍCULO EN ESPERA
         * =========================================================
         */

        .articulo-info {
            display: flex;
            align-items: center;
            gap: 15px;
        }

        .articulo-imagen {
            width: 65px;
            height: 65px;
            border-radius: 10px;
            object-fit: cover;
            border: 1px solid #ddd;
            background: #f7f7f7;
        }

        .articulo-imagen-vacia {
            width: 65px;
            height: 65px;
            border-radius: 10px;
            background: #f3f3f3;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #999;
            font-size: 24px;
        }

        .articulo-titulo {
            font-weight: 700;
            color: #222;
            margin-bottom: 4px;
        }

        .articulo-vendedor {
            color: #777;
            font-size: 13px;
        }

        .articulo-precio {
            font-weight: 700;
            color: #7c3f1d;
        }

        /*
         * =========================================================
         * BOTONES
         * =========================================================
         */

        .acciones-articulo {
            display: flex;
            justify-content: flex-end;
            gap: 8px;
            flex-wrap: wrap;
        }

        .btn-aprobar-articulo {
            border: none;
            background: #2E7D32;
            color: white;
            padding: 9px 14px;
            border-radius: 7px;
            cursor: pointer;
            font-weight: 600;
        }

        .btn-aprobar-articulo:hover {
            background: #1B5E20;
        }

        .btn-rechazar-articulo {
            border: none;
            background: #C62828;
            color: white;
            padding: 9px 14px;
            border-radius: 7px;
            cursor: pointer;
            font-weight: 600;
        }

        .btn-rechazar-articulo:hover {
            background: #B71C1C;
        }

        /*
         * =========================================================
         * SEPARADORES
         * =========================================================
         */

        .seccion-titulo {
            display: flex;
            align-items: center;
            gap: 10px;
            margin-bottom: 18px;
        }

        .seccion-titulo i {
            color: #7c3f1d;
        }

        .contador-espera {
            background: #FFF3E0;
            color: #E65100;
            border-radius: 20px;
            padding: 4px 10px;
            font-size: 12px;
            font-weight: 700;
        }

        /*
         * =========================================================
         * MODAL
         * =========================================================
         */

        .modal-overlay {
            display: none;
            position: fixed;
            inset: 0;
            background: rgba(0, 0, 0, 0.55);
            z-index: 9999;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }

        .modal-content {
            background: white;
            width: 100%;
            max-width: 650px;
            border-radius: 14px;
            padding: 25px;
            box-shadow: 0 20px 60px rgba(0,0,0,.25);
        }

        .modal-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }

        .modal-header h3 {
            margin: 0;
            font-size: 20px;
        }

        .btn-close-modal {
            border: none;
            background: transparent;
            font-size: 28px;
            cursor: pointer;
            color: #777;
        }

        .modal-body-group {
            color: #555;
        }

        .modal-actions {
            display: flex;
            justify-content: space-between;
            gap: 12px;
            margin-top: 25px;
        }

        .btn-delete {
            border: none;
            background: #C62828;
            color: white;
            padding: 10px 18px;
            border-radius: 8px;
            cursor: pointer;
            font-weight: 600;
        }

        .btn-guardar-modal {
            border: none;
            background: #2E7D32;
            color: white;
            padding: 10px 18px;
            border-radius: 8px;
            cursor: pointer;
            font-weight: 600;
        }

    </style>

</head>

<body>

<!-- =============================================================
     SIDEBAR
     ============================================================= -->

<div class="sidebar-overlay"
     id="sidebarOverlay">
</div>

<jsp:include page="/includes/sidebar.jsp">

    <jsp:param name="active"
               value="actividad" />

</jsp:include>


<!-- =============================================================
     CONTENIDO
     ============================================================= -->

<main class="main-content">

    <!-- =========================================================
         TOPBAR
         ========================================================= -->

    <div class="topbar">

        <div class="topbar-left">

            <button type="button"
                    class="btn-hamburger"
                    id="btnHamburger">

                <i class="fa-solid fa-bars"></i>

            </button>

            <div>

                Administración &gt;

                <span style="color:#555;">
                    Actividad reciente
                </span>

            </div>

        </div>

    </div>


    <!-- =========================================================
         USUARIOS EN ESPERA
         ========================================================= -->

    <div class="container mt-4">

        <div class="table-card">

            <div class="seccion-titulo">

                <i class="fa-solid fa-user-clock"></i>

                <h2 class="table-title"
                    style="margin:0;">

                    Usuarios pendientes de verificación

                </h2>

                <span class="contador-espera">

                    ${empty listaPeticiones ? 0 : listaPeticiones.size()}

                </span>

            </div>


            <div class="table-responsive">

                <table class="custom-table"
                       id="tablaPeticiones">

                    <thead>

                    <tr>

                        <th style="width:15%;">
                            Matrícula
                        </th>

                        <th>
                            Nombre completo
                        </th>

                        <th>
                            Correo institucional
                        </th>

                        <th>
                            Estado
                        </th>

                        <th style="text-align:right;">
                            Acciones
                        </th>

                    </tr>

                    </thead>


                    <tbody>

                    <c:choose>

                        <c:when test="${empty listaPeticiones}">

                            <tr>

                                <td colspan="5"
                                    style="text-align:center;
                                           padding:35px;
                                           color:#888;">

                                    <i class="fa-solid fa-circle-check"
                                       style="font-size:25px;
                                              margin-bottom:10px;">
                                    </i>

                                    <br>

                                    No hay usuarios pendientes de verificación.

                                </td>

                            </tr>

                        </c:when>


                        <c:otherwise>

                            <c:forEach
                                    var="user"
                                    items="${listaPeticiones}">

                                <tr id="row-user-${user.matricula}">

                                    <td>

                                        <strong>

                                            <c:out
                                                    value="${user.matricula}"/>

                                        </strong>

                                    </td>


                                    <td>

                                        <c:out
                                                value="${user.nombre} ${user.apellidoPaterno}"/>

                                    </td>


                                    <td>

                                        <c:out
                                                value="${user.correoInstitucional}"/>

                                    </td>


                                    <td>

                                        <span class="badge-usuario">

                                            EN ESPERA

                                        </span>

                                    </td>


                                    <td style="text-align:right;">

                                        <button
                                                type="button"
                                                class="btn-update"
                                                onclick="abrirModalPeticion(
                                                        '${user.matricula}',
                                                        '${user.nombre} ${user.apellidoPaterno}',
                                                        '${user.correoInstitucional}',
                                                        '${user.fotoCredencialFrente}',
                                                        '${user.fotoCredencialReverso}'
                                                        )">

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


    <!-- =========================================================
         ARTÍCULOS EN ESPERA
         ========================================================= -->

    <div class="container mt-4"
         style="margin-bottom:40px;">

        <div class="table-card">

            <div class="seccion-titulo">

                <i class="fa-solid fa-box-open"></i>

                <h2 class="table-title"
                    style="margin:0;">

                    Artículos pendientes de verificación

                </h2>

                <span class="contador-espera">

                    ${empty listaArticulosEspera ? 0 : listaArticulosEspera.size()}

                </span>

            </div>


            <div class="table-responsive">

                <table class="custom-table"
                       id="tablaArticulosEspera">

                    <thead>

                    <tr>

                        <th style="width:42%;">
                            Artículo
                        </th>

                        <th>
                            Vendedor
                        </th>

                        <th>
                            Precio
                        </th>

                        <th>
                            Estado
                        </th>

                        <th style="text-align:right;">
                            Acciones
                        </th>

                    </tr>

                    </thead>


                    <tbody>

                    <c:choose>

                        <c:when test="${empty listaArticulosEspera}">

                            <tr>

                                <td colspan="5"
                                    style="text-align:center;
                                           padding:35px;
                                           color:#888;">

                                    <i class="fa-solid fa-circle-check"
                                       style="font-size:25px;
                                              margin-bottom:10px;">
                                    </i>

                                    <br>

                                    No hay artículos pendientes de verificación.

                                </td>

                            </tr>

                        </c:when>


                        <c:otherwise>

                            <c:forEach
                                    var="articulo"
                                    items="${listaArticulosEspera}">

                                <tr id="row-articulo-${articulo.idArticulo}">

                                    <!-- ARTÍCULO -->

                                    <td>

                                        <div class="articulo-info">

                                            <c:choose>

                                                <c:when test="${not empty articulo.imagenPrincipal}">

                                                    <img
                                                            class="articulo-imagen"
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

                                                    <c:out
                                                            value="${articulo.nombre}"/>

                                                </div>

                                                <div class="articulo-vendedor">

                                                    ID artículo:

                                                    <strong>
                                                        #${articulo.idArticulo}
                                                    </strong>

                                                </div>

                                                <c:if test="${not empty articulo.nombreCategoria}">

                                                    <div class="articulo-vendedor">

                                                        Categoría:

                                                        <c:out
                                                                value="${articulo.nombreCategoria}"/>

                                                    </div>

                                                </c:if>

                                            </div>

                                        </div>

                                    </td>


                                    <!-- VENDEDOR -->

                                    <td>

                                        <c:out
                                                value="${articulo.nombreUsuario}"/>

                                        <br>

                                        <small style="color:#888;">

                                            Matrícula:

                                            <c:out
                                                    value="${articulo.idUsuarioFk}"/>

                                        </small>

                                    </td>


                                    <!-- PRECIO -->

                                    <td>

                                        <span class="articulo-precio">

                                            $

                                            <c:out
                                                    value="${articulo.precio}"/>

                                            MXN

                                        </span>

                                    </td>


                                    <!-- ESTADO -->

                                    <td>

                                        <span class="badge-espera">

                                            ESPERA

                                        </span>

                                    </td>


                                    <!-- ACCIONES -->

                                    <td>

                                        <div class="acciones-articulo">

                                            <button
                                                    type="button"
                                                    class="btn-aprobar-articulo"
                                                    onclick="procesarArticulo(
                                                        ${articulo.idArticulo},
                                                            'aceptar'
                                                            )">

                                                <i class="fa-solid fa-check"></i>

                                                Aprobar

                                            </button>


                                            <button
                                                    type="button"
                                                    class="btn-rechazar-articulo"
                                                    onclick="procesarArticulo(
                                                        ${articulo.idArticulo},
                                                            'rechazar'
                                                            )">

                                                <i class="fa-solid fa-xmark"></i>

                                                Rechazar

                                            </button>

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


<!-- =============================================================
     MODAL USUARIO
     ============================================================= -->

<div id="modalPeticion"
     class="modal-overlay">

    <div class="modal-content">

        <div class="modal-header">

            <h3>
                Detalles de Verificación de Identidad
            </h3>

            <button
                    type="button"
                    class="btn-close-modal"
                    onclick="cerrarModal()">

                &times;

            </button>

        </div>


        <div class="modal-body-group">

            <p>

                <strong>Matrícula:</strong>

                <span id="mod-matricula"
                      style="color:#222;">
                </span>

            </p>


            <p>

                <strong>Nombre:</strong>

                <span id="mod-nombre"
                      style="color:#222;">
                </span>

            </p>


            <p>

                <strong>Correo:</strong>

                <span id="mod-correo"
                      style="color:#222;">
                </span>

            </p>


            <div style="
                    display:flex;
                    gap:15px;
                    margin-top:20px;
                 ">

                <div style="
                        flex:1;
                        text-align:center;
                    ">

                    <h6>
                        Credencial Frente
                    </h6>

                    <img
                            id="img-frente"
                            src=""
                            alt="Frente"
                            style="
                                width:100%;
                                height:180px;
                                object-fit:contain;
                                border:1px solid #ccc;
                                border-radius:8px;
                                background:#fafafa;
                            ">

                </div>


                <div style="
                        flex:1;
                        text-align:center;
                    ">

                    <h6>
                        Credencial Reverso
                    </h6>

                    <img
                            id="img-reverso"
                            src=""
                            alt="Reverso"
                            style="
                                width:100%;
                                height:180px;
                                object-fit:contain;
                                border:1px solid #ccc;
                                border-radius:8px;
                                background:#fafafa;
                            ">

                </div>

            </div>

        </div>


        <div class="modal-actions">

            <button
                    type="button"
                    class="btn-delete"
                    id="btn-rechazar">

                Rechazar petición

            </button>


            <button
                    type="button"
                    class="btn-guardar-modal"
                    id="btn-aceptar">

                Aceptar petición

            </button>

        </div>

    </div>

</div>


<!-- =============================================================
     JAVASCRIPT
     ============================================================= -->

<script src="${pageContext.request.contextPath}/assets/js/sidebar.js"></script>


<script>

    const contextPath =
        "${pageContext.request.contextPath}";


    /*
     * ============================================================
     * VARIABLES USUARIO
     * ============================================================
     */

    let matriculaActual = '';

    let correoActual = '';


    /*
     * ============================================================
     * ABRIR MODAL USUARIO
     * ============================================================
     */

    function abrirModalPeticion(
        matricula,
        nombre,
        correo,
        fotoFrente,
        fotoReverso
    ) {

        matriculaActual =
            matricula;

        correoActual =
            correo;


        document.getElementById(
            'mod-matricula'
        ).textContent =
            matricula;


        document.getElementById(
            'mod-nombre'
        ).textContent =
            nombre;


        document.getElementById(
            'mod-correo'
        ).textContent =
            correo;


        const imgFrente =
            document.getElementById(
                'img-frente'
            );

        const imgReverso =
            document.getElementById(
                'img-reverso'
            );


        if (fotoFrente) {

            imgFrente.src =
                contextPath +
                "/" +
                fotoFrente;

        } else {

            imgFrente.src = "";

        }


        if (fotoReverso) {

            imgReverso.src =
                contextPath +
                "/" +
                fotoReverso;

        } else {

            imgReverso.src = "";

        }


        document.getElementById(
            'modalPeticion'
        ).style.display =
            'flex';
    }


    /*
     * ============================================================
     * CERRAR MODAL
     * ============================================================
     */

    function cerrarModal() {

        document.getElementById(
            'modalPeticion'
        ).style.display =
            'none';
    }


    /*
     * ============================================================
     * CLICK FUERA DEL MODAL
     * ============================================================
     */

    window.onclick =
        function(event) {

            const modal =
                document.getElementById(
                    'modalPeticion'
                );

            if (event.target === modal) {

                cerrarModal();
            }
        };


    /*
     * ============================================================
     * PROCESAR USUARIO
     * ============================================================
     */

    function procesarPeticion(
        accion
    ) {

        let mensajeConfirmacion;

        if (accion === 'aceptar') {

            mensajeConfirmacion =
                '¿Estás seguro de que la identidad es válida? Se activará la cuenta del usuario.';

        } else {

            mensajeConfirmacion =
                '¿Estás seguro de rechazar y eliminar permanentemente esta petición?';
        }


        if (!confirm(mensajeConfirmacion)) {
            return;
        }


        fetch(
            contextPath + '/adminactividad',
            {
                method: 'POST',

                headers: {
                    'Content-Type':
                        'application/x-www-form-urlencoded'
                },

                body:
                    new URLSearchParams({

                        tipo: 'usuario',

                        accion: accion,

                        matricula:
                        matriculaActual,

                        correo:
                        correoActual

                    })
            }
        )

            .then(
                response =>
                    response.json()
            )

            .then(
                data => {

                    if (data.exito) {

                        alert(
                            data.mensaje
                        );

                        cerrarModal();


                        const fila =
                            document.getElementById(
                                'row-user-' +
                                matriculaActual
                            );


                        if (fila) {
                            fila.remove();
                        }

                    } else {

                        alert(
                            data.mensaje
                        );
                    }

                }
            )

            .catch(
                error => {

                    console.error(
                        'Error:',
                        error
                    );

                    alert(
                        'Ocurrió un error al procesar la petición.'
                    );
                }
            );
    }


    /*
     * ============================================================
     * PROCESAR ARTÍCULO
     * ============================================================
     */

    function procesarArticulo(
        idArticulo,
        accion
    ) {

        let mensaje;

        if (accion === 'aceptar') {

            mensaje =
                '¿Estás seguro de aprobar este artículo? Después de aprobarlo aparecerá en el catálogo.';

        } else {

            mensaje =
                '¿Estás seguro de rechazar este artículo? El artículo será eliminado del catálogo.';
        }


        if (!confirm(mensaje)) {
            return;
        }


        fetch(
            contextPath + '/adminactividad',
            {
                method: 'POST',

                headers: {
                    'Content-Type':
                        'application/x-www-form-urlencoded'
                },

                body:
                    new URLSearchParams({

                        tipo: 'articulo',

                        accion: accion,

                        idArticulo:
                        idArticulo

                    })
            }
        )

            .then(
                response =>
                    response.json()
            )

            .then(
                data => {

                    if (data.exito) {

                        alert(
                            data.mensaje
                        );


                        const fila =
                            document.getElementById(
                                'row-articulo-' +
                                idArticulo
                            );


                        if (fila) {
                            fila.remove();
                        }

                    } else {

                        alert(
                            data.mensaje
                        );
                    }

                }
            )

            .catch(
                error => {

                    console.error(
                        'Error:',
                        error
                    );

                    alert(
                        'Ocurrió un error al procesar el artículo.'
                    );
                }
            );
    }


    /*
     * ============================================================
     * BOTONES USUARIO
     * ============================================================
     */

    document
        .getElementById('btn-aceptar')
        .addEventListener(
            'click',
            function() {

                procesarPeticion(
                    'aceptar'
                );

            }
        );


    document
        .getElementById('btn-rechazar')
        .addEventListener(
            'click',
            function() {

                procesarPeticion(
                    'rechazar'
                );

            }
        );

</script>


</body>

</html>