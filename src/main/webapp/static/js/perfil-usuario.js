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