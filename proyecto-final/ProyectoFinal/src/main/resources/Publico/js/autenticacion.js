
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

function verificarSesionAdmin() {

    if (!validarSesion()) {
        return false;
    }

    const roles = localStorage.getItem("roles");

    if (roles != "ADMIN") {
        window.location.href = "formulario.html";
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