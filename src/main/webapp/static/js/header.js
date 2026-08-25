/*
 * DOCUMENTACIÓN TÉCNICA — MUA
 * Archivo: src/main/webapp/static/js/header.js
 * Propósito: Proporciona el modal universal de mensajes de MUA.
 * Dependencias/Integración: elementos DOM definidos por la vista JSP asociada y, cuando corresponde, endpoints HTTP de los Servlets.
 * @author Sergio
 * @date 25/08/2025
 */
// ==========================================
// MODAL UNIVERSAL MUA
// ==========================================
function mostrarModalMUA(mensaje, tipo = 'info', callback = null) {
    const modal = document.getElementById('muaModalUniversal');
    const titulo = document.getElementById('muaModalTitulo');
    const texto = document.getElementById('muaModalMensaje');
    const icono = document.getElementById('muaModalIcono');
    const btnAceptar = document.getElementById('muaModalBtnAceptar');

    if (!modal) return;

    if (tipo === 'exito') {
        titulo.textContent = '¡Éxito!';
        icono.innerHTML = '<i class="bi bi-check-circle-fill" style="color: #28a745;"></i>';
    } else if (tipo === 'error') {
        titulo.textContent = '¡Atención!';
        icono.innerHTML = '<i class="bi bi-exclamation-triangle-fill" style="color: #d9534f;"></i>';
    } else {
        titulo.textContent = 'Notificación';
        icono.innerHTML = '<i class="bi bi-info-circle-fill" style="color: #7c3f1d;"></i>';
    }

    texto.textContent = mensaje;
    btnAceptar.onclick = function() {
        modal.classList.remove('show-modal');
        if (typeof callback === 'function') callback();
    };

    modal.classList.add('show-modal');
}

// ==========================================
// TOAST FLOTANTE
// ==========================================
function mostrarToast(mensaje, tipo = 'info', titulo = 'Notificación') {
    let container = document.getElementById('muaToastContainer');
    if (!container) {
        container = document.createElement('div');
        container.id = 'muaToastContainer';
        container.className = 'mua-toast-container';
        document.body.appendChild(container);
    }

    const iconos = {
        exito: 'bi-check-circle-fill',
        error: 'bi-exclamation-triangle-fill',
        info: 'bi-info-circle-fill'
    };

    const toast = document.createElement('div');
    toast.className = `mua-toast ${tipo}`;
    toast.innerHTML = `
        <i class="bi ${iconos[tipo] || iconos.info}"></i>
        <div class="mua-toast-text">
            <strong>${titulo}</strong>
            <span>${mensaje}</span>
        </div>
    `;

    container.appendChild(toast);

    setTimeout(() => {
        toast.style.animation = 'toastSlideOut 0.3s forwards';
        setTimeout(() => toast.remove(), 300);
    }, 4000);
}

// ==========================================
// BANDEJA DE NOTIFICACIONES
// ==========================================
function toggleBandejaNotificaciones(e) {
    e.stopPropagation();
    const tray = document.getElementById('trayNotificaciones');
    if (tray) tray.classList.toggle('show');
}

window.addEventListener('click', function(e) {
    const dropdown = document.getElementById('dropdownNotificaciones');
    const tray = document.getElementById('trayNotificaciones');
    if (dropdown && tray && !dropdown.contains(e.target)) {
        tray.classList.remove('show');
    }
});

function cargarNotificacionesReales() {
    fetch(`${CONTEXT_PATH}/api/notificaciones`)
        .then(response => response.json())
        .then(data => {
            const contenedor = document.getElementById('listaNotificacionesDinamica');
            const badge = document.getElementById('badgeCount');

            if (!contenedor) return;

            if (!data || data.length === 0) {
                contenedor.innerHTML = '<p style="padding: 20px; text-align: center; color: #888; font-size: 13px; margin: 0;">No tienes notificaciones pendientes.</p>';
                if (badge) badge.style.display = 'none';
                return;
            }

            if (badge) {
                badge.textContent = data.length;
                badge.style.display = 'inline-block';
            }

            let html = '';
            data.forEach(item => {
                const colorIcono = item.tipo === 'OFERTA' ? 'bg-brown' : 'bg-green';
                const icono = item.tipo === 'OFERTA' ? 'bi-tag-fill' : 'bi-check-lg';

                html += `
                    <div class="tray-item unread">
                        <div class="tray-icon ${colorIcono}"><i class="bi ${icono}"></i></div>
                        <div class="tray-content">
                            <p>${item.mensaje}</p>
                            <span class="tray-time">${item.tiempo}</span>
                        </div>
                    </div>`;
            });
            contenedor.innerHTML = html;
        })
        .catch(error => {
            console.error("Error al cargar notificaciones:", error);
            const contenedor = document.getElementById('listaNotificacionesDinamica');
            if (contenedor) contenedor.innerHTML = '<p style="padding: 15px; text-align: center; color: #888; font-size: 13px; margin: 0;">Sin notificaciones recientes.</p>';
        });
}

function marcarTodasLeidas() {
    fetch(`${CONTEXT_PATH}/api/notificaciones/marcar-leidas`, { method: 'POST' })
        .then(() => {
            document.querySelectorAll('.tray-item.unread').forEach(item => item.classList.remove('unread'));
            const badge = document.getElementById('badgeCount');
            if (badge) badge.style.display = 'none';
        })
        .catch(err => console.error("Error al marcar como leídas:", err));
}

document.addEventListener('DOMContentLoaded', cargarNotificacionesReales);
