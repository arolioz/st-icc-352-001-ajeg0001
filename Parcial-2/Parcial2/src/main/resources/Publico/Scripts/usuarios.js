(() => {


    console.log("Cargando usuarios...");



    async function cargarUsuarios() {

        try {


            const respuesta = await fetch("/Usuarios/Listar");


            if (!respuesta.ok) {

                throw new Error(`HTTP error! Status: ${respuesta.status}`);

            }

            const usuarios = await respuesta.json();

            console.log(usuarios);

            mostrarUsuarios(usuarios);

        } catch(error) {

            console.error("Error cargando usuarios:", error);

        }

    }

    function usuarioEstaBloqueado(usuario) {

        if (usuario.listaRoles.includes("ROLE_BLOQUEADO")) {
            return true;  
        } else {
            return false;
        }
    }


    function crearBotones(usuario) {

        const boton = document.createElement("button");
        boton.className = "boton1 color4"

        if (usuarioEsOrganizador(usuario)) {
            boton.textContent = "Quitar organizador";
        } else {
            boton.textContent = "Hacer organizador";
        }

        return boton;

    }


    function mostrarUsuarios(usuarios) {


        const tablaUsuario = document.getElementById("idTablaUsuarios");
        tablaUsuario.innerHTML = "";


        usuarios.forEach(usuario => {

            const fila = document.createElement("tr");
            fila.className = "border-b text-center border-amber-950";

            fila.innerHTML = `

                <td class="p-3 font-semibold"> ${usuario.usuario} </td>
                <td class="p-3 font-semibold"> ${usuario.nombre} </td>
            `;

            const estado = document.createElement("td");
            estado.className = "p-3 font-semibold";

            if (usuarioEstaBloqueado(usuario)) {
                estado.textContent = "Bloqueado";
                estado.className = "text-red-800";
                console.log("BLOQUEADO");
            } else {
                estado.textContent = "Activo";
                estado.classList.add("text-green-800");
                console.log("ACTIVO");
            }


            const acciones = document.createElement("td");
            acciones.className = "p-3 font-semibold";
            acciones.appendChild(crearBotones(usuario));

            fila.appendChild(estado);
            fila.appendChild(acciones);

            tablaUsuario.appendChild(fila);



        });



    }


    cargarUsuarios();



})();