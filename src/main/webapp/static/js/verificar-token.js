window.onload = function() {
    const loader = document.getElementById('loader');
    if(loader) {
        setTimeout(() => {
            loader.classList.add('d-none');
            document.getElementById('status-text').classList.add('d-none');
            document.getElementById('success-msg').classList.remove('d-none');

            setTimeout(() => {
                document.getElementById('tokenForm').submit();
            }, 500);
        }, 1500);
    }
};