// ==========================================
// LÓGICA DE VALIDACIÓN DE CONTRASEÑAS (Reutilizada de Registro)
// ==========================================
const passwordInput1 = document.getElementById('txtPassword1');
const passwordInput2 = document.getElementById('txtPassword2');

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

// Listeners para Contraseña 1
passwordInput1.addEventListener('focus', () => rulesDiv.classList.remove('d-none'));
passwordInput1.addEventListener('blur', () => rulesDiv.classList.add('d-none'));

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

// Listeners para Contraseña 2
passwordInput2.addEventListener('focus', () => matchDiv.classList.remove('d-none'));
passwordInput2.addEventListener('blur', () => matchDiv.classList.add('d-none'));

const validateMatch = () => {
    const val1 = passwordInput1.value;
    const val2 = passwordInput2.value;

    const isMatch = (val1 === val2) && (val2.length > 0);

    toggleRule(ruleMatch, isMatch, 'Las contraseñas no coinciden', 'Las contraseñas coinciden');
};

passwordInput2.addEventListener('input', validateMatch);