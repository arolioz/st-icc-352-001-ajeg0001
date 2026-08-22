
function validarSesion() {
    const token = localStorage.getItem("token");
    const usuario = localStorage.getItem("usuario");
    const idUsuario = localStorage.getItem("id");

    if (token == null || !usuario || !idUsuario) {
        window.location.href = "login.html";
        return false;
    }

    return true;
}

function obtenerRoles() {
    const crudo = localStorage.getItem("roles");
    if (!crudo) return [];

    try {
        const valor = JSON.parse(crudo);
        return Array.isArray(valor) ? valor : [valor];
    } catch {
        // por si se guardo como texto separado por comas
        return crudo.split(",").map(r => r.trim());
    }
}

function verificarSesionAdmin() {
    if (!validarSesion()) return false;

    if (!obtenerRoles().includes("ROLE_ADMIN")) {
        //window.location.href = "formulario.html";
        return false;
    }

    return true;
}

function cerrarSesion() {

    localStorage.removeItem("token");
    localStorage.removeItem("id");
    localStorage.removeItem("usuario");
    localStorage.removeItem("roles");

    window.location.href = "login.html";
}