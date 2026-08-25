/*
 * DOCUMENTACIÓN TÉCNICA — MUA
 * Archivo: src/main/webapp/static/js/historial.js
 * Propósito: Cambia entre las pestañas de compras y ventas del historial.
 * Dependencias/Integración: elementos DOM definidos por la vista JSP asociada y, cuando corresponde, endpoints HTTP de los Servlets.
 * @author Sergio
 * @date 25/08/2025
 */
function cambiarPestana(tipo, btn) {
    document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');

    document.getElementById('tab-compras').style.display = (tipo === 'compras') ? 'flex' : 'none';
    document.getElementById('tab-ventas').style.display = (tipo === 'ventas') ? 'flex' : 'none';
}
