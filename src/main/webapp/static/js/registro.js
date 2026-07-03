// ==========================================
// NAVEGACIÓN POR PASOS Y BARRA DE PROGRESO
// ==========================================
const step1 = document.getElementById('step-1');
const step2 = document.getElementById('step-2');
const btnNext = document.getElementById('btnNext');
const btnBack = document.getElementById('btnBack');

const progressBar = document.getElementById('form-progress');
const progressText = document.getElementById('progress-text');
const progressInputs = document.querySelectorAll('.calc-progress');

// 1. LÓGICA DEL BOTÓN SIGUIENTE
btnNext.addEventListener('click', () => {
    const step1Elements = step1.querySelectorAll('input[required], select[required]');
    let allValid = true;

    step1Elements.forEach(element => {
        // Verificamos si el valor está vacío
        if (element.value.trim() === "") {
            element.classList.add('is-invalid');
            allValid = false;
        } else {
            element.classList.remove('is-invalid');
        }
    });
    if (allValid) {
        step1.classList.add('d-none');
        step2.classList.remove('d-none');
    }
});

step1.querySelectorAll('input[required], select[required]').forEach(element => {
    element.addEventListener('input', () => element.classList.remove('is-invalid'));
    element.addEventListener('change', () => element.classList.remove('is-invalid'));
});

// Regresar al Paso 1
btnBack.addEventListener('click', () => {
    step2.classList.add('d-none');
    step1.classList.remove('d-none');
});

// Actualizar barra de progreso dinámicamente
const updateProgress = () => {
    let filledCount = 0;
    const totalRequired = progressInputs.length;

    progressInputs.forEach(input => {
        if (input.value.trim() !== "") {
            filledCount++;
        }
    });

    const percentage = Math.round((filledCount / totalRequired) * 100);

    progressBar.style.width = `${percentage}%`;
    progressBar.setAttribute('aria-valuenow', percentage);
    progressText.innerText = `${percentage}% completado`;
};

// Escuchar cambios en cada campo para actualizar la barra
progressInputs.forEach(input => {
    input.addEventListener('input', updateProgress);
    input.addEventListener('change', updateProgress);
});

updateProgress(); // Llamada inicial

// ==========================================
// LÓGICA DE VALIDACIÓN DE CONTRASEÑAS
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
        element.innerHTML = `<i class="bi bi-check-circle-fill icon-rule"></i>${textSuccess || textError}`;
    } else {
        element.classList.remove('text-success');
        element.classList.add('text-danger');
        element.innerHTML = `<i class="bi bi-x-circle icon-rule"></i>${textError}`;
    }
};

passwordInput1.addEventListener('focus', () => {
    rulesDiv.classList.remove('d-none');
});

passwordInput1.addEventListener('blur', () => {
    rulesDiv.classList.add('d-none');
});

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

passwordInput2.addEventListener('focus', () => {
    matchDiv.classList.remove('d-none');
});

passwordInput2.addEventListener('blur', () => {
    matchDiv.classList.add('d-none');
});

const validateMatch = () => {
    const val1 = passwordInput1.value;
    const val2 = passwordInput2.value;

    const isMatch = (val1 === val2) && (val2.length > 0);

    toggleRule(ruleMatch, isMatch, 'Las contraseñas no coinciden', 'Las contraseñas coinciden');
};

passwordInput2.addEventListener('input', validateMatch);