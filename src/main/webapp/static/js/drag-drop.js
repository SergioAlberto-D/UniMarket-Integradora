document.addEventListener("DOMContentLoaded", () => {
    const inputImagenes = document.getElementById("imagenes");
    const previewGrid = document.getElementById("previewGrid");
    const dropZone = document.getElementById("dropZone");

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

    // 5. Función que el HTML llamará para borrar una foto específica
    window.eliminarImagen = function(index) {
        const nuevoDt = new DataTransfer();

        // Copiamos todos los archivos EXCEPTO el que el usuario quiso borrar
        for(let i = 0; i < dt.files.length; i++) {
            if (i !== index) {
                nuevoDt.items.add(dt.files[i]);
            }
        }

        dt = nuevoDt;
        inputImagenes.files = dt.files;
        actualizarVistaPrevia();
    };

    // 6. Dibuja las imágenes y los recuadros vacíos
    function actualizarVistaPrevia() {
        previewGrid.innerHTML = "";
        const archivosActuales = Array.from(dt.files);

        for (let i = 0; i < 3; i++) {
            const archivo = archivosActuales[i];

            if (archivo) {
                const lector = new FileReader();
                lector.onload = function (e) {
                    const item = document.createElement("div");
                    item.className = "preview-item";

                    // Aquí agregamos el botón de la 'X' flotante
                    item.innerHTML = `
                        <img src="${e.target.result}" alt="Vista previa">
                        <button type="button" class="btn-eliminar-foto" onclick="eliminarImagen(${i})">
                            <i class="bi bi-x"></i>
                        </button>
                        <span>${i === 0 ? 'Principal' : (i + 1)}</span>
                    `;
                    previewGrid.appendChild(item);
                };
                lector.readAsDataURL(archivo);
            } else {
                const empty = document.createElement("div");
                empty.className = "preview-empty";
                empty.innerHTML = `<i class="bi bi-image"></i><span>${i + 1}</span>`;
                previewGrid.appendChild(empty);
            }
        }
    }
});