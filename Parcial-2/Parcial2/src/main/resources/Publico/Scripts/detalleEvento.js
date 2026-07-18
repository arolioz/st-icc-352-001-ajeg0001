(() => {

    console.log("Cargando detalles...");
    //const idEvento = location.pathname.split("/").pop();
    const idEvento = new URLSearchParams(window.location.search).get('id');

    async function cargarDetalleEvento() {

        try {

            const respuesta = await fetch('/Api/listaEventos');
            console.log(respuesta);

            if (!respuesta.ok) {
                throw new Error(`HTTP error! Status: ${respuesta.status}`);
            }

            const data = await respuesta.json();
            const evento = data.find(e => e.id == idEvento);

            console.log(evento);

            mostrarDetallesEventos(evento);

        } catch (error) {
            console.error('Fetch error:', error);
        }

    }

    async function inscribirEvento(idEvento) {

        try {

            const respuesta = await fetch(`/Eventos/inscribir/${idEvento}`, {
                method: "POST"
            });

            if (!respuesta.ok) {
                throw new Error(`HTTP error! Status: ${respuesta.status}`);
            }

            cargarDetalleEvento();

        } catch (error) {
            console.error('Fetch error:', error);
        }
    }

    async function editarEvento(idEvento) {
        try {
            const respuesta = await fetch(`/Eventos/procesar-modificar/${idEvento}`, {
                method: "POST"
            });

            if (!respuesta.ok) {
                throw new Error(`HTTP error! Status: ${respuesta.status}`);
            }

            //cargarDetalleEvento();

        } catch (error) {
            console.error('Fetch error:', error);
        }
    }

    async function crearBotones(evento) {

        const container = document.createElement("div");
        container.className = "flex gap-3"

        const esAdmin = await usuarioEsAdmin();
        const esOrganizador = await usuarioEsOrganizador();

        if (esAdmin || esOrganizador) {

            const btnEditar = document.createElement("button");
            btnEditar.className = "boton1 color3 text-center block flex-1";
            btnEditar.textContent = "Editar";
            btnEditar.href = `editarEvento.html?id=${evento.id}`;

            //btnEditar.setAttribute("href", `/Eventos/procesar-modificar/${evento.id}`);
            //btnEditar.setAttribute("href", `/Eventos/procesar-modificar/${evento.id}`);


        }
        container.appendChild(btnEditar);
        
    }

   

    async function mostrarDetallesEventos(evento) {

        const organizadorEvento = await obtenerOrganizadorEvento(evento.idOrganizador);
        const cantidadInscritos = await obtenerCantidadInscritos(evento.id);
        const detalleEvento = document.getElementById('detalleEvento');

        detalleEvento.innerHTML = '';

            const card = document.createElement("div");
            card.className = "card";

            const cardHeader = document.createElement("div");
            cardHeader.className = "card-header relative";

            const titulo = document.createElement("h2");
            titulo.className = "text-xl font-bold text-amber-50 text-center";
            titulo.textContent = evento.titulo;

            cardHeader.innerHTML = `<a href="listaEventos.html" class="icon-x">
                                    <i class="bi bi-x-lg"></i>
                                    </a> `;

            cardHeader.appendChild(titulo);

            const eventBody = document.createElement("div");
            eventBody.className = " w-full max-w-md min-h-[450px] rounded-b-xl bg-white p-8 drop-shadow-amber-950 flex flex-col justify-between";

            //ORGANIZADOR
            const organizador = document.createElement("p");
            organizador.className = "text-label";
            const iconOrganizador = document.createElement("i");
            iconOrganizador.className = "bi bi-person-fill";
            organizador.appendChild(iconOrganizador);
            organizador.append("Organizador: " + organizadorEvento);

            // FECHA
            const fecha = document.createElement("p");
            fecha.className = "text-label";

            const iconoCalendario = document.createElement("i");
            iconoCalendario.className = "bi bi-calendar-event";

            fecha.appendChild(iconoCalendario);
            fecha.append(" Fecha: " + new Date(evento.fecha).toLocaleDateString());

            //HORA
            const hora = document.createElement("p");
            hora.className = "text-label";

            const iconoReloj = document.createElement("i");
            iconoReloj.className = "bi bi-clock-fill";

            hora.appendChild(iconoReloj);
            hora.append(" Hora: " + evento.hora);
            console.log(evento.hora);

            // CUPO MAXIMO
            const totalInscritos = document.createElement("p");
            totalInscritos.className = "text-label";

            const iconoPersona = document.createElement("i");
            iconoPersona.className = "bi bi-person-badge";

            totalInscritos.appendChild(iconoPersona);
            totalInscritos.append(" Cupo: " + cantidadInscritos + " / " + evento.cupoMaximo);

            // DESCRIPCION
            const containerDescripcion = document.createElement("div");
            containerDescripcion.className = "flex flex-col gap-2";
            
            const labelDescripcion = document.createElement("label");
            labelDescripcion.className = "text-label";
            labelDescripcion.textContent = "Descripción";

            const descripcion = document.createElement("div");
            descripcion.className = "w-full rounded-md border border-gray-300 px-3 py-2 bg-gray-100 break-words";
            descripcion.textContent = evento.descripcion;

            // BOTON
            const btnInscribir = document.createElement("button");
            btnInscribir.className = "boton1 color4 text-center block";
            btnInscribir.textContent = "Inscribirse";
            btnInscribir.addEventListener("click", async () => {

            inscribirEvento(evento.id)
        
            });

            //btnInscribir.setAttribute("href", `/Eventos/inscribir/${evento.id}`);

            eventBody.appendChild(organizador);
            eventBody.appendChild(fecha);
            eventBody.appendChild(hora);
            eventBody.appendChild(totalInscritos);
            eventBody.appendChild(containerDescripcion);
            containerDescripcion.appendChild(labelDescripcion);
            containerDescripcion.appendChild(descripcion);
            eventBody.appendChild(btnInscribir);
            card.appendChild(cardHeader);
            card.appendChild(eventBody);
            detalleEvento.appendChild(card);

        }

    cargarDetalleEvento();

})();