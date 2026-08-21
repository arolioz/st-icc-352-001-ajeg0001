(() => {
    console.log("ENTRA AL SCRIPT FORMULARIO");

    if (!validarSesion()) {
        window.location.href = "login.html";
        return;
    }

    const btnNivelEscolar = document.getElementById("btnNivelEscolar");
    const opciones = document.querySelectorAll(".dropdown-item");
    const idNivelEscolar = document.getElementById("idNivelEscolar");

    let ubicacionActual = {
        latitud: null,
        longitud: null
    };

    function configurarDropdown() {

        opciones.forEach(opcion => {

            opcion.addEventListener("click", function (event) {

                event.preventDefault();

                const nivel = this.dataset.value;
                btnNivelEscolar.textContent = nivel;
                idNivelEscolar.value = nivel;

            });
        });
    }

    function procesarFormulario() {

        const idForm = document.getElementById('idForm');

        idForm.addEventListener('submit', async (event) => {

            event.preventDefault();

            const nombre = document.getElementById("idNombre").value;
            const sector = document.getElementById('idSector').value;
            const nivelEscolar = document.getElementById('idNivelEscolar').value;
            document.getElementById("idUsuario").value = localStorage.getItem("usuario");

            if (!nombre || !sector || !nivelEscolar) {
                alert("Todos los campos son obligatorios");
                return;
            }

            try {

                await obtenerubicacion();

                guardarFormulario(nombre, sector, nivelEscolar);

            } catch (error) {
                alert("No se pudo obtener la ubicación.");
            }

        });
    }

    function guardarFormulario(nombre, sector, nivelEscolar) {

        const formularios = JSON.parse(localStorage.getItem("formularios")) || [];

        const nuevoForm = {
            uuid: crypto.randomUUID(),
            nombre: nombre,
            sector: sector,
            nivelEscolar: nivelEscolar,
            usuarioId: localStorage.getItem("id"),
            usuario: localStorage.getItem("usuario"),
            latitud: ubicacionActual.latitud,
            longitud: ubicacionActual.longitud
        };

        formularios.push(nuevoForm);
        localStorage.setItem("formularios", JSON.stringify(formularios));

        console.log(JSON.parse(localStorage.getItem("formularios")));

        alert("Formulario guardado correctamente");

    }

    function obtenerubicacion() {

        return new Promise((resolve, reject) => {

            if (!navigator.geolocation) {
                console.log("El navegador no soporta geolocalización.");
                return;
            }

            navigator.geolocation.getCurrentPosition(
                (posicion) => {
                    ubicacionActual.latitud = posicion.coords.latitude;
                    ubicacionActual.longitud = posicion.coords.longitude;

                    resolve();
                },
                (error) => {

                    console.error("Error obteniendo ubicación:", error);
                    reject(error);
                }

            );



        });

    }

    configurarDropdown();
    procesarFormulario();
})();