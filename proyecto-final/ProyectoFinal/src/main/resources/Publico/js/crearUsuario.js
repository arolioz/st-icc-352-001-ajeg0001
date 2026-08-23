(() => {

    console.log("CREAR USUARIO");

    if (!verificarSesionAdmin()) {
        alert("Debe ser administrador para ver esta pagina");
        window.location.href = "crudFormulario.html";
        return;
    }

    function validarCamposCrear() {
        const usuario = document.getElementById("usuario").value;
        const password = document.getElementById("idPassword").value;

        console.log(usuario);
        console.log(password);


        if (!usuario || !password) {
            alert("Debe completar todos los campos");
            return false;
        }

        return true;
    }

    async function crearUsuario() {

        if (!validarCamposCrear()) {
            return;
        }

        const usuario = document.getElementById("usuario").value;
        const password = document.getElementById("idPassword").value;

        try {

            const respuesta = await fetch("/api/usuario", {
                method: "POST",
                headers: {
                    "Authorization": "Bearer " + localStorage.getItem("token"),
                    "Accept": "application/json",
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ user: usuario, password: password })
            });

            if (!respuesta.ok) {
                throw new Error(`HTTP error! Status: ${respuesta.status}`);
            }

            console.log(respuesta);

            alert("Usuario creado correctamente");

            window.location.href = "crudUsuario.html";

        } catch (error) {

            console.error("Error creando usuario:", error);
            alert("No se pudo crear el usuario");

        }

    }

    function configurarBotones() {

        const formulario = document.getElementById("idFormUsuario");

        if (!formulario) {
            return;
        }

        formulario.addEventListener("submit", (evento) => {

            evento.preventDefault();
            crearUsuario();

        });

        const btnCancelar = document.getElementById("btnCancelar");

        if (btnCancelar) {
            btnCancelar.addEventListener("click", () => {
                window.location.href = "crudUsuario.html";
            });
        }
    }

    configurarBotones();


})();