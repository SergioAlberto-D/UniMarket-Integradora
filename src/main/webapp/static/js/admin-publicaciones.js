/*
 * DOCUMENTACIÓN TÉCNICA — MUA
 * Archivo: src/main/webapp/static/js/admin-publicaciones.js
 * Propósito: Script de interfaz correspondiente al módulo admin-publicaciones.
 * Dependencias/Integración: elementos DOM definidos por la vista JSP asociada y, cuando corresponde, endpoints HTTP de los Servlets.
 * @author Sergio
 * @date 25/08/2025
 */
document.querySelectorAll('.form-eliminar-publicacion').forEach(form => {
    form.addEventListener('submit', function(e) {
        e.preventDefault(); // Detiene la recarga de la página

        if (!confirm('¿Deseas dar de baja esta publicación?')) {
            return;
        }

        const formData = new URLSearchParams(new FormData(this));
        const botonEliminar = this.querySelector('.btn-delete');
        const fila = this.closest('tr');

        // Toma la ruta exacta del atributo "action" del formulario
        const urlDestino = this.getAttribute('action');

        // Deshabilita el botón mientras procesa
        botonEliminar.disabled = true;
        botonEliminar.innerText = 'Eliminando...';

        fetch(urlDestino, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: formData.toString() // Convertimos a String para evitar fallos de compatibilidad
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error HTTP ' + response.status);
                }
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    // Eliminamos la fila de la tabla visualmente
                    fila.remove();

                    // Si ya no quedan filas en la tabla, mostramos el mensaje de vacío
                    const tbody = document.querySelector('#tablaDatos tbody');
                    if (tbody.querySelectorAll('tr').length === 0) {
                        tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; padding: 40px; color: #888;">No hay publicaciones registradas por el momento.</td></tr>';
                    }
                } else {
                    alert('Error: ' + data.message);
                    botonEliminar.disabled = false;
                    botonEliminar.innerText = 'Eliminar';
                }
            })
            .catch(error => {
                console.error('Detalle del error:', error);
                alert('Ocurrió un error al conectar con el servidor. Presiona F12 y revisa la Consola.');
                botonEliminar.disabled = false;
                botonEliminar.innerText = 'Eliminar';
            });
    });
});
