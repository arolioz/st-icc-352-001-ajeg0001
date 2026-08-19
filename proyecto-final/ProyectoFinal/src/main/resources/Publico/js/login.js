(() => {
    console.log("ENTRA AL SCRIPT LOGIN")

    function procesarLogin() {

        const loginForm = document.getElementById('loginForm');

        loginForm.addEventListener('submit', (event) => {

            event.preventDefault();

            const usuario = document.getElementById('idUsuario').value;
            const password = document.getElementById('idPassword').value;

            if (!validarDatos(usuario, password)) {
                return;
            }

        });
    }

    function validarDatos(usuario, password) {

        if (usuario == "") {
            alert("El campo usuario es obligatorio");
            return false;
        }
        if (password == "") {
            alert("El campo contraseña es obligatorio");
            return false;
        }

        return true;
    }

    procesarLogin();

})();