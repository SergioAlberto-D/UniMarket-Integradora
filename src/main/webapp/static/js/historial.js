function cambiarPestana(tipo, btn) {
    document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');

    document.getElementById('tab-compras').style.display = (tipo === 'compras') ? 'flex' : 'none';
    document.getElementById('tab-ventas').style.display = (tipo === 'ventas') ? 'flex' : 'none';
}