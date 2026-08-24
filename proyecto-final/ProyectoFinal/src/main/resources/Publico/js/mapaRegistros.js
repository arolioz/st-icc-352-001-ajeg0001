(() => {

    if (!verificarSesionAdmin()) {
        alert("Debe ser administrador para ver esta pagina");
        return;
    }

    console.log("Entrando al Mapa");

    const idMapa = L.map("idMapa").setView([19.425299, -70.712168], 13);
    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {attribution: '&copy; <a href="colaboradoreshttps://www.openstreetmap.org/copyright">OpenStreetMap</a>'}).addTo(idMapa);

    async function cargarFormularios() {

        try {

            const respuesta = await fetch("/api/encuesta", {
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + localStorage.getItem("token"),
                    "Accept": "application/json"
                }
            });

            if (!respuesta.ok) {
                throw new Error(`HTTP error! Status: ${respuesta.status}`);
            }

            const formularios = await respuesta.json();

            console.log("Formularios:", formularios);

            mostrarMapa(formularios);

        } catch (error) {

            console.error("Error cargando formularios:", error);

        }

    }

    function mostrarMapa(formularios) {

        const coordenadas = [];
        formularios.forEach( form => {

           if (form.latitud == null || form.longitud == null) {
               return;
           }

           const coordenada = [form.latitud, form.longitud];

           coordenadas.push(coordenada);

           const ubicacion = L.marker([form.latitud, form.longitud]).addTo(idMapa);

           ubicacion.bindPopup(`<strong>${form.nombre}</strong>
                                <p>Registrado por: ${form.usuarioNombre}</p>`);

        });

        if (coordenadas > 0) {
            idMapa.fitBounds(coordenadas);
        }

    }

    cargarFormularios();

})();