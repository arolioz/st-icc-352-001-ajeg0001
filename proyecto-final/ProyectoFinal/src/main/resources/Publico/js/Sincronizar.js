(() => {
const t = localStorage.getItem('token');
const ws = new WebSocket('ws://localhost:7000/sincronizacion?token=' + t);

ws.onmessage = e => console.log('ACK:', e.data);

ws.onopen = () => ws.send(JSON.stringify({
    uuid: 'ws-1',
    nombre: 'Carlos WebSocket',
    sector: 'Los Jardines',
    nivelEscolar: 'MEDIO',
    latitud: 19.45,
    longitud: -70.69
}));
})();