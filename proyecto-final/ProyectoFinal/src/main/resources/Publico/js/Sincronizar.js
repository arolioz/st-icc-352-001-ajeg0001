(() => {
const t = localStorage.getItem('token');

const ws = new WebSocket('ws://localhost:7000/sincronizacion?token=' + t);
ws.onopen    = () => ws.send('hola');
ws.onmessage = e => console.log('servidor:', e.data);
ws.onclose   = e => console.log('cerrado', e.code, e.reason);
})();