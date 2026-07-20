(() => {

    console.log("Cargando codigo qr...");
    const idEvento = new URLSearchParams(window.location.search).get('id');


    async function cargarDetalleEvento() {

        try {

            const respuesta = await fetch(`/Api/obtenerEstadisticaEvento/${idEvento}`);
            console.log(respuesta);

            if (!respuesta.ok) {
                throw new Error(`HTTP error! Status: ${respuesta.status}`);
            }

            const data = await respuesta.json();

            console.log(data);

            document.getElementById("titulo").textContent = `Codigo qr para del evento ${data.evento}`

            mostrarQr();

        } catch (error) {
            console.error('Fetch error:', error);
        }

    }





    async function mostrarQr() {
        const img = document.getElementById("qr");

        img.src = `/Usuarios/mostrarQr/${idEvento}`;
    }

    cargarDetalleEvento();

})();