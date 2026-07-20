(() => {

    console.log("Cargando estadisticas...");
    const idEvento = new URLSearchParams(window.location.search).get('id');
    let inscripcionesChart = null;
    let asistenciaChart = null;

    async function cargarDetalleEvento() {

        try {

            const respuesta = await fetch(`/Api/obtenerEstadisticaEvento/${idEvento}`);
            console.log(respuesta);

            if (!respuesta.ok) {
                throw new Error(`HTTP error! Status: ${respuesta.status}`);
            }

            const data = await respuesta.json();

            console.log(data);

            document.getElementById("titulo").textContent = `Estadisticas del evento ${data.evento}`

            mostrarEstadisticasEventos(data);
            actualizarGraficaInscritos(data);
            actualizarGraficaAsistencia(data);

        } catch (error) {
            console.error('Fetch error:', error);
        }

    }

    async function actualizarGraficaInscritos(evento){
        const ctx = document.getElementById('inscripcionesChart');

        try {

            const nombres = Object.keys(evento.inscripcionesPorDia);
            const cantidad = Object.values(evento.inscripcionesPorDia);

            if (inscripcionesChart == null){
                inscripcionesChart = new Chart(ctx, {
                    type: 'pie',
                    data: {
                        labels: nombres,
                        datasets: [{
                            label: 'Inscripciones',
                            data: cantidad,
                            borderWidth: 1
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false
                    }
                });
            }
            else{
                inscripcionesChart.data.labels = nombres;
                inscripcionesChart.data.datasets[0].data = cantidad;
                inscripcionesChart.update();
            }
        } catch (error) {
            console.error('Fetch error:', error);
        }
    }

    async function actualizarGraficaAsistencia(evento){
        const ctx = document.getElementById('asistencaiChart');

        try {

            const nombres = Object.keys(evento.asistenciaPorHora);
            const cantidad = Object.values(evento.asistenciaPorHora);

            if (asistenciaChart == null){
                asistenciaChart = new Chart(ctx, {
                    type: 'pie',
                    data: {
                        labels: nombres,
                        datasets: [{
                            label: 'Asistencia',
                            data: cantidad,
                            borderWidth: 1
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false
                    }
                });
            }
            else{
                asistenciaChart.data.labels = nombres;
                asistenciaChart.data.datasets[0].data = cantidad;
                asistenciaChart.update();
            }
        } catch (error) {
            console.error('Fetch error:', error);
        }
    }


    async function mostrarEstadisticasEventos(evento) {

        const totalInscritos = await evento.totalInscritos;
        const totalAsistencia = await evento.totalAsistencia;
        const porcentajeAsistencia = await evento.porcentajeAsistencia;

        document.getElementById("inscritos").textContent = `Total inscritos: ${totalInscritos}`
        document.getElementById("asistencia").textContent = `Total asistente: ${totalAsistencia}`
        document.getElementById("procentajeAsistencia").textContent = `Porcentaje Asistencia: ${porcentajeAsistencia}%`

    }

    async function cargarUsuarios() {

        try {


            const respuesta = await fetch(`/Api/obtenerUsuariosEvento/${idEvento}`);


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

            if (usuario.asistio === false) {
                estado.textContent = "No";
                estado.className = "text-red-800";
                console.log("BLOQUEADO");
            } else {
                estado.textContent = "Si";
                estado.classList.add("text-green-800");
                console.log("ACTIVO");
            }


            fila.appendChild(estado);


            tablaUsuario.appendChild(fila);


        });
    }

    window.cargarUsuarios = cargarUsuarios;
    window.cargarEstadisticas = cargarDetalleEvento;

    cargarUsuarios();
    cargarDetalleEvento();

})();