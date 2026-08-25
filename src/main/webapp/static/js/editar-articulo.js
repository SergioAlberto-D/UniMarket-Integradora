/*
 * DOCUMENTACIÓN TÉCNICA — MUA
 * Archivo: src/main/webapp/static/js/editar-articulo.js
 * Propósito: Gestiona eliminación de imágenes de un artículo mediante POST.
 * Dependencias/Integración: elementos DOM definidos por la vista JSP asociada y, cuando corresponde, endpoints HTTP de los Servlets.
 * @author Sergio
 * @date 25/08/2025
 */
function eliminarImagenServidor(idImagen, botonElemento) {
    mostrarModalMUA("¿Estás seguro de eliminar esta imagen?", "info", () => {
        fetch(`${CONTEXT_PATH}/eliminar-imagen-articulo?id=` + idImagen, { method: 'POST' })
            .then(res => res.json())
            .then(data => {
                if (data.exito) {
                    const caja = botonElemento.closest('.preview-item');
                    if (caja) {
                        caja.className = 'preview-empty';
                        caja.style = '';
                        caja.innerHTML = '<i class="bi bi-image"></i><span>Vacío</span>';
                    }
                    mostrarModalMUA("Imagen eliminada correctamente.", "exito");
                } else {
                    mostrarModalMUA(data.mensaje || "No se pudo eliminar la imagen.", "error");
                }
            });
    });
}
