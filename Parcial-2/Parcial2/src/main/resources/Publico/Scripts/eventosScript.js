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
            console.log(data);

            mostrarEventos(data);
            
        } catch (error) {
            console.error('Fetch error:', error);
        }


    }

    async function mostrarEventos(eventos) {

        const listaEventos = document.getElementById('listaEventos');

        listaEventos.innerHTML = '';

        eventos.forEach(evento => {
            const card = document.createElement("div");
            card.className = "evento-card";

            const cardHeader = document.createElement("div");
            cardHeader.className = "evento-header";

            const titulo = document.createElement("h2");
            titulo.className = "text-xl font-bold text-amber-50 text-center";
            titulo.textContent = evento.titulo;

            cardHeader.appendChild(titulo);

            const eventBody = document.createElement("div");
            eventBody.className = "event-body";

            const totalInscritos = document.createElement("p");
            const iconoPersona = document.createElement("i");
            iconoPersona.className = "bi bi-person-check";

            totalInscritos.appendChild(iconoPersona);
            totalInscritos.textContent = evento.cupoMaximo;

            const hora = document.createElement("p");
            const iconoReloj = document.createElement("i");
            iconoReloj.className = "bi bi-clock-fill";

            hora.appendChild(iconoReloj);
            hora.textContent = evento.hora;
            //hora.appendChild(evento.hora);

            const fecha = document.createElement("p");
            const iconoCalendario = document.createElement("i");
            iconoCalendario.className = "bi bi-calendar-event";

            fecha.appendChild(iconoCalendario);
            fecha.textContent = evento.fecha;
            //fecha.appendChild(evento.fecha);

            const btnVerDetalles = document.createElement("a");
            btnVerDetalles.className = "boton1 color4 text-center block";
            btnVerDetalles.textContent = "Ver Detalles";
            btnVerDetalles.setAttribute("href", "detalleEvento.html?id=" + evento.id);
            
            eventBody.appendChild(totalInscritos);
            eventBody.appendChild(hora);
            eventBody.appendChild(fecha);
            eventBody.appendChild(btnVerDetalles);

            card.appendChild(cardHeader);
            card.appendChild(eventBody);

            listaEventos.appendChild(card);

            
        });
    }

        


    
    cargarEventos();

})