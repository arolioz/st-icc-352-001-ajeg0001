(() => {


    console.log("Cargando eventos...");

    async function cargarEventos() {

        try {

            const respuesta = await fetch('/Api/listaEventosAdmin');
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


            await cargarEventos();


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


            await cargarEventos();


        }catch(error){

            console.error(error);

        }

    }

    
    async function eliminarEvento(id){


        try{

            const respuesta = await fetch(`/Eventos/eliminar/${id}`,{
                method:"POST"
            });


            if(!respuesta.ok){
                throw new Error("Error eliminando evento");
            }


            await cargarEventos();


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


    async function crearBotones(evento) {

        const esAdmin = await usuarioEsAdmin();

        const container = document.createElement("div");
        container.className = "flex gap-2 justify-center";


        // VER DETALLE
        const btnDetalle = document.createElement("a");
        btnDetalle.className = "btn2 color5 text-center block flex-1 !w-auto";
        btnDetalle.textContent = "Ver";
        btnDetalle.href = `estadisticasEvento.html?id=${evento.id}`;

        // EDITAR
        const btnEditar = document.createElement("a");
        btnEditar.className = "btn2 color4 text-center block flex-1 !w-auto";
        btnEditar.textContent = "Editar";
        btnEditar.href = `crearEventos.html?id=${evento.id}`;

        // PUBLICAR / DESPUBLICAR
        const btnEstado = document.createElement("button");
        btnEstado.className = "btn2 color3 text-center block flex-1 !w-auto";

        if (eventoEstaPublicado(evento)) {

            btnEstado.textContent = "Despublicar";

        } else {

            btnEstado.textContent = "Publicar";

        }

        btnEstado.addEventListener("click", () => {
            cambiarEstadoEvento(evento.id);
        });


        // CANCELAR
        const btnCancelar = document.createElement("button");
        btnCancelar.className = "btn2 color2 text-center block flex-1 !w-auto";
        btnCancelar.textContent = "Cancelar";


        btnCancelar.addEventListener("click", () => {
            if (!confirm("¿Está seguro de que desea cancelar este evento?")) {
                return;
            }
            cancelarEvento(evento.id);

            alert("Evento cancelado exitosamente");
        });

        const btnEliminar = document.createElement("button");
        btnEliminar.className = "btn2 color1 text-center block flex-1 !w-auto"
        btnEliminar.textContent = "Eliminar";

        btnEliminar.addEventListener("click", () => {
            if (!confirm("¿Está seguro de que desea eliminar este evento?")) {
                return;
            }

            eliminarEvento(evento.id);

            alert("Evento eliminado exitosamente");
        });

        if (eventoEstaEliminado(evento)) {
            return container;

        } else if (eventoEstaCancelado(evento)) {
            if (esAdmin) {
                container.appendChild(btnEliminar);

            }

        } else {
            container.appendChild(btnDetalle);
            container.appendChild(btnEditar);
            container.appendChild(btnEstado);
            container.appendChild(btnCancelar);

            if (esAdmin) {
                container.appendChild(btnEliminar);
            }

        }


        return container;

    }


    async function mostrarEventos(eventos) {


        const tablaEventos = document.getElementById("idTablaEventos");
        tablaEventos.innerHTML = "";


       for (const evento of eventos) {

            const idOrganizador = await obtenerOrganizadorEvento(evento.idOrganizador);
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

            const fechaUTC = new Date(evento.fecha);

            const anio = fechaUTC.getUTCFullYear();
            const mes = String(fechaUTC.getUTCMonth() + 1).padStart(2, '0'); // Los meses van de 0 a 11
            const dia = String(fechaUTC.getUTCDate()).padStart(2, '0');

            fecha.append(`${dia}/${mes}/${anio}`);

            
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
            const botones =  await crearBotones(evento);
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