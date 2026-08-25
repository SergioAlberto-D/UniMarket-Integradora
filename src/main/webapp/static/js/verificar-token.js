/*
 * DOCUMENTACIÓN TÉCNICA — MUA
 * Archivo: src/main/webapp/static/js/verificar-token.js
 * Propósito: Automatiza la transición del formulario de verificación de token.
 * Dependencias/Integración: elementos DOM definidos por la vista JSP asociada y, cuando corresponde, endpoints HTTP de los Servlets.
 * @author Sergio
 * @date 25/08/2025
 */
window.onload = function() {
    const loader = document.getElementById('loader');
    if(loader) {
        setTimeout(() => {
            loader.classList.add('d-none');
            document.getElementById('status-text').classList.add('d-none');
            document.getElementById('success-msg').classList.remove('d-none');

            setTimeout(() => {
                document.getElementById('tokenForm').submit();
            }, 500);
        }, 1500);
    }
};
