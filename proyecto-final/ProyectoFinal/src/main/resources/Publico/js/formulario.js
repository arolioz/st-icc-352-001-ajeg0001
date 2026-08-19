(() => {
    console.log("ENTRA AL SCRIPT FORMULARIO");

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

    configurarDropdown();

})();