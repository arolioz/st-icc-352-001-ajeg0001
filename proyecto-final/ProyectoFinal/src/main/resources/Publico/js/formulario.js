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

    let foto = null;
    const webcamElement = document.getElementById('webcam');
    const canvasElement = document.getElementById('canvas');
    const idFoto = document.getElementById("idFoto");
    const btnFoto = document.getElementById("btnTomarFoto");

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
        const btnCancelar = document.getElementById("btnCancelar");
        const accion = localStorage.getItem("accionForm");

        console.log("Acción:", accion);

        btnCancelar.addEventListener("click", () => {
            window.location.href = "crudFormulario.html";
        });

        idForm.addEventListener('submit', async (event) => {

            event.preventDefault();

            console.log("SE PRESIONÓ GUARDAR");

            const nombre = document.getElementById("idNombre").value;
            const sector = document.getElementById('idSector').value;
            const nivelEscolar = document.getElementById('idNivelEscolar').value;
            document.getElementById("idUsuario").value = localStorage.getItem("usuario");

            if (!nombre || !sector || !nivelEscolar) {
                alert("Todos los campos son obligatorios");
                return;
            }

            if (accion === "editar") {
                actualizarFormulario(nombre, sector, nivelEscolar);
                return;
            }

            if (!foto) {
                alert("Debe tomar una foto antes de guardar");
                return;
            }

            try {

                await obtenerubicacion();

                await guardarFormulario(nombre, sector, nivelEscolar);
                window.location.href = "crudFormulario.html";

                reiniciarCamara();

            } catch (error) {
                alert("No se pudo obtener la ubicación.");
            }

        });
    }

    async function guardarFormulario(nombre, sector, nivelEscolar) {

        const formularios = JSON.parse(localStorage.getItem("formularios")) || [];
        const uuid = crypto.randomUUID();

        const nuevoForm = {
            uuid: uuid,
            nombre: nombre,
            sector: sector,
            nivelEscolar: nivelEscolar,
            usuarioId: localStorage.getItem("id"),
            usuario: localStorage.getItem("usuario"),
            latitud: ubicacionActual.latitud,
            longitud: ubicacionActual.longitud,
            //foto: foto,
            id : null
        };

        formularios.push(nuevoForm);
        localStorage.setItem("formularios", JSON.stringify(formularios));

        await guardarFoto(uuid, foto);

        console.log(JSON.parse(localStorage.getItem("formularios")));

        alert("Formulario guardado correctamente");

        document.getElementById('idForm').reset();
        btnNivelEscolar.textContent = "Seleccione";
        document.getElementById("idFoto").style.display = "none";
        foto = null;

    }

    function obtenerubicacion() {

        return new Promise((resolve, reject) => {

            if (!navigator.geolocation) {
                console.log("El navegador no soporta geolocalización.");
                reject("Geolocalización no soportada");
                return;
            }

            navigator.geolocation.getCurrentPosition(
                (posicion) => {
                    ubicacionActual.latitud = posicion.coords.latitude;
                    ubicacionActual.longitud = posicion.coords.longitude;

                    resolve();
                },
                (error) => {

                    console.error("Error obteniendo ubicación:", error.message);
                    reject(error);
                }

            );



        });

    }

    function configurarCamara() {

        const webcam = new Webcam(webcamElement, 'user', canvasElement);

        webcam.start()
            .then(result =>{
                console.log("webcam started");
                btnFoto.disabled = false;
            })
            .catch(error => {
                console.log(error);
                alert("Error al acceder a la camara");
                btnFoto.disabled = true;
            });


        btnFoto.addEventListener("click", () => {

            if (!foto) {
                foto = webcam.snap();
                idFoto.src = foto;
                webcamElement.style.display = "none";
                idFoto.style.display = "block";
                btnFoto.textContent = "Reintentar";

            } else {
                foto = null;
                idFoto.src = "";
                idFoto.style.display = "none";
                webcamElement.style.display = "block";
                btnFoto.textContent = "Tomar foto";
            }
        });

        window.addEventListener("beforeunload", () => {
            webcam.stop();
        });
    }

    function reiniciarCamara() {

        foto = null;
        idFoto.src = "";
        idFoto.style.display = "none";
        webcamElement.style.display = "block";
        btnFoto.textContent = "Tomar foto";
    }

    async function cargarDatosFormulario(formulario) {

        document.getElementById("idNombre").value = formulario.nombre;
        document.getElementById("idSector").value = formulario.sector;
        document.getElementById("idNivelEscolar").value = formulario.nivelEscolar;
        document.getElementById("btnNivelEscolar").textContent = formulario.nivelEscolar;
        document.getElementById("idUsuario").value = formulario.usuario;

        const foto = await obtenerFoto(formulario.uuid);
        document.getElementById("idFoto").src = foto;
        document.getElementById("idFoto").style.display = "block";
    }

    async function determinarAccionForm() {

        const accion = localStorage.getItem("accionForm");

        if (!accion) {
            return;
        }

        const uuid = localStorage.getItem("form");

        if (!uuid) {
            alert("No hay formularios disponibles");
            return;
        }

        const formularios = JSON.parse(localStorage.getItem("formularios")) || [];

        const formulario = formularios.find(form => form.uuid == uuid);

        if (!formulario) {
            alert("Formulario no encontrado");
            return;
        }

        await cargarDatosFormulario(formulario);

        switch (accion) {
            case "visualizar":
                visualizarForm();
                localStorage.removeItem("accionForm");
                localStorage.removeItem("form");
                break;

            case "editar":
                editarForm();
                break;
        }

    }

    function visualizarForm() {

        document.getElementById("idNombre").readOnly = true;
        document.getElementById("idSector").readOnly = true;
        document.getElementById("idNivelEscolar").disabled = true;
        document.getElementById("btnNivelEscolar").disabled = true;
        document.getElementById("idUsuario").readOnly = true;
        document.getElementById("webcam").style.display = "none";
        document.getElementById("btnTomarFoto").style.display = "none";
        document.getElementById("btnEnviar").style.display = "none";
    }

    function editarForm() {

        document.getElementById("idNombre").readOnly = false;
        document.getElementById("idSector").readOnly = false;
        document.getElementById("idNivelEscolar").disabled = false;
        document.getElementById("btnNivelEscolar").disabled = false;
        document.getElementById("idUsuario").readOnly = true;

        document.getElementById("idFoto").style.display = "block";
        document.getElementById("webcam").style.display = "none";
        document.getElementById("btnTomarFoto").style.display = "none";

        document.getElementById("btnEnviar").style.display = "block";
        document.getElementById("btnEnviar").textContent = "Guardar cambios";
    }

    function actualizarFormulario(nombre, sector, nivelEscolar) {

        const uuid = localStorage.getItem("form");
        const formularios = JSON.parse(localStorage.getItem("formularios")) || [];
        const formulario = formularios.find(form => form.uuid === uuid);

        if (!formulario) {
            alert("Formulario no encontrado");
            return;
        }

        formulario.nombre = nombre;
        formulario.sector = sector;
        formulario.nivelEscolar = nivelEscolar;

        localStorage.setItem("formularios", JSON.stringify(formularios));

        localStorage.removeItem("accionForm");
        localStorage.removeItem("form");

        alert("Formulario actualizado correctamente");

        window.location.href = "crudFormulario.html";
    }

    function iniciarFormulario() {
        configurarDropdown();
        procesarFormulario();

        const accion = localStorage.getItem("accionForm");

        if (accion) {
            determinarAccionForm ();
        } else {
            configurarCamara();
        }
    }

    iniciarFormulario();

})();