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