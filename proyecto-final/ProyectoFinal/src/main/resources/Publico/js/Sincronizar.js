(() => {
const ws = new WebSocket('ws://localhost:7000/sincronizacion');

ws.onopen    = () => { console.log('abierto'); ws.send('hola'); };
ws.onmessage = (e) => console.log('servidor dice:', e.data);
ws.onclose   = (e) => console.log('cerrado', e.code);
})();