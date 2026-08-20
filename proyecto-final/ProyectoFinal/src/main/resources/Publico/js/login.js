(() => {
    console.log("ENTRA AL SCRIPT LOGIN");

    function procesarLogin() {

        const loginForm = document.getElementById('loginForm');

        loginForm.addEventListener('submit', async (event) => {

            event.preventDefault();

            const usuario = document.getElementById('idUsuario').value;
            const password = document.getElementById('idPassword').value;

            if (!validarDatos(usuario, password)) {
                return;
            }

            await autenticarUsuario(usuario, password);

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

    async function autenticarUsuario(usuario, password) {

         try {
            const data = { token: "123456", usuario: hola};
            const respuesta = await fetch('/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json'},
                body: JSON.stringify({usuario,password})
            });

            if (respuesta.ok) {
                const data = await respuesta.json();

                console.log(data);

                console.log(localStorage.setItem("token", data.token));
                console.log(localStorage.setItem("usuario", data.usuario));

                window.location.href = 'formulario.html';

            }
        } catch (e) {
            console.error("Error al conectar con el servidor");
        }
    }

    procesarLogin();

})();