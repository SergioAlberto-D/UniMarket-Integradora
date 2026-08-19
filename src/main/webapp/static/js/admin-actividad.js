(function () {
    'use strict';

    const CTX = (window.MUA_CTX && window.MUA_CTX.contextPath) || '';
    const URL_ADMIN_ACTIVIDAD = CTX + '/adminactividad';

    // ---------- Referencias DOM ----------

    const cuerpoPeticiones = document.getElementById('cuerpoPeticiones');
    const cuerpoArticulosEspera = document.getElementById('cuerpoArticulosEspera');

    const modalPeticion = document.getElementById('modalPeticion');
    const modalArticulo = document.getElementById('modalArticulo');

    const btnAceptarUsuario = document.getElementById('btn-aceptar');
    const btnRechazarUsuario = document.getElementById('btn-rechazar');

    const btnAprobarArticulo = document.getElementById('btn-aprobar-articulo-modal');
    const btnRechazarArticulo = document.getElementById('btn-rechazar-articulo-modal');

    // ---------- Estado ----------

    let matriculaActual = '';
    let correoActual = '';
    let idArticuloActual = null;

    // =====================================================================
    // MODAL USUARIO
    // =====================================================================

    function abrirModalPeticion(datos) {
        matriculaActual = datos.matricula || '';
        correoActual = datos.correo || '';

        document.getElementById('mod-matricula').textContent = matriculaActual;
        document.getElementById('mod-nombre').textContent = datos.nombre || '';
        document.getElementById('mod-correo').textContent = correoActual;

        const imgFrente = document.getElementById('img-frente');
        const imgReverso = document.getElementById('img-reverso');

        imgFrente.src = datos.fotoFrente ? (CTX + '/' + datos.fotoFrente) : '';
        imgReverso.src = datos.fotoReverso ? (CTX + '/' + datos.fotoReverso) : '';

        modalPeticion.style.display = 'flex';
    }

    function cerrarModalPeticion() {
        modalPeticion.style.display = 'none';
    }

    function procesarPeticion(accion) {
        const mensajeConfirmacion = accion === 'aceptar'
            ? '¿Estás seguro de que la identidad es válida? Se activará la cuenta del usuario.'
            : '¿Estás seguro de rechazar y eliminar permanentemente esta petición?';

        if (!confirm(mensajeConfirmacion)) return;

        fetch(URL_ADMIN_ACTIVIDAD, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: new URLSearchParams({
                tipo: 'usuario',
                accion: accion,
                matricula: matriculaActual,
                correo: correoActual
            })
        })
            .then(function (response) { return response.json(); })
            .then(function (data) {
                alert(data.mensaje);

                if (data.exito) {
                    cerrarModalPeticion();

                    const fila = document.getElementById('row-user-' + matriculaActual);
                    if (fila) fila.remove();
                }
            })
            .catch(function (error) {
                console.error('Error:', error);
                alert('Ocurrió un error al procesar la petición.');
            });
    }

    // =====================================================================
    // MODAL ARTÍCULO
    // =====================================================================

    function abrirModalArticulo(boton) {
        idArticuloActual = boton.getAttribute('data-id');

        document.getElementById('art-nombre').textContent =
            boton.getAttribute('data-nombre') || '';
        document.getElementById('art-categoria').textContent =
            boton.getAttribute('data-categoria') || 'Sin categoría';
        document.getElementById('art-vendedor').textContent =
            boton.getAttribute('data-vendedor') || '';
        document.getElementById('art-matricula').textContent =
            boton.getAttribute('data-matricula') || '';
        document.getElementById('art-precio').textContent =
            boton.getAttribute('data-precio') || '0';
        document.getElementById('art-descripcion').textContent =
            boton.getAttribute('data-descripcion') || 'Sin descripción.';

        renderizarImagenesArticulo(boton.getAttribute('data-imagenes') || '');

        modalArticulo.style.display = 'flex';
    }

    function renderizarImagenesArticulo(imagenesRaw) {
        const contenedor = document.getElementById('art-imagenes');
        contenedor.innerHTML = '';

        const urls = imagenesRaw
            .split('|')
            .map(function (url) { return url.trim(); })
            .filter(function (url) { return url !== ''; });

        if (urls.length === 0) {
            const vacio = document.createElement('span');
            vacio.className = 'articulo-galeria-vacia';
            vacio.textContent = 'Este artículo no tiene imágenes.';
            contenedor.appendChild(vacio);
            return;
        }

        urls.forEach(function (url) {
            const img = document.createElement('img');
            img.src = CTX + '/' + url;
            img.alt = 'Imagen del artículo';
            contenedor.appendChild(img);
        });
    }

    function cerrarModalArticulo() {
        modalArticulo.style.display = 'none';
        idArticuloActual = null;
    }

    function procesarArticulo(idArticulo, accion) {
        const mensaje = accion === 'aceptar'
            ? '¿Estás seguro de aprobar este artículo? Después de aprobarlo aparecerá en el catálogo.'
            : '¿Estás seguro de rechazar este artículo? El artículo será eliminado del catálogo.';

        if (!confirm(mensaje)) return;

        fetch(URL_ADMIN_ACTIVIDAD, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: new URLSearchParams({
                tipo: 'articulo',
                accion: accion,
                idArticulo: idArticulo
            })
        })
            .then(function (response) { return response.json(); })
            .then(function (data) {
                alert(data.mensaje);

                if (data.exito) {
                    const fila = document.getElementById('row-articulo-' + idArticulo);
                    if (fila) fila.remove();
                }
            })
            .catch(function (error) {
                console.error('Error:', error);
                alert('Ocurrió un error al procesar el artículo.');
            });
    }

    // =====================================================================
    // DELEGACIÓN DE EVENTOS — TABLA USUARIOS
    // =====================================================================

    cuerpoPeticiones.addEventListener('click', function (e) {
        const boton = e.target.closest('.btn-ver-usuario');
        if (!boton) return;

        abrirModalPeticion({
            matricula: boton.getAttribute('data-matricula'),
            nombre: boton.getAttribute('data-nombre'),
            correo: boton.getAttribute('data-correo'),
            fotoFrente: boton.getAttribute('data-foto-frente'),
            fotoReverso: boton.getAttribute('data-foto-reverso')
        });
    });

    // =====================================================================
    // DELEGACIÓN DE EVENTOS — TABLA ARTÍCULOS
    // =====================================================================

    cuerpoArticulosEspera.addEventListener('click', function (e) {
        const boton = e.target.closest('.btn-ver-articulo');
        if (boton) abrirModalArticulo(boton);
    });

    // =====================================================================
    // BOTONES DENTRO DE LOS MODALES
    // =====================================================================

    btnAceptarUsuario.addEventListener('click', function () {
        procesarPeticion('aceptar');
    });

    btnRechazarUsuario.addEventListener('click', function () {
        procesarPeticion('rechazar');
    });

    btnAprobarArticulo.addEventListener('click', function () {
        if (!idArticuloActual) return;
        procesarArticulo(idArticuloActual, 'aceptar');
        cerrarModalArticulo();
    });

    btnRechazarArticulo.addEventListener('click', function () {
        if (!idArticuloActual) return;
        procesarArticulo(idArticuloActual, 'rechazar');
        cerrarModalArticulo();
    });

    // ---------- Botones de cerrar (X) ----------

    document.getElementById('btnCerrarModalPeticion').addEventListener('click', cerrarModalPeticion);
    document.getElementById('btnCerrarModalArticulo').addEventListener('click', cerrarModalArticulo);

    // ---------- Click fuera del modal ----------

    window.addEventListener('click', function (event) {
        if (event.target === modalPeticion) cerrarModalPeticion();
        if (event.target === modalArticulo) cerrarModalArticulo();
    });
})();