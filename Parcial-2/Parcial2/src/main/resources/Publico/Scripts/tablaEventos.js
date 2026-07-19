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

    async function cambiarEstadoEvento(id) {

        try{

            const respuesta = await fetch(`/Eventos/cambiarEstado/${id}`,{
                method:"POST"
            });


            if(!respuesta.ok){
                throw new Error("Error cambiando estado del evento");
            }


            cargarEventos();


        }catch(error){

            console.error(error);

        }
    }

    async function cancelarEvento(id){


        try{

            const respuesta = await fetch(`/Eventos/cancelar/${id}`,{
                method:"POST"
            });


            if(!respuesta.ok){
                throw new Error("Error cancelando evento");
            }


            cargarEventos();


        }catch(error){

            console.error(error);

        }

    }

    function eventoEstaEliminado(evento) {

        return evento.eliminado;
    }

    function eventoEstaCancelado(evento) {
        return evento.activo === false;
    }

    function eventoEstaPublicado(evento) {
        return evento.publicado
    }


    function crearBotones(evento) {

        const container = document.createElement("div");
        container.className = "flex gap-2 justify-center";


        // VER DETALLE
        const btnDetalle = document.createElement("a");
        btnDetalle.className = "boton1 color4 text-center block flex-1 !w-auto";
        btnDetalle.textContent = "Ver";
        btnDetalle.href = `detalleEvento.html?id=${evento.id}`;

        // EDITAR
        const btnEditar = document.createElement("a");
        btnEditar.className = "boton1 color2 text-center block flex-1 !w-auto";
        btnEditar.textContent = "Editar";
        btnEditar.href = `crearEventos.html?id=${evento.id}`;

        // PUBLICAR / DESPUBLICAR
        const btnEstado = document.createElement("button");
        btnEstado.className = "boton1 color3 text-center block flex-1 !w-auto";

        if(eventoEstaPublicado(evento)) {

            btnEstado.textContent = "Despublicar";

        } else {

            btnEstado.textContent = "Publicar";

        }

        btnEstado.addEventListener("click", () => {
            cambiarEstadoEvento(evento.id);
        });


        // CANCELAR
        const btnCancelar = document.createElement("button");
        btnCancelar.className = "btn2 text-center block flex-1 !w-auto";
        btnCancelar.textContent = "Cancelar";


        btnCancelar.addEventListener("click", () => {
            cancelarEvento(evento.id);
        });


        container.appendChild(btnDetalle);
        container.appendChild(btnEditar);
        container.appendChild(btnEstado);
        container.appendChild(btnCancelar);

        return container;

    }


    async function mostrarEventos(eventos) {


        const tablaEventos = document.getElementById("idTablaEventos");
        tablaEventos.innerHTML = "";


       for (const evento of eventos) {

        console.log("Evento:", evento.titulo);
        console.log("Activo:", evento.activo);
        console.log("Publicado:", evento.publicado);
        console.log("Eliminado:", evento.eliminado);

            const idOrganizador = await obtenerOrganizadorEvento(evento.idOrganizador);
            //const cantidadInscritos = await obtenerCantidadInscritos(evento.id);

            const fila = document.createElement("tr");
            fila.className = "border-b text-center border-amber-950";

            //TITULO
            const titulo = document.createElement("td");
            titulo.className = "p-3 font-semibold";
            titulo.textContent = evento.titulo;

            //ORGANIZADOR
            const organizador = document.createElement("td");
            organizador.className = "p-3 font-semibold";
            organizador.textContent = idOrganizador;

            //FECHA
            const fecha = document.createElement("td");
            fecha.className = "p-3 font-semibold";
            fecha.textContent = new Date(evento.fecha).toLocaleDateString();
            
            //HORA
            const hora = document.createElement("td");
            hora.className = "p-3 font-semibold";
            hora.textContent = evento.hora;


            //LUGAR
            const lugar = document.createElement("td");
            lugar.className = "p-3 font-semibold";
            lugar.textContent = evento.lugar;


            //ESTADO 
            const estado = document.createElement("td");
            estado.className = "p-3 font-semibold";

            if (eventoEstaEliminado(evento)) {

                estado.textContent = "Eliminado";
                estado.className = "text-red-900";


            } else if (eventoEstaCancelado(evento)) {

                estado.textContent = "Cancelado";
                estado.className = "text-red-600";


            } else if (eventoEstaPublicado(evento)) {

                estado.textContent = "Publicado";
                estado.className = "text-green-700";


            } else {

                estado.textContent = "No publicado";
                estado.className = "text-yellow-700";

            }


            const acciones = document.createElement("td");
            acciones.className = "p-3 font-semibold";
            const botones =  crearBotones(evento);
            acciones.appendChild(botones);

           fila.appendChild(titulo);
           fila.appendChild(organizador);
           fila.appendChild(fecha);
           fila.appendChild(hora);
           fila.appendChild(lugar);
           fila.appendChild(estado);
           fila.appendChild(acciones);

            tablaEventos.appendChild(fila);



        }



    }


    cargarEventos();



})();