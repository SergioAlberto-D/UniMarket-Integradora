// ==========================================
// PESTAÑAS Y MENÚS
// ==========================================
let articuloVendidoIdTemporal = null;

function cambiarPestana(tipo, btn) {
    document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
    document.getElementById('tab-disponibles').style.display = (tipo === 'disponibles') ? 'flex' : 'none';
    document.getElementById('tab-proceso').style.display = (tipo === 'proceso') ? 'flex' : 'none';
}

window.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.get('tab') === 'proceso') {
        const btnProceso = document.querySelectorAll('.tab-btn')[1];
        if (btnProceso) cambiarPestana('proceso', btnProceso);
    }
});

function toggleMenuAction(e, menuId) {
    e.stopPropagation();
    document.querySelectorAll('.dropdown-menu-acciones').forEach(menu => {
        if (menu.id !== menuId) menu.classList.remove('show');
    });
    const targetMenu = document.getElementById(menuId);
    if (targetMenu) targetMenu.classList.toggle('show');
}

window.addEventListener('click', function() {
    document.querySelectorAll('.dropdown-menu-acciones').forEach(menu => menu.classList.remove('show'));
});

// ==========================================
// ESTADOS Y ELIMINACIÓN
// ==========================================
function actualizarEstadoTransaccion(idArticulo, nuevoEstado) {
    const datos = new URLSearchParams({ idArticulo, estado: nuevoEstado });

    fetch(`${CONTEXT_PATH}/actualizar-transaccion`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: datos
    })
        .then(res => res.json())
        .then(data => {
            if (data.exito) {
                if (nuevoEstado === 'COMPLETADO') {
                    articuloVendidoIdTemporal = idArticulo;
                    document.getElementById('modalDecisionVenta').style.display = 'flex';
                } else {
                    mostrarModalMUA(data.mensaje, 'exito', () => location.reload());
                }
            } else {
                mostrarModalMUA(data.mensaje || "Ocurrió un error.", 'error');
            }
        });
}

function accionSeguirVendiendo() {
    document.getElementById('modalDecisionVenta').style.display = 'none';
    location.reload();
}

function accionQuitarCatalogo() {
    if (!articuloVendidoIdTemporal) return;
    document.getElementById('modalDecisionVenta').style.display = 'none';
    eliminarArticuloBase(articuloVendidoIdTemporal);
}

function confirmarEliminacion(idArticulo) {
    mostrarModalMUA("¿Estás seguro de eliminar este artículo?", "info", () => {
        eliminarArticuloBase(idArticulo);
    });
}

function eliminarArticuloBase(idArticulo) {
    fetch(`${CONTEXT_PATH}/eliminar-articulo?id=` + idArticulo, { method: 'POST' })
        .then(res => res.json())
        .then(data => {
            if (data.exito) {
                mostrarModalMUA("Artículo eliminado correctamente.", 'exito', () => location.reload());
            } else {
                mostrarModalMUA(data.mensaje || "No se pudo eliminar.", 'error', () => location.reload());
            }
        });
}