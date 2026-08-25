// ==========================================
// LÓGICA DE VALIDACIÓN DE CONTRASEÑAS
// ==========================================
const passwordInput1 = document.getElementById('passwordNueva');
const passwordInput2 = document.getElementById('passwordConfirmar');

const rulesDiv = document.getElementById('password-rules');
const ruleLength = document.getElementById('rule-length');
const ruleUppercase = document.getElementById('rule-uppercase');
const ruleNumber = document.getElementById('rule-number');
const ruleSpecial = document.getElementById('rule-special');

const matchDiv = document.getElementById('match-rules');
const ruleMatch = document.getElementById('rule-match');

const toggleRule = (element, isValid, textError, textSuccess) => {
    if (isValid) {
        element.classList.remove('text-danger');
        element.classList.add('text-success');
        element.innerHTML = `<i class="bi bi-check-circle-fill icon-rule me-1"></i>${textSuccess || textError}`;
    } else {
        element.classList.remove('text-success');
        element.classList.add('text-danger');
        element.innerHTML = `<i class="bi bi-x-circle icon-rule me-1"></i>${textError}`;
    }
};

// --- INPUT 1: NUEVA CONTRASEÑA ---

// 1. Mostrar reglas al hacer clic (focus)
passwordInput1.addEventListener('focus', () => {
    rulesDiv.classList.remove('d-none');
});

// 2. Ocultar reglas al dar clic fuera (blur)
passwordInput1.addEventListener('blur', () => {
    rulesDiv.classList.add('d-none');
});

// 3. Validar en tiempo real mientras escribe
passwordInput1.addEventListener('input', () => {
    const val = passwordInput1.value;

    toggleRule(ruleLength, val.length >= 8, 'Mínimo 8 caracteres');
    toggleRule(ruleUppercase, /[A-Z]/.test(val), 'Al menos una mayúscula');
    toggleRule(ruleNumber, /\d/.test(val), 'Al menos un número');
    toggleRule(ruleSpecial, /[!@#$%^&*(),.?":{}|<>]/.test(val), 'Al menos un carácter especial (@$!%*?&)');

    if (passwordInput2.value.length > 0) {
        validateMatch();
    }
});


// --- INPUT 2: CONFIRMAR CONTRASEÑA ---

// 1. Mostrar regla de coincidencia al hacer clic (focus)
passwordInput2.addEventListener('focus', () => {
    matchDiv.classList.remove('d-none');
});

// 2. Ocultar regla de coincidencia al dar clic fuera (blur)
passwordInput2.addEventListener('blur', () => {
    matchDiv.classList.add('d-none');
});

// 3. Validar si coinciden
const validateMatch = () => {
    const val1 = passwordInput1.value;
    const val2 = passwordInput2.value;

    const isMatch = (val1 === val2) && (val2.length > 0);
    toggleRule(ruleMatch, isMatch, 'Las contraseñas no coinciden', 'Las contraseñas coinciden');
};

passwordInput2.addEventListener('input', validateMatch);


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