document.querySelectorAll('.toggle-password').forEach(icon => {
    icon.addEventListener('click', function() {
        const targetId = this.getAttribute('data-target');
        const input = document.getElementById(targetId);
        if (input.type === 'password') {
            input.type = 'text';
            this.classList.remove('bi-eye-slash');
            this.classList.add('bi-eye');
        } else {
            input.type = 'password';
            this.classList.remove('bi-eye');
            this.classList.add('bi-eye-slash');
        }
    });
});
// ==========================================
// MOSTRAR / OCULTAR CONTRASEÑA
// ==========================================
document.querySelectorAll('.toggle-password').forEach(icon => {
    icon.addEventListener('click', function() {
        const targetId = this.getAttribute('data-target');
        const input = document.getElementById(targetId);
        if (input.type === 'password') {
            input.type = 'text';
            this.classList.remove('bi-eye-slash');
            this.classList.add('bi-eye');
        } else {
            input.type = 'password';
            this.classList.remove('bi-eye');
            this.classList.add('bi-eye-slash');
        }
    });
});

// ==========================================
// MODAL DE VERIFICACIÓN AUTOMÁTICO
// ==========================================
document.addEventListener("DOMContentLoaded", function() {
    var modalElement = document.getElementById('modalVerificacion');
    if (modalElement) {
        var modalObj = new bootstrap.Modal(modalElement);
        modalObj.show();
    }
});
