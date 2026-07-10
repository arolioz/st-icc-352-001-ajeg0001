(() => {

    const idProducto = location.pathname.split('/').pop();
    let esAdmin = false;
    let comentariosSocket;
    let heartbeat;
    let retardoReconexion = 1000;
    const retardoMaximo = 30000;

    console.log(idProducto);


    async function actualizarComentarios(){
        try {
            const respuesta = await fetch(`/crud-producto/${idProducto}/comentarios`);

            if (!respuesta.ok) {
                throw new Error(`HTTP error! Status: ${respuesta.status}`);
            }

            const data = await respuesta.json();
            console.log(data);

            mostrarComentarios(data);
        } catch (error) {
            console.error('Fetch error:', error);
        }
    }

    async function usuarioEsAdmin() {
        try {
            const respuesta = await fetch(`/administracion/esAdmin`);

            if (!respuesta.ok) {
                throw new Error(`HTTP error! Status: ${respuesta.status}`);
            }

            esAdmin = await respuesta.json();
            console.log(esAdmin);

        } catch (error){
            console.error('Fetch error: ', error);
        }
    }


    async function mostrarComentarios(comentarios) {
        const container = document.getElementById("listaComentarios");
        container.innerHTML = "";

        comentarios.forEach((c) => {

            if (c.habilitado) {
                const container2 = document.createElement("div");
                container2.className = "container mb-3"

                const tituloDiv = document.createElement("div");
                tituloDiv.id = "comentario-" + c.id;
                tituloDiv.className = "px-4 text-left";

                const usuario = document.createElement("h4");
                usuario.className = "bold";
                usuario.textContent = c.usuario;

                tituloDiv.appendChild(usuario);

                const card = document.createElement("div");
                card.className = "card";
                const cardBody = document.createElement("div");
                cardBody.className = "card-body mb-4";

                const contenido = document.createElement("p");
                contenido.textContent = c.comentario;

                cardBody.appendChild(contenido);

                if (esAdmin) {
                    const botonEliminar = document.createElement("a");
                    botonEliminar.className ="btn btn-danger";
                    botonEliminar.textContent = "Eliminar";

                    botonEliminar.setAttribute("href", `/administracion/eliminar-comentario/${c.id}`);

                    cardBody.appendChild(botonEliminar);
                }

                card.appendChild(cardBody);
                container2.appendChild(tituloDiv);
                container2.appendChild(card);
                container.appendChild(container2);

            }

        });

    }

    function conectarComentarios() {
        const esquema = location.protocol === "https:" ? "wss" : "ws";
        const url = `${esquema}://${location.host}/comentarios`;
        comentariosSocket = new WebSocket(url);

        // open: handshake completado (HTTP 101 Switching Protocols).
        comentariosSocket.addEventListener("open", () => {

            retardoReconexion = 1000;    // reiniciamos el backoff al conectar
            iniciarHeartbeat();
        });

        comentariosSocket.addEventListener("message", (evento) => {
            console.log("RECIBIDO DEL SERVIDOR:", evento.data);
            if (evento.data == "ActualizarComentarios"){
                actualizarComentarios();
            }
        });

        // close: la conexión se cerró; programamos reintento con backoff.
        comentariosSocket.addEventListener("close", () => {
            clearInterval(heartbeat);
            setTimeout(conectarComentarios, retardoReconexion);
            retardoReconexion = Math.min(retardoReconexion * 1.5, retardoMaximo);
        });

        // error: por seguridad el navegador no da detalles; cerramos para reconectar.
        comentariosSocket.addEventListener("error", () => comentariosSocket.close());
    }
    // Heartbeat: enviamos un "latido" cada 30 s para que proxies/balanceadores
    // no cierren la conexión por inactividad.
    function iniciarHeartbeat() {
        heartbeat = setInterval(() => {
            if (comentariosSocket.readyState === WebSocket.OPEN) {
                comentariosSocket.send(JSON.stringify({ type: "ping" }));
            }
        }, 30000);
    }



    async function iniciar() {
        await usuarioEsAdmin();
        await actualizarComentarios();
        conectarComentarios();
    }

    iniciar();





})();