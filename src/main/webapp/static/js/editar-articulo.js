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