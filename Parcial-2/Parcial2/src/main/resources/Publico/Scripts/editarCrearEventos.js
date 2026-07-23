(() => {
    console.log("FORM EDITAR/CREAR");

    const idEvento =  new URLSearchParams(window.location.search).get("id");
    console.log("EVENTO URL" + idEvento);
    const form = document.getElementById("idForm");
    const titulo = document.getElementById("idTituloFormulario");
    const btn = document.getElementById("btn");

    if (idEvento) {
        form.action = "/Eventos/procesar-modificar";
        titulo.textContent = "Editar evento";
        btn.textContent = "Guardar cambios";

        cargarEventoEditar(idEvento);
        
    } 


    async function obtenerEventos (idEvento) {

        try {

            const respuesta = await fetch(`/Api/obtenerEvento/${idEvento}`);

            if (!respuesta.ok) {

                throw new Error(
                    `Error HTTP: ${respuesta.status}`
                );

            }
            console.log(respuesta);

            return await respuesta.json();

        } catch (error) {

            console.error(error);

        }
        
    }

    async function cargarEventoEditar(idEvento) {

        const evento = await obtenerEventos(idEvento);
        console.log("CARGAR " + evento);
        document.getElementById("idEvento").value = evento.id;
        document.getElementById("titulo").value = evento.titulo;
        document.getElementById("descripcion").value = evento.descripcion;
        const fecha = document.getElementById("fecha");

        const fechaUTC = new Date(evento.fecha);

        const anio = fechaUTC.getUTCFullYear();
        const mes = String(fechaUTC.getUTCMonth() + 1).padStart(2, '0'); // Los meses van de 0 a 11
        const dia = String(fechaUTC.getUTCDate()).padStart(2, '0');

        fecha.value = `${anio}-${mes}-${dia}`;

        document.getElementById("hora").value = evento.hora;
        document.getElementById("lugar").value = evento.lugar;
        document.getElementById("cupo").value = evento.cupoMaximo;

        
    }





})();

