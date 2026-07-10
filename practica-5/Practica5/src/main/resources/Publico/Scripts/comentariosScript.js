(() => {

    const idProducto = location.pathname.split('/').pop();

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

                card.appendChild(cardBody);
                container2.appendChild(tituloDiv);
                container2.appendChild(card);
                container.appendChild(container2);

            }

        });

    }



    async function iniciar() {

        await actualizarComentarios();
    }

    iniciar();





})();