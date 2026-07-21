(() => {
    console.log("Cargando eventos...");
    async function cargarEventos() {

        try {

            const respuesta = await fetch('/Api/listaEventos');
            console.log(respuesta);
            
            if (!respuesta.ok) {
                throw new Error(`HTTP error! Status: ${respuesta.status}`);
            }

            const data = await respuesta.json();

            const eventosDisponibles = data.filter(evento =>
             evento.publicado === true
            );

            console.log(eventosDisponibles);

            mostrarEventos(eventosDisponibles);
            
        } catch (error) {
            console.error('Fetch error:', error);
        }

    }

    async function mostrarEventos(eventos) {

        const listaEventos = document.getElementById('listaEventos');

        listaEventos.innerHTML = '';

        if (eventos.length === 0) {
            listaEventos.innerHTML = `<h3 class="text-xl text-amber-950 text-center justify-center">
                                        No hay eventos disponibles en este momento.
                                     </h3>`;
            return;
        }

        for (const evento of eventos) {
            const idOrganizador = await obtenerOrganizadorEvento(evento.idOrganizador);
            const cantidadInscritos = await obtenerCantidadInscritos(evento.id);

            const card = document.createElement("div");
            card.className = "event-card";

            const cardHeader = document.createElement("div");
            cardHeader.className = "event-header";

            const titulo = document.createElement("h2");
            titulo.className = "text-xl font-bold text-amber-50 text-center";
            titulo.textContent = evento.titulo;

            cardHeader.appendChild(titulo);

            const eventBody = document.createElement("div");
            eventBody.className = "event-body";

            //ORGANIZADOR
            const organizador = document.createElement("p");
            organizador.className = "text-label";
            const iconOrganizador = document.createElement("i");
            iconOrganizador.className = "bi bi-person-fill";
            organizador.appendChild(iconOrganizador);
            organizador.append("Organizador: " + idOrganizador);

            // FECHA
            const fecha = document.createElement("p");
            fecha.className = "text-label";

            const iconoCalendario = document.createElement("i");
            iconoCalendario.className = "bi bi-calendar-event";

            fecha.appendChild(iconoCalendario);
            fecha.appendChild(iconoCalendario);
            const fechaUTC = new Date(evento.fecha);

            const anio = fechaUTC.getUTCFullYear();
            const mes = String(fechaUTC.getUTCMonth() + 1).padStart(2, '0'); // Los meses van de 0 a 11
            const dia = String(fechaUTC.getUTCDate()).padStart(2, '0');

            fecha.appendChild(iconoCalendario);
            fecha.append(` Fecha: ${dia}/${mes}/${anio}`);

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
            totalInscritos.append(" Cupo: " + cantidadInscritos + " / " +evento.cupoMaximo);

            // BOTON
            const btnVerDetalles = document.createElement("a");
            btnVerDetalles.className = "boton1 color4 text-center block";
            btnVerDetalles.textContent = "Ver Detalles";
            btnVerDetalles.setAttribute("href", "detalleEvento.html?id=" + evento.id);

            eventBody.appendChild(organizador);
            eventBody.appendChild(fecha);
            eventBody.appendChild(hora);
            eventBody.appendChild(totalInscritos);
            eventBody.appendChild(btnVerDetalles);
            card.appendChild(cardHeader);
            card.appendChild(eventBody);
            listaEventos.appendChild(card);

        }
    }

    cargarEventos();

})();