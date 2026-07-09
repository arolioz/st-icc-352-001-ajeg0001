(() => {
let socket;
    let heartbeat;
    let retardoReconexion = 1000;        // 1 s inicial
    const retardoMaximo = 30000;         // tope de 30 s

    let $cantUsuarios = document.getElementById("cantUsuarios");

    function conectar() {
        // Usa wss:// si la página se sirvió por HTTPS; si no, ws://.
        const esquema = location.protocol === "https:" ? "wss" : "ws";
        const url = `${esquema}://${location.host}/conectarUsuario`;
        socket = new WebSocket(url);

        // open: handshake completado (HTTP 101 Switching Protocols).
        socket.addEventListener("open", () => {

            retardoReconexion = 1000;    // reiniciamos el backoff al conectar
            iniciarHeartbeat();
        });

        // message: el servidor nos envía HTML ya formateado; lo insertamos tal cual.
        socket.addEventListener("message", (evento) => {
            console.log("Recibido del servidor:", evento.data);
            $cantUsuarios.textContent = evento.data;
        });

        // close: la conexión se cerró; programamos reintento con backoff.
        socket.addEventListener("close", () => {
            clearInterval(heartbeat);
            setTimeout(conectar, retardoReconexion);
            retardoReconexion = Math.min(retardoReconexion * 1.5, retardoMaximo);
        });

        // error: por seguridad el navegador no da detalles; cerramos para reconectar.
        socket.addEventListener("error", () => socket.close());
    }

    // Heartbeat: enviamos un "latido" cada 30 s para que proxies/balanceadores
    // no cierren la conexión por inactividad.
    function iniciarHeartbeat() {
        heartbeat = setInterval(() => {
            if (socket.readyState === WebSocket.OPEN) {
                socket.send(JSON.stringify({ type: "ping" }));
            }
        }, 30000);
    }


    conectar();
})();

