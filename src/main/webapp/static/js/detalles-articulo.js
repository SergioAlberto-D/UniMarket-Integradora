// ==========================================
// GALERÍA Y CARRUSEL
// ==========================================
let indiceImagenActual = 0;
let carruselIntervalo;

function cambiarImagen(elemento, rutaImagen, indice) {
    document.getElementById("imagenPrincipal").src = rutaImagen;
    document.querySelectorAll(".thumb").forEach(m => m.classList.remove("active"));
    elemento.classList.add("active");
    if(indice !== undefined) indiceImagenActual = indice;
}

function avanzarCarrusel() {
    const miniaturas = document.querySelectorAll(".thumb");
    if (miniaturas.length <= 1) return;
    indiceImagenActual = (indiceImagenActual + 1) % miniaturas.length;
    miniaturas[indiceImagenActual].click();
}

function iniciarCarrusel() {
    const miniaturas = document.querySelectorAll(".thumb");
    if (miniaturas.length > 1) {
        carruselIntervalo = setInterval(avanzarCarrusel, 15000);
    }
}

function detenerCarrusel() {
    clearInterval(carruselIntervalo);
}

document.addEventListener('DOMContentLoaded', () => {
    iniciarCarrusel();
    const galeria = document.querySelector('.product-gallery');
    if (galeria) {
        galeria.addEventListener('mouseenter', detenerCarrusel);
        galeria.addEventListener('mouseleave', iniciarCarrusel);
    }
});

// ==========================================
// MODAL DE ZOOM
// ==========================================
function abrirModalZoom() {
    const modal = document.getElementById("imageModal");
    const modalImg = document.getElementById("modalImage");
    modalImg.src = document.getElementById("imagenPrincipal").src;
    modal.style.display = "flex";
    detenerCarrusel();
}

function cerrarModalZoom(event) {
    if (event.target.id === "imageModal" || event.target.className === "close-modal") {
        document.getElementById("imageModal").style.display = "none";
        resetearZoom();
        iniciarCarrusel();
    }
}

function hacerZoom(event) {
    const img = document.getElementById("modalImage");
    const x = (event.offsetX / img.offsetWidth) * 100;
    const y = (event.offsetY / img.offsetHeight) * 100;
    img.style.transformOrigin = `${x}% ${y}%`;
    img.style.transform = "scale(2.5)";
}

function resetearZoom() {
    const img = document.getElementById("modalImage");
    img.style.transformOrigin = "center center";
    img.style.transform = "scale(1)";
}

// ==========================================
// OFERTAS Y COMPRAS
// ==========================================
function cerrarModalOferta() {
    document.getElementById('modalOferta').style.display = 'none';
}

function enviarOfertaAjax(idArticulo, matriculaVendedor) {
    const monto = document.getElementById('inputMontoOferta').value;
    if (!monto || parseFloat(monto) <= 0) {
        mostrarModalMUA("Ingresa un monto válido.", 'error');
        return;
    }

    const datos = new URLSearchParams({ idArticulo, matriculaVendedor, monto });

    fetch(`${CONTEXT_PATH}/ofertar-articulo`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: datos
    })
        .then(res => res.json())
        .then(data => {
            if (data.exito) {
                cerrarModalOferta();
                document.getElementById('inputMontoOferta').value = '';
                mostrarModalMUA(data.mensaje || "¡Oferta enviada!", 'exito');
            } else {
                mostrarModalMUA(data.mensaje || "Error al enviar la oferta.", 'error');
            }
        });
}

function abrirModalCompra() {
    document.getElementById('modalCompra').style.display = 'flex';
}

function cerrarModalCompra() {
    document.getElementById('modalCompra').style.display = 'none';
}

function formatearNumeroWhatsapp(numero) {
    if (!numero) return null;
    let limpio = numero.replace(/\D/g, '');
    if (limpio.length === 10) limpio = '52' + limpio;
    return limpio;
}

function ejecutarCompraAjax(metodoContacto, idArticulo) {
    const mensaje = document.getElementById('inputMensajeComprador').value.trim();
    const datos = new URLSearchParams({ idArticulo, mensaje });

    fetch(`${CONTEXT_PATH}/comprar-articulo`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: datos
    })
        .then(res => res.json())
        .then(data => {
            cerrarModalCompra();
            if (data.exito) {
                if (metodoContacto === 'whatsapp') {
                    const telefonoDestino = formatearNumeroWhatsapp(data.telefonoVendedor);
                    if (telefonoDestino) {
                        mostrarModalMUA("Abriremos WhatsApp.", 'exito', () => {
                            window.open("https://wa.me/" + telefonoDestino + "?text=" + encodeURIComponent(data.mensajeChat), '_blank');
                            setTimeout(() => location.reload(), 1500);
                        });
                    } else {
                        mostrarModalMUA("No se encontró número de WhatsApp.", 'error', () => location.reload());
                    }
                } else {
                    const correo = data.correoVendedor;
                    if (correo) {
                        mostrarModalMUA("Abriremos Gmail.", 'exito', () => {
                            window.open(`https://mail.google.com/mail/?view=cm&fs=1&tf=1&to=${encodeURIComponent(correo)}&su=Interés en compra - MUA&body=${encodeURIComponent(data.mensajeChat)}`, '_blank');
                            setTimeout(() => location.reload(), 1500);
                        });
                    }
                }
            } else {
                mostrarModalMUA(data.mensaje || "Error en la compra.", 'error');
            }
        });
}