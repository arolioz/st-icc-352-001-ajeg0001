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

    function usuarioEsOrganizador(usuario) {

        if (usuario.listaRoles.includes("ROLE_ORGANIZADOR")) {
            return true;  
        } else {
            return false;
        }
    }

    async function bloquearUsuario(idUsuario){

        try {
            const respuesta = await fetch(`/Usuarios/bloquear/${idUsuario}`, {
            method: "POST"
            });

            if (!respuesta.ok){
                throw new Error("Error cambiando estado");
            }

            cargarUsuarios();
            console.log(idUsuario);

        } catch (error) {
            console.error(error);

        }

        
    }


    async function cambiarRolOrganizador(idUsuario) {

        try {

        const respuesta = await fetch(`/Usuarios/cambiarRol/${idUsuario}`, {
            method: "POST"
        });


        if (!respuesta.ok){
            throw new Error("Error cambiando rol");
        }


        cargarUsuarios();

    } catch(error){
        console.error(error);
    }
        
    }

    function crearBotones(usuario) {

        const container = document.createElement("div");
        container.className = "flex gap-2 justify-center";
        
        const botonOrganizador = document.createElement("button");
        botonOrganizador.className = "boton1 color4 text-center block flex-1 !w-auto"

        if (usuarioEsOrganizador(usuario)) {
            botonOrganizador.textContent = "Quitar organizador";
        } else {
            botonOrganizador.textContent = "Hacer organizador";
        }

        botonOrganizador.addEventListener("click", () => {
            cambiarRolOrganizador(usuario.id);
        });

        const btnBloquear = document.createElement("button");
        btnBloquear.className = "boton1 color3 text-center block flex-1 !w-auto";

        if (usuarioEstaBloqueado(usuario)) {
            btnBloquear.textContent = "Desbloquear";
        } else {
            btnBloquear.textContent = "Bloquear";
        }

        btnBloquear.addEventListener("click", () => {
            bloquearUsuario(usuario.id);
        });

        container.appendChild(botonOrganizador);
        container.appendChild(btnBloquear);

        return container;

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
            const botones =  crearBotones(usuario);
            acciones.appendChild(botones);

            fila.appendChild(estado);
            fila.appendChild(acciones);

            tablaUsuario.appendChild(fila);



        });



    }


    cargarUsuarios();



})();