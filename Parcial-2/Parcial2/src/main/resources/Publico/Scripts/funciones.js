async function obtenerOrganizadorEvento(idOrganizador) {

    try {
        const respuesta =  await fetch(`/Api/obtenerOrganizador/${idOrganizador}`);

        if (!respuesta.ok) {
            throw new Error(`HTTP error! Status: ${respuesta.status}`);
        }

        const organizador =  respuesta.text();
        console.log(organizador);
        return organizador;

    } catch (error) {
        console.error('Fetch error:', error);
    }
}

async function obtenerCantidadInscritos(idEvento) {
    try {
        const respuesta =  await fetch(`/Api/obtenerCupo-Evento/${idEvento}`);

        if (!respuesta.ok) {
            throw new Error(`HTTP error! Status: ${respuesta.status}`);
        }

        const cantidadInscritos =  respuesta.json();
        console.log(cantidadInscritos);
        return cantidadInscritos;

    } catch (error) {
        console.error('Fetch error:', error);
    }
}

async function usuarioEsAdmin() {

        try {
            const respuesta = await fetch(`/Api/usuarioEsAdmin`);

            if (!respuesta.ok) {
                throw new Error(`HTTP error! Status: ${respuesta.status}`);
            }

            console.log(respuesta);
            return await respuesta.json();
        

        } catch (error){
            console.error('Fetch error: ', error);
        }

    }

    async function usuarioEsOrganizador() {

        try {
            const respuesta = await fetch(`/Api/usuarioEsOrganizador`);
            
            if (!respuesta.ok) {
                throw new Error(`HTTP error! Status: ${respuesta.status}`);
            }

            console.log(respuesta);
            return await respuesta.json();
        
        } catch (error){
            console.error('Fetch error: ', error);
        }

    }

    async function obtenerUsuarioActual() {

        try {
            const respuesta = await fetch("/Api/usuarioActual");

            if (!respuesta.ok) {
                throw new Error(`HTTP error! Status: ${respuesta.status}`);
            }

            console.log(respuesta);
            return await respuesta.json();

        } catch (error){
            console.error('Fetch error: ', error);
        }

    }

    async function configurarSesion() {

        const usuario = await obtenerUsuarioActual();

        if (!usuario) {
            return;
        }

        document.getElementById("btnLogin")?.classList.add("hidden");
        document.getElementById("btnRegistrar")?.classList.add("hidden");

        document.getElementById("nombreUsuario").textContent = usuario.usuario;
        document.getElementById("infoUsuario").classList.remove("hidden");

    }

async function configurarNavbar() {

    const usuario = await obtenerUsuarioActual();

    if (!usuario) {
        return;
    }

    const roles = usuario.listaRoles;

    if (roles.includes("ROLE_ORGANIZADOR")) {
        document.getElementById("navEventos")?.classList.remove("hidden");
        document.getElementById("navCrearEvento")?.classList.remove("hidden");
    }

    if (roles.includes("ROLE_ADMIN")) {
        document.getElementById("navUsuario")?.classList.remove("hidden");

        document.getElementById("navEventos")?.classList.remove("hidden");
    }
}