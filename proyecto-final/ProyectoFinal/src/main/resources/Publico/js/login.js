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
            const respuesta = await fetch('/api/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json'},
                body: JSON.stringify({usuario,password})
            });


            if (respuesta.status == 401){
                alert("Usuario o contrasena incorrectos");
            }

            if (respuesta.ok) {
                const data = await respuesta.json();

                console.log(data);

                localStorage.setItem("token", data.token);
                localStorage.setItem("id",data.usuario.id);
                localStorage.setItem("usuario", data.usuario.usuario);
                localStorage.setItem("roles", data.usuario.roles);

                //window.location.href = 'formulario.html';

            }
        } catch (e) {

            console.error("Error al conectar con el servidor");
        }
    }

    function validarSesion() {
       const token = localStorage.getItem("token");
       const usuario = localStorage.getItem("usuario");
       const idUsuario = localStorage.getItem("id");

       if (token == null || !usuario || !idUsuario) {
           window.location.href = "login.html";
           return false;
       }

       return true;
    }

    procesarLogin();

})();