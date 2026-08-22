(() => {

    console.log("CRUD USUARIOS");

    if (!verificarSesionAdmin()) {
        alert("Debe ser administrador para ver esta pagina");
        return;
    }

    async function cargarUsuarios() {

        try {

            const respuesta = await fetch("/api/usuario", {
                headers: {
                    "Authorization": "Bearer " + localStorage.getItem("token"),
                    "Accept": "application/json"
                }
            });

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

        const tabla = document.getElementById("idTablaUsuarios");

        tabla.innerHTML = "";

        if (usuarios.length === 0) {

            tabla.innerHTML = `
                <tr>
                    <td colspan="3" class="text-center">
                        <h5>No hay usuarios registrados</h5>
                    </td>
                </tr>
            `;

            return;
        }

        usuarios.forEach(usuario => {

            const fila = document.createElement("tr");

            fila.innerHTML = `
                <td>${usuario.user}</td>
            `;

            const acciones = document.createElement("td");

            const btnRol = document.createElement("button");
            btnRol.className = "btn btn-secondary btn-sm me-2";
            btnRol.textContent = "Cambiar rol";

            const btnEditar = document.createElement("button");
            btnEditar.className = "btn btn-primary btn-sm me-2";
            btnEditar.textContent = "Editar";

            const btnEliminar = document.createElement("button");
            btnEliminar.className = "btn btn-danger btn-sm";
            btnEliminar.textContent = "Eliminar";

            acciones.appendChild(btnRol);
            acciones.appendChild(btnEditar);
            acciones.appendChild(btnEliminar);

            fila.appendChild(acciones);
            tabla.appendChild(fila);
        });

    }

    cargarUsuarios();


})();