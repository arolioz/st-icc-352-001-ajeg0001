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

        document.getElementById("btnLoginMovil")?.classList.add("hidden");
        document.getElementById("btnRegistrarMovil")?.classList.add("hidden");

        document.getElementById("nombreUsuario").textContent = usuario.usuario;
        document.getElementById("infoUsuario").classList.remove("hidden");


        document.getElementById("nombreUsuarioMovil").textContent = usuario.usuario;
        document.getElementById("infoUsuarioMovil").classList.remove("hidden");


    }

async function configurarNavbar() {
    const btnMenu = document.getElementById("btnMenuMovil");
    const menu = document.getElementById("menuMovil");

    btnMenu.addEventListener("click", () => {
        menu.classList.toggle("hidden");
    });

    const usuario = await obtenerUsuarioActual();

    if (!usuario) {
        return;
    }

    const roles = usuario.listaRoles;

    if (roles.includes("ROLE_ORGANIZADOR")) {
        document.getElementById("navEventosMovil")?.classList.remove("hidden");
        document.getElementById("navCrearEventoMovil")?.classList.remove("hidden");

        document.getElementById("navEventos")?.classList.remove("hidden");
        document.getElementById("navCrearEvento")?.classList.remove("hidden");
    }

    if (roles.includes("ROLE_ADMIN")) {
        document.getElementById("navUsuario")?.classList.remove("hidden");
        document.getElementById("navEventos")?.classList.remove("hidden");

        document.getElementById("navUsuarioMovil")?.classList.remove("hidden");
        document.getElementById("navEventosMovil")?.classList.remove("hidden");
    }

}