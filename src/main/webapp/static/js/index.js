document.addEventListener("DOMContentLoaded", () => {
    const formFiltros = document.getElementById("formFiltros");
    const contenedorArticulos = document.getElementById("contenedorArticulos");

    if (!formFiltros || !contenedorArticulos) return;

    // Función principal asíncrona
    function aplicarFiltrosAsincronos(e) {
        if (e) e.preventDefault(); // Evitamos que el formulario recargue la página

        const formData = new FormData(formFiltros);
        const queryString = new URLSearchParams(formData).toString();

        // 1. Mostrar estado de carga (Feedback visual)
        contenedorArticulos.innerHTML = `
            <div style="grid-column: 1/-1; text-align: center; padding: 60px; color: #7c3f1d;">
                <i class="bi bi-arrow-repeat" style="font-size: 38px; display: inline-block; animation: girar 1s linear infinite;"></i>
                <p style="margin-top: 10px; font-weight: 600;">Actualizando catálogo...</p>
            </div>
        `;

        // 2. Hacer la petición AJAX al InicioServlet
        // (Usamos la constante CONTEXT_PATH que ya declaramos en el header.jsp)
        fetch(`${CONTEXT_PATH}/inicio?${queryString}`, {
            method: 'GET',
            headers: {
                'X-Requested-With': 'XMLHttpRequest' // Identificador clave para el Servlet
            }
        })
            .then(response => {
                if (!response.ok) throw new Error("Error en la petición de red");
                return response.text(); // Recibimos el HTML generado por articulos-fragment.jsp
            })
            .then(html => {
                // 3. Inyectar el nuevo HTML sin recargar
                contenedorArticulos.innerHTML = html;
            })
            .catch(err => {
                console.error("Error al filtrar:", err);
                contenedorArticulos.innerHTML = `
                <div class="empty-catalog" style="grid-column: 1/-1;">
                    <i class="bi bi-exclamation-triangle"></i>
                    <h2>Error de conexión</h2>
                    <p>No se pudo conectar con el servidor para actualizar los filtros.</p>
                </div>
            `;
            });
    }

    // Escuchar el evento 'change' (cuando cambian los dropdowns)
    const selects = formFiltros.querySelectorAll("select");
    selects.forEach(select => {
        select.addEventListener("change", aplicarFiltrosAsincronos);
    });

    // Escuchar el evento 'submit' (cuando se da clic a "Aplicar precio")
    formFiltros.addEventListener("submit", aplicarFiltrosAsincronos);

    // Añadir animación de giro dinámicamente si no existe
    if (!document.getElementById('animacion-giro')) {
        const style = document.createElement('style');
        style.id = 'animacion-giro';
        style.innerHTML = `@keyframes girar { 100% { transform: rotate(360deg); } }`;
        document.head.appendChild(style);
    }
});