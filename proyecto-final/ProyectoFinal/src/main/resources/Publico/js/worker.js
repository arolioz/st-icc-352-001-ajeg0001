importScripts('indexedDB.js');

const TIEMPO_ESPERA_ACK = 20000;

let token = null;
let socket = null;

const esperando = new Map();

function conectar() {
    return new Promise((resolve, reject) => {
 
        if (socket && socket.readyState === WebSocket.OPEN) {
            resolve(socket);
            return;
        }
 
        if (!token) {
            reject(new Error('Falta el token'));
            return;
        }

        const protocolo = self.location.protocol === 'https:' ? 'wss:' : 'ws:';
        const url = `${protocolo}//${self.location.host}/sincronizacion?token=${encodeURIComponent(token)}`;
 
        socket = new WebSocket(url);
 
        socket.onopen = () => {
            avisar('conectado');
            resolve(socket);
        };
 
        socket.onmessage = (evento) => recibirAck(evento.data);
 
        socket.onerror = () => reject(new Error('No se pudo abrir el WebSocket'));

        socket.onclose = (evento) => {
            avisar('cerrado', `${evento.code} — ${evento.reason}`);

            for (const [uuid, resolver] of esperando) {
                resolver({ uuid, ok: false, motivo: 'conexion cerrada' });
            }
            esperando.clear();
 
            socket = null;
        };
    });
}

function recibirAck(crudo) {
    let ack;
 
    try {
        ack = JSON.parse(crudo);
    } catch {
        return;
    }
 
    const resolver = esperando.get(ack.uuid);
 
    if (resolver) {
        esperando.delete(ack.uuid);
        resolver(ack);
    }
}
 
async function enviarYEsperar(encuesta) {

    const foto = await obtenerFoto(encuesta.uuid);

    if (!foto) {
        console.warn(`No se encontró foto para ${encuesta.uuid}`);
    }

    return new Promise((resolve) => {
 
        esperando.set(encuesta.uuid, resolve);

        socket.send(JSON.stringify({
            uuid: encuesta.uuid,
            nombre: encuesta.nombre,
            sector: encuesta.sector,
            nivelEscolar: encuesta.nivelEscolar,
            latitud: encuesta.latitud,
            longitud: encuesta.longitud,
            fotoBase64: foto.split(',')[1] || ""
        }));
 
        // si el servidor no contesta, no dejamos la promesa colgada
        setTimeout(() => {
            if (esperando.delete(encuesta.uuid)) {
                resolve({ uuid: encuesta.uuid, ok: false, motivo: 'sin respuesta' });
            }
        }, TIEMPO_ESPERA_ACK);
    });
}

async function enviarUna(encuesta) {
    if (!encuesta || !encuesta.uuid) {
        avisar('error', 'Encuesta sin uuid');
        return;
    }
 
    try {
        await conectar();
 
        const ack = await enviarYEsperar(encuesta);
        avisar('ack', null, ack);
 
    } catch (error) {
        // sin red: la encuesta se queda pendiente en la pagina
        avisar('ack', null, {
            uuid: encuesta.uuid,
            ok: false,
            motivo: error.message || 'sin conexion'
        });
    }
}

self.onmessage = (evento) => {
    const { tipo, valor } = evento.data || {};
 
    switch (tipo) {
        case 'token':
            token = valor;
            avisar('token-recibido', String(valor).slice(0, 15) + '...');
            break;
 
        case 'enviar':
            enviarUna(valor);
            break;
 
        case 'cerrar':
            if (socket) socket.close();
            break;
 
        default:
            avisar('error', 'Mensaje desconocido: ' + tipo);
    }
};

function avisar(estado, mensaje = null, datos = null) {
    self.postMessage({ estado, mensaje, datos });
}