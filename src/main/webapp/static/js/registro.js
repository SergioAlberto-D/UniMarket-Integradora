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

// Función para validar en tiempo real y habilitar/deshabilitar el botón
const checkStep1Validity = () => {
    const step1Elements = step1.querySelectorAll('input[required], select[required]');
    let allValid = true;

    step1Elements.forEach(element => {
        if (element.value.trim() === "") {
            allValid = false;
        }
    });
    btnNext.disabled = !allValid;
};

step1.querySelectorAll('input[required], select[required]').forEach(element => {
    element.addEventListener('input', () => {
        element.classList.remove('is-invalid');
        checkStep1Validity();
    });
    element.addEventListener('change', () => {
        element.classList.remove('is-invalid');
        checkStep1Validity();
    });
});


checkStep1Validity();

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

// ==========================================
// MÁSCARA PARA NÚMERO DE TELÉFONO: (XXX)-XXX-XXXX
// ==========================================
const formatearTelefono = (event) => {
    let input = event.target;
    // 1. Elimina todo lo que no sea un número (letras, espacios, símbolos)
    let valor = input.value.replace(/\D/g, ''); 

    // 2. Limita a un máximo de 10 números
    if (valor.length > 10) {
        valor = valor.substring(0, 10);
    }

    // 3. Construye el formato paso a paso según la cantidad de números
    let valorFormateado = '';
    
    if (valor.length > 0) {
        valorFormateado = '(' + valor.substring(0, 3);
    }
    if (valor.length >= 4) {
        valorFormateado += ')-' + valor.substring(3, 6);
    }
    if (valor.length >= 7) {
        valorFormateado += '-' + valor.substring(6, 10);
    }

    // 4. Asigna el valor formateado de vuelta al input
    input.value = valorFormateado;
};

// Selecciona el input de teléfono del registro y le asigna el evento
const inputTelefono = document.getElementById('txtTel');
if (inputTelefono) {
    // Escucha cada vez que el usuario escribe, borra o pega algo
    inputTelefono.addEventListener('input', formatearTelefono);
}

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

// ==========================================
// MOSTRAR / OCULTAR CONTRASEÑA
// ==========================================
document.querySelectorAll('.toggle-password').forEach(icon => {
    icon.addEventListener('click', function() {
        // Encontrar el input relacionado a este icono a través de su data-target
        const targetId = this.getAttribute('data-target');
        const input = document.getElementById(targetId);
        
        // Alternar el tipo de input (texto/password)
        if (input.type === 'password') {
            input.type = 'text';
            // Alternar iconos
            this.classList.remove('bi-eye-slash');
            this.classList.add('bi-eye');
        } else {
            input.type = 'password';
            // Alternar iconos
            this.classList.remove('bi-eye');
            this.classList.add('bi-eye-slash');
        }
    });
});