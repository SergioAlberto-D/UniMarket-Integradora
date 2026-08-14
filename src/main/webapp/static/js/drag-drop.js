document.addEventListener("DOMContentLoaded", () => {
    const inputImagenes = document.getElementById("imagenes");
    const previewGrid = document.getElementById("previewGrid");
    const dropZone = document.getElementById("dropZone");

    if (!inputImagenes || !previewGrid || !dropZone) return;

    // DataTransfer nos permite manipular la lista de archivos de un input=file
    let dt = new DataTransfer();

    // 1. Evitar comportamientos por defecto del navegador al arrastrar
    ['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
        dropZone.addEventListener(eventName, preventDefaults, false);
        document.body.addEventListener(eventName, preventDefaults, false);
    });

    function preventDefaults(e) {
        e.preventDefault();
        e.stopPropagation();
    }

    // 2. Efecto visual al pasar una imagen por encima
    ['dragenter', 'dragover'].forEach(eventName => {
        dropZone.addEventListener(eventName, () => dropZone.classList.add('drag-over'), false);
    });

    ['dragleave', 'drop'].forEach(eventName => {
        dropZone.addEventListener(eventName, () => dropZone.classList.remove('drag-over'), false);
    });

    // 3. Cuando se sueltan las imágenes
    dropZone.addEventListener('drop', (e) => {
        const files = e.dataTransfer.files;
        procesarArchivos(files);
    });

    // 4. Cuando se usa el botón de "Seleccionar archivos"
    inputImagenes.addEventListener('change', function() {
        procesarArchivos(this.files);
    });

    function procesarArchivos(archivosNuevos) {
        Array.from(archivosNuevos).forEach(archivo => {
            // Solo procesamos si no nos pasamos de 3 y si son imágenes
            if (dt.items.length < 3 && archivo.type.startsWith('image/')) {
                dt.items.add(archivo);
            }
        });

        // Sincronizamos nuestro DataTransfer con el input real del formulario
        inputImagenes.files = dt.files;
        actualizarVistaPrevia();
    }

    // 5. Función global para borrar una foto específica por su índice
    window.eliminarImagen = function(index) {
        const nuevoDt = new DataTransfer();

        // Copiamos todos los archivos EXCEPTO el que el usuario quiso borrar
        for (let i = 0; i < dt.files.length; i++) {
            if (i !== index) {
                nuevoDt.items.add(dt.files[i]);
            }
        }

        dt = nuevoDt;
        inputImagenes.files = dt.files;
        actualizarVistaPrevia();
    };

    // 6. Dibuja las imágenes manteniendo SIEMPRE el orden 1, 2 y 3
    function actualizarVistaPrevia() {
        previewGrid.innerHTML = "";
        const archivosActuales = Array.from(dt.files);

        for (let i = 0; i < 3; i++) {
            const archivo = archivosActuales[i];

            // CREAMOS EL CONTENEDOR DE FORMA SÍNCRONA PARA RESERVAR SU POSICIÓN EXACTA
            const item = document.createElement("div");
            item.className = archivo ? "preview-item" : "preview-empty";
            previewGrid.appendChild(item);

            if (archivo) {
                const lector = new FileReader();
                lector.onload = function (e) {
                    // Cuando termina la lectura, rellenamos la caja que ya está en la posición correcta
                    item.innerHTML = `
                        <img src="${e.target.result}" alt="Vista previa" style="width: 100%; height: 100%; object-fit: cover; border-radius: 8px;">
                        <button type="button" class="btn-eliminar-foto" onclick="eliminarImagen(${i})">
                            <i class="bi bi-x"></i>
                        </button>
                        <span>${i === 0 ? 'Principal' : (i + 1)}</span>
                    `;
                };
                lector.readAsDataURL(archivo);
            } else {
                item.innerHTML = `<i class="bi bi-image"></i><span>${i + 1}</span>`;
            }
        }
    }
});