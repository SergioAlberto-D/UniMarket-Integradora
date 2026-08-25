/*
 * DOCUMENTACIÓN TÉCNICA — MUA
 * Archivo: src/main/webapp/static/js/categoria.js
 * Propósito: Gestiona altas, edición y acciones de categorías mediante fetch/AJAX.
 * Dependencias/Integración: elementos DOM definidos por la vista JSP asociada y, cuando corresponde, endpoints HTTP de los Servlets.
 * @author Sergio
 * @date 25/08/2025
 */
(function () {
    'use strict';

    const CTX = (window.MUA_CTX && window.MUA_CTX.contextPath) || '';
    const URL_ADMIN_CATEGORIAS = CTX + '/admincategorias';

    const tbody = document.getElementById('cuerpoTablaCategorias');
    const formAgregar = document.getElementById('formAgregarCategoria');
    const inputNombre = document.getElementById('inputNombreCategoria');

    const modal = document.getElementById('modalEditar');
    const formEditar = document.getElementById('formEditarCategoria');
    const modalIdCategoria = document.getElementById('modalIdCategoria');
    const modalNombreCategoria = document.getElementById('modalNombreCategoria');
    const btnCerrarModal = document.getElementById('btnCerrarModal');
    const btnCancelarModal = document.getElementById('btnCancelarModal');

    /**
     * Envío genérico de una acción al servlet vía fetch, siempre como AJAX.
     */
    function enviarAccion(datos) {
        const body = new URLSearchParams(datos);

        return fetch(URL_ADMIN_CATEGORIAS, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: body
        }).then(function (response) {
            return response.json().then(function (data) {
                if (!response.ok || !data.success) {
                    throw new Error(data.message || 'Ocurrió un error al procesar la solicitud.');
                }
                return data;
            });
        });
    }

    function quitarFilaSinDatos() {
        const filaVacia = document.getElementById('rowSinDatos');
        if (filaVacia) filaVacia.remove();
    }

    function mostrarFilaSinDatosSiVacio() {
        if (tbody.querySelector('tr[data-id]')) return;
        if (document.getElementById('rowSinDatos')) return;

        const tr = document.createElement('tr');
        tr.id = 'rowSinDatos';
        tr.innerHTML =
            '<td colspan="3" style="text-align: center; padding: 40px; color: #888;">' +
            'No hay categorías registradas.</td>';
        tbody.appendChild(tr);
    }

    function renumerarFilas() {
        const filas = tbody.querySelectorAll('tr[data-id]');
        filas.forEach(function (fila, index) {
            const celdaNumero = fila.querySelector('.col-numero');
            if (celdaNumero) celdaNumero.innerHTML = '<strong>' + (index + 1) + '</strong>';
        });
    }

    function crearFilaCategoria(id, nombre) {
        const tr = document.createElement('tr');
        tr.setAttribute('data-id', id);
        tr.innerHTML =
            '<td class="col-numero"><strong></strong></td>' +
            '<td class="col-nombre"><strong></strong></td>' +
            '<td style="text-align: right;">' +
            '<div style="display: flex; gap: 8px; justify-content: flex-end;">' +
            '<button type="button" class="btn-update btn-editar-categoria" data-id="' + id + '">Editar</button>' +
            '<button type="button" class="btn-delete btn-eliminar-categoria" data-id="' + id + '">Eliminar</button>' +
            '</div>' +
            '</td>';

        // Se asigna el nombre vía textContent (no innerHTML) para evitar inyección de HTML.
        tr.querySelector('.col-nombre strong').textContent = nombre;
        tr.querySelector('.btn-editar-categoria').setAttribute('data-nombre', nombre);
        tr.querySelector('.btn-eliminar-categoria').setAttribute('data-nombre', nombre);

        return tr;
    }

    // ---------- Agregar ----------

    formAgregar.addEventListener('submit', function (e) {
        e.preventDefault();

        const nombreVal = inputNombre.value.trim();
        if (!nombreVal) return;

        const btnSubmit = formAgregar.querySelector('button[type="submit"]');
        btnSubmit.disabled = true;

        enviarAccion({ accion: 'agregar', nombreCategoria: nombreVal })
            .then(function (data) {
                quitarFilaSinDatos();
                const fila = crearFilaCategoria(data.idCategoria, data.nombreCategoria);
                tbody.appendChild(fila);
                renumerarFilas();
                inputNombre.value = '';
                inputNombre.focus();
            })
            .catch(function (error) {
                console.error('Error:', error);
                alert(error.message || 'Ocurrió un error al guardar la categoría.');
            })
            .finally(function () {
                btnSubmit.disabled = false;
            });
    });

    // ---------- Editar ----------

    function abrirModal(id, nombre) {
        modalIdCategoria.value = id;
        modalNombreCategoria.value = nombre;
        modal.style.display = 'flex';
        modalNombreCategoria.focus();
    }

    function cerrarModal() {
        modal.style.display = 'none';
        formEditar.reset();
    }

    formEditar.addEventListener('submit', function (e) {
        e.preventDefault();

        const idCategoria = modalIdCategoria.value;
        const nombreVal = modalNombreCategoria.value.trim();
        if (!nombreVal) return;

        const btnGuardar = formEditar.querySelector('.btn-guardar-modal');
        btnGuardar.disabled = true;

        enviarAccion({ accion: 'editar', idCategoria: idCategoria, nombreCategoria: nombreVal })
            .then(function (data) {
                const fila = tbody.querySelector('tr[data-id="' + data.idCategoria + '"]');
                if (fila) {
                    fila.querySelector('.col-nombre strong').textContent = data.nombreCategoria;
                    fila.querySelector('.btn-editar-categoria').setAttribute('data-nombre', data.nombreCategoria);
                    fila.querySelector('.btn-eliminar-categoria').setAttribute('data-nombre', data.nombreCategoria);
                }
                cerrarModal();
            })
            .catch(function (error) {
                console.error('Error:', error);
                alert(error.message || 'Ocurrió un error al actualizar la categoría.');
            })
            .finally(function () {
                btnGuardar.disabled = false;
            });
    });

    btnCerrarModal.addEventListener('click', cerrarModal);
    btnCancelarModal.addEventListener('click', cerrarModal);
    window.addEventListener('click', function (event) {
        if (event.target === modal) cerrarModal();
    });

    // ---------- Eliminar ----------

    function eliminarCategoria(id, nombre, fila) {
        const confirmado = confirm(
            "Al eliminar la categoría '" + nombre + "', sus productos asociados pasarán a la categoría 'Otros'. " +
            '¿Deseas continuar?'
        );
        if (!confirmado) return;

        enviarAccion({ accion: 'eliminar', idCategoria: id })
            .then(function () {
                fila.remove();
                renumerarFilas();
                mostrarFilaSinDatosSiVacio();
            })
            .catch(function (error) {
                console.error('Error:', error);
                alert(error.message || 'Ocurrió un error al eliminar la categoría.');
            });
    }

    // ---------- Delegación de eventos (cubre filas agregadas dinámicamente) ----------

    tbody.addEventListener('click', function (e) {
        const btnEditar = e.target.closest('.btn-editar-categoria');
        if (btnEditar) {
            abrirModal(btnEditar.getAttribute('data-id'), btnEditar.getAttribute('data-nombre'));
            return;
        }

        const btnEliminar = e.target.closest('.btn-eliminar-categoria');
        if (btnEliminar) {
            const fila = btnEliminar.closest('tr');
            eliminarCategoria(
                btnEliminar.getAttribute('data-id'),
                btnEliminar.getAttribute('data-nombre'),
                fila
            );
        }
    });
})();
