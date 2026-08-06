document.addEventListener("DOMContentLoaded", function () {
    const btnHamburger = document.getElementById("btnHamburger");
    const sidebar = document.getElementById("sidebar");
    const sidebarOverlay = document.getElementById("sidebarOverlay");

    function toggleSidebar() {
        if (sidebar && sidebarOverlay) {
            sidebar.classList.toggle("active");
            sidebarOverlay.classList.toggle("active");
        }
    }

    if (btnHamburger) {
        btnHamburger.addEventListener("click", toggleSidebar);
    }

    if (sidebarOverlay) {
        sidebarOverlay.addEventListener("click", toggleSidebar);
    }
});