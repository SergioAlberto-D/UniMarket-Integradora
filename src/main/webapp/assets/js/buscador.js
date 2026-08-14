document.addEventListener('DOMContentLoaded', function() {
    const inputBuscar = document.getElementById('inputBuscar');
    const tablaDatos = document.getElementById('tablaDatos');

    if (inputBuscar && tablaDatos) {
        inputBuscar.addEventListener('keyup', function() {
            const textoBusqueda = this.value.toLowerCase();
            const filas = tablaDatos.querySelectorAll('tbody tr');

            filas.forEach(fila => {
                const contenidoFila = fila.textContent.toLowerCase();
                if (contenidoFila.includes(textoBusqueda)) {
                    fila.style.display = '';
                } else {
                    fila.style.display = 'none';
                }
            });
        });
    }
});