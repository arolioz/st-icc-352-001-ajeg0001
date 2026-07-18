(() => {
    console.log("FORM EDITAR/CREAR");

    const idEvento =  new URLSearchParams(window.location.search).get("id");
    const form = document.getElementById("idForm");
    const titulo = document.getElementById("idTituloFormulario");
    const btn = document.getElementById("btn");

    if (idEvento) {
        form.action = "/Eventos/procesar-modificar";
        titulo.textContent = "Editar evento";
        btn.textContent = "Guardar cambios";

        cargarEventoEditar();
        
    } 


    async function obtenerEventos (idEvento) {

        try {

            const respuesta = await fetch(`/Api/obtenerEvento/${idEvento}`);

            if (!respuesta.ok) {

                throw new Error(
                    `Error HTTP: ${respuesta.status}`
                );

            }

            return await respuesta.json();

        } catch (error) {

            console.error(error);

        }
        
    }

    async function cargarEventoEditar(idEvento) {

        const evento = await obtenerEventos(idEvento);

        document.getElementById("idEvento").value = evento.id;
        document.getElementById("titulo").value = evento.titulo;
        document.getElementById("descripcion").value = evento.descripcion;
        document.getElementById("fecha").value = new Date(evento.fecha);
        document.getElementById("hora").value = evento.hora;
        document.getElementById("lugar").value = evento.lugar;
        document.getElementById("cupo").value = evento.cupoMaximo;

        
    }





})();

