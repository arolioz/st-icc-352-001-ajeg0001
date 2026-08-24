const DB_NAME = "formulariosDB";
const DB_VERSION = 1;
const STORE_NAME = "fotos";

function  abrirDB() {

    return new Promise((resolve, reject) => {

        const request = indexedDB.open(DB_NAME, DB_VERSION);

        request.onupgradeneeded = (evento) => {

            const db = evento.target.result;

            if (!db.objectStoreNames.contains(STORE_NAME)) {
                db.createObjectStore(STORE_NAME);
            }
        };

        request.onsuccess = () => {
            resolve(request.result);
        };

        request.onerror = () => {
            reject(request.error);
        };
    });

}

async function guardarFoto(id, foto) {

    const db = await abrirDB();

    return new Promise((resolve, reject) => {

        const transaccion = db.transaction(STORE_NAME, "readwrite");

        const store = transaccion.objectStore(STORE_NAME);

        store.put(foto, id);

        transaccion.oncomplete = () => {
            resolve();
        };

        transaccion.onerror = () => {
            reject(transaccion.error);
        };
    });
}

async function obtenerFoto(id) {

    const db = await abrirDB();

    return new Promise((resolve, reject) => {

        const transaccion = db.transaction(STORE_NAME, "readonly");

        const store = transaccion.objectStore(STORE_NAME);

        const request = store.get(id);

        request.onsuccess = () => {
            resolve(request.result);
        };

        request.onerror = () => {
            reject(request.error);
        };
    });
}

async function eliminarFoto(id) {
    const db = await abrirDB();

    return new Promise((resolve, reject) => {
        const transaccion = db.transaction(STORE_NAME, "readwrite");
        const store = transaccion.objectStore(STORE_NAME);

        store.delete(id);

        transaccion.oncomplete = () => resolve();
        transaccion.onerror = () => reject(transaccion.error);
    });
}

