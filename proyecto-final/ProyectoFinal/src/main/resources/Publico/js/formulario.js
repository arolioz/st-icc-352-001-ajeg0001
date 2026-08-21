(() => {
    console.log("ENTRA AL SCRIPT FORMULARIO");

    if (!validarSesion()) {
        window.location.href = "login.html";
        return;
    }

    const btnNivelEscolar = document.getElementById("btnNivelEscolar");
    const opciones = document.querySelectorAll(".dropdown-item");
    const idNivelEscolar = document.getElementById("idNivelEscolar");

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

            if (!nombre || !sector || !nivelEscolar) {
                alert("Todos los campos son obligatorios");
                return;
            }

            guardarFormulario(nombre, sector, nivelEscolar);

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
            usuario: localStorage.getItem("usuario")
        };

        formularios.push(nuevoForm);
        localStorage.setItem("formularios", JSON.stringify(formularios));

        //PRUEBA
        console.log(JSON.parse(localStorage.getItem("formularios")));

        alert("Formulario guardado correctamente");

    }

    configurarDropdown();
    procesarFormulario();

})();