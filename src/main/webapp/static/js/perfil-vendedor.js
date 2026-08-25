let calificacionActual = 0;

function abrirModalComentario() { document.getElementById('modalComentar').style.display = 'flex'; }
function cerrarModalComentario() { document.getElementById('modalComentar').style.display = 'none'; }

document.addEventListener("DOMContentLoaded", () => {
    const contenedorEstrellas = document.getElementById('interactiveStars');
    if (contenedorEstrellas) {
        const estrellas = contenedorEstrellas.querySelectorAll('i');

        estrellas.forEach(estrella => {
            estrella.addEventListener('mouseover', function() {
                const valHover = parseInt(this.getAttribute('data-value'));
                estrellas.forEach(e => {
                    const val = parseInt(e.getAttribute('data-value'));
                    e.classList.toggle('hovered', val <= valHover);
                });
            });

            estrella.addEventListener('click', function() {
                calificacionActual = parseInt(this.getAttribute('data-value'));
                document.getElementById('calificacionSeleccionada').value = calificacionActual;
                estrellas.forEach(e => {
                    const val = parseInt(e.getAttribute('data-value'));
                    e.classList.toggle('active', val <= calificacionActual);
                });
            });
        });

        contenedorEstrellas.addEventListener('mouseleave', function() {
            estrellas.forEach(e => e.classList.remove('hovered'));
        });
    }
});

function enviarComentario(matriculaReceptor) {
    const comentario = document.getElementById('textoComentarioInput').value.trim();
    const calificacion = parseInt(document.getElementById('calificacionSeleccionada').value);

    if (calificacion === 0) return mostrarModalMUA("Selecciona una puntuación en estrellas.", 'error');
    if (!comentario) return mostrarModalMUA("Redacta un comentario.", 'error');

    const datos = new URLSearchParams({ matriculaReceptor, comentario, calificacion });

    fetch(`${CONTEXT_PATH}/comentar-vendedor`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: datos
    })
        .then(res => res.json())
        .then(data => {
            cerrarModalComentario();
            if (data.exito) {
                mostrarModalMUA("¡Comentario publicado con éxito!", 'exito', () => location.reload());
            } else {
                mostrarModalMUA(data.mensaje || "Error al registrar comentario.", 'error');
            }
        });
}