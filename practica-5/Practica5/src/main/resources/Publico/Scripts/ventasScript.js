
(() => {
    let productosChart = null;
    let ventasSocket;
    let heartbeat;
    let retardoReconexion = 1000;        // 1 s inicial
    const retardoMaximo = 30000;         // tope de 30 s

    let $totalVentas = document.getElementById("totalVentas");

    async function actualizarTotalVentas(){
          try {
            const respuesta = await fetch('/administracion/totalVentas');


            if (!respuesta.ok) {
              throw new Error(`HTTP error! Status: ${respuesta.status}`);
            }

            const data = await respuesta.json();
            $totalVentas.textContent = data;
          } catch (error) {
            console.error('Fetch error:', error);
          }
    }
    window.addEventListener('resize', () => {
        if (productosChart) {
            productosChart.resize();
        }
    });
    async function actualizarGrafica(){
          const ctx = document.getElementById('productosChart');

          try {
            const respuesta = await fetch('/administracion/cantidad-productosVendidos');


            if (!respuesta.ok) {
              throw new Error(`HTTP error! Status: ${respuesta.status}`);
            }
            const data = await respuesta.json();
            console.log(data);
            const nombres = Object.keys(data);
            const cantidad = Object.values(data);

            if (productosChart == null){
                productosChart = new Chart(ctx, {
                    type: 'pie',
                    data: {
                    labels: nombres,
                    datasets: [{
                        label: 'Cantidad de ventas',
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
                    productosChart.data.labels = nombres;
                    productosChart.data.datasets[0].data = cantidad;
                    productosChart.update();
                }
          } catch (error) {
            console.error('Fetch error:', error);
          }
    }

    function conectar() {
        // Usa wss:// si la página se sirvió por HTTPS; si no, ws://.
        const esquema = location.protocol === "https:" ? "wss" : "ws";
        const url = `${esquema}://${location.host}/estVentas`;
        ventasSocket = new WebSocket(url);

        // open: handshake completado (HTTP 101 Switching Protocols).
        ventasSocket.addEventListener("open", () => {

            retardoReconexion = 1000;    // reiniciamos el backoff al conectar
            iniciarHeartbeat();
        });

        // message: el servidor nos envía HTML ya formateado; lo insertamos tal cual.
        ventasSocket.addEventListener("message", (evento) => {
            console.log("Recibido del servidor:", evento.data);
            if (evento.data == "Actualizar"){
                actualizarTotalVentas();
                actualizarGrafica();
            }
        });

        // close: la conexión se cerró; programamos reintento con backoff.
        ventasSocket.addEventListener("close", () => {
            clearInterval(heartbeat);
            setTimeout(conectar, retardoReconexion);
            retardoReconexion = Math.min(retardoReconexion * 1.5, retardoMaximo);
        });

        // error: por seguridad el navegador no da detalles; cerramos para reconectar.
        ventasSocket.addEventListener("error", () => ventasSocket.close());
    }

    // Heartbeat: enviamos un "latido" cada 30 s para que proxies/balanceadores
    // no cierren la conexión por inactividad.
    function iniciarHeartbeat() {
        heartbeat = setInterval(() => {
            if (ventasSocket.readyState === WebSocket.OPEN) {
                ventasSocket.send(JSON.stringify({ type: "ping" }));
            }
        }, 30000);
    }


    actualizarTotalVentas();
    actualizarGrafica();
    conectar();
})();

