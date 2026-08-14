// ==========================================
// MÁSCARA PARA NÚMERO DE TELÉFONO
// ==========================================
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
// ==========================================
// MÁSCARA PARA NÚMERO DE TELÉFONO
// ==========================================
const formatearTelefono = (event) => {
    let input = event.target;
    let valor = input.value.replace(/\D/g, '');
    if (valor.length > 10) valor = valor.substring(0, 10);

    let valorFormateado = '';
    if (valor.length > 0) valorFormateado = '(' + valor.substring(0, 3);
    if (valor.length >= 4) valorFormateado += ')-' + valor.substring(3, 6);
    if (valor.length >= 7) valorFormateado += '-' + valor.substring(6, 10);
    input.value = valorFormateado;
};

const inputTelefono = document.getElementById('telefonoNuevo');
if (inputTelefono) inputTelefono.addEventListener('input', formatearTelefono);

// ==========================================
// CONTROL DE MODALES GENERALES
// ==========================================
function abrirModal(id) {
    const el = document.getElementById(id);
    if (el) el.classList.add("show-modal");
}

function cerrarModal(id) {
    const el = document.getElementById(id);
    if (el) el.classList.remove("show-modal");
}

window.addEventListener('click', function(e) {
    if (e.target.classList.contains('modal-overlay')) {
        e.target.classList.remove('show-modal');
    }
});

// ==========================================
// GESTIÓN DE OFERTAS
// ==========================================
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

function toggleMenu(boton) {
    document.querySelectorAll('.menu-flotante.show').forEach(menu => {
        if (menu !== boton.nextElementSibling) menu.classList.remove('show');
    });
    boton.nextElementSibling.classList.toggle('show');
}

window.addEventListener('click', function(e) {
    if (!e.target.closest('.dropdown-hamburguesa')) {
        document.querySelectorAll('.menu-flotante.show').forEach(m => m.classList.remove('show'));
    }
});

function responderOferta(idOferta, nuevoEstado) {
    const tituloEl = document.getElementById('tituloModalOferta');
    const textoEl = document.getElementById('textoModalOferta');
    const btnEjecutar = document.getElementById('btnEjecutarAccionOferta');

    if (!tituloEl || !textoEl || !btnEjecutar) return;

    if (nuevoEstado === 'ACEPTADA') {
        tituloEl.textContent = 'Aceptar oferta';
        textoEl.textContent = '¿Estás seguro de aceptar esta oferta de compra?';
    } else if (nuevoEstado === 'RECHAZADA') {
        tituloEl.textContent = 'Rechazar oferta';
        textoEl.textContent = '¿Estás seguro de rechazar esta oferta? La propuesta se eliminará de tu lista.';
    } else {
        tituloEl.textContent = 'Cancelar oferta';
        textoEl.textContent = '¿Estás seguro de retirar tu oferta de compra? La propuesta se eliminará.';
    }

    btnEjecutar.onclick = function() {
        cerrarModal('modalConfirmarOferta');
        const datos = new URLSearchParams({ idOferta, estado: nuevoEstado });

        fetch(`${CONTEXT_PATH}/responder-oferta`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: datos
        })
            .then(response => response.json())
            .then(data => {
                if (typeof mostrarToast === 'function') {
                    mostrarToast(data.mensaje, data.exito ? 'exito' : 'error', data.exito ? 'Operación exitosa' : 'Atención');
                }
                if (data.exito) setTimeout(() => location.reload(), 1200);
            });
    };
    abrirModal('modalConfirmarOferta');
}

let idOfertaSeleccionada = null;
function abrirModalAceptarOferta(idOferta, nombreArticulo, monto, imagenArticulo) {
    idOfertaSeleccionada = idOferta;
    document.getElementById('nombreAceptarOferta').textContent = nombreArticulo;
    document.getElementById('montoAceptarOferta').textContent = '$' + monto + ' MXN';
    document.getElementById('imgAceptarOferta').src = `${CONTEXT_PATH}/` + imagenArticulo;
    document.getElementById('inputMensajeAceptarOferta').value = '';
    abrirModal('modalAceptarOferta');
}

function formatearNumeroWhatsapp(numero) {
    if (!numero) return null;
    let limpio = numero.replace(/\D/g, '');
    if (limpio.length === 10) limpio = '52' + limpio;
    return limpio;
}

function ejecutarAceptarOfertaAjax(metodoContacto) {
    if (!idOfertaSeleccionada) return;
    const mensaje = document.getElementById('inputMensajeAceptarOferta').value.trim();
    const datos = new URLSearchParams({ idOferta: idOfertaSeleccionada, estado: 'ACEPTADA', mensaje });

    fetch(`${CONTEXT_PATH}/responder-oferta`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: datos
    })
        .then(res => res.json())
        .then(data => {
            cerrarModal('modalAceptarOferta');
            if (!data.exito) {
                mostrarToast(data.mensaje || 'No se pudo aceptar.', 'error');
                return;
            }

            if (metodoContacto === 'whatsapp') {
                const telefono = formatearNumeroWhatsapp(data.telefonoComprador);
                if(telefono) window.open(`https://wa.me/${telefono}?text=${encodeURIComponent(data.mensajeChat)}`, '_blank');
            } else {
                const correo = data.correoComprador;
                if(correo) window.open(`https://mail.google.com/mail/?view=cm&fs=1&tf=1&to=${encodeURIComponent(correo)}&su=Oferta aceptada en MUA&body=${encodeURIComponent(data.mensajeChat)}`, '_blank');
            }
            mostrarToast(data.mensaje || 'Oferta aceptada.', 'exito');
            setTimeout(() => location.reload(), 1500);
        });
}

// ==========================================
// EDITOR DE FOTOS PERFIL (CANVAS)
// ==========================================
let imagenActual = new Image();
let zoomLevel = 1.0;
let offsetX = 0, offsetY = 0;
let isDragging = false, startX = 0, startY = 0;

function abrirEditorFoto(input) {
    if (input.files && input.files[0]) {
        const reader = new FileReader();
        reader.onload = function(e) {
            imagenActual.onload = function() {
                zoomLevel = 1.0; offsetX = 0; offsetY = 0;
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
    if(!canvas) return;
    const ctx = canvas.getContext('2d');
    canvas.width = 300; canvas.height = 300;

    ctx.clearRect(0, 0, canvas.width, canvas.height);
    ctx.save();
    const cx = canvas.width / 2;
    const cy = canvas.height / 2;
    ctx.translate(cx + offsetX, cy + offsetY);
    ctx.scale(zoomLevel, zoomLevel);

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

const editorArea = document.getElementById('editorArea');
if (editorArea) {
    editorArea.addEventListener('mousedown', e => { isDragging = true; startX = e.clientX - offsetX; startY = e.clientY - offsetY; });
    window.addEventListener('mousemove', e => { if (isDragging) { offsetX = e.clientX - startX; offsetY = e.clientY - startY; dibujarCanvas(); }});
    window.addEventListener('mouseup', () => isDragging = false);
}

function guardarFotoPerfil() {
    const canvas = document.getElementById('canvasEditor');
    const base64Image = canvas.toDataURL('image/jpeg', 0.9);

    fetch(`${CONTEXT_PATH}/actualizar-foto-perfil`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: 'fotoBase64=' + encodeURIComponent(base64Image)
    })
        .then(res => res.json())
        .then(data => {
            cerrarModal('modalEditorFoto');
            if (data.exito) {
                mostrarModalMUA("Foto actualizada.", 'exito', () => location.reload());
            } else {
                mostrarModalMUA(data.mensaje || 'Error al actualizar foto.', 'error');
            }
        });
}