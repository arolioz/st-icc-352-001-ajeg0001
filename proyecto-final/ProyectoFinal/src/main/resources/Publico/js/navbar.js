(() => {
    const navbar = document.getElementById("idNavbar");

    fetch("navbar.html")
        .then(response => response.text())
        .then(data => {

            navbar.innerHTML = data;

            const usuario = localStorage.getItem("usuario");

            if (usuario) {
                document.getElementById("idUsuario").textContent = usuario;
            }

        })
        .catch(error => {
            console.error("Error cargando navbar:", error);
        });
})();