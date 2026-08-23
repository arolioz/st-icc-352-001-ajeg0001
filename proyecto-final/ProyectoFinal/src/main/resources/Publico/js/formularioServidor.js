(() => {
    console.log("CRUD FORMULARIOS SERVIDOR");

    if (!verificarSesionAdmin()) {
        alert("Debe ser administrador para ver esta pagina");
        return;
    }

    async function cargarFormularios() {
        try {

            const respuesta = await fetch("/api/encuesta", {
                method: "GET",
                headers: {
                    "Authorization": "Bearer " + localStorage.getItem("token"),
                    "Accept": "application/json"
                }
            });

            if (!respuesta.ok) {
                throw new Error(`HTTP error! Status: ${respuesta.status}`);
            }

            const formularios = await respuesta.json();

            console.log(formularios);

            mostrarFormularios(formularios);

        } catch (error) {

            console.error("Error cargando formularios:", error);
            alert("No se pudieron cargar los formularios");

        }
    }

    function mostrarFormularios(formularios) {

        const tabla = document.getElementById("idformularioServidor");
        tabla.innerHTML = "";

        if (formularios.length === 0) {

            tabla.innerHTML = `
                <tr>
                    <td colspan="7" class="text-center">
                        <h5>
                            No hay formularios registrados
                        </h5>
                    </td>
                </tr>
           `;

            return;
        }

        formularios.forEach(form => {
            const fila = document.createElement("tr");
            console.log("ID:", form.id);
            //console.log(form);

            //console.log("Foto:", form.foto);
            //console.log("FotoBase64:", form.fotoBase64);

            fila.innerHTML = `

                <td>
                    ${form.nombre}
                </td>
    
                <td>
                    ${form.sector}
                </td>
    
                <td>
                    ${form.nivelEscolar}
                </td>
    
                <td>
                    ${form.usuarioNombre}
                </td>
                
                <td>
                    <img src="data:image/jpeg;base64,${form.fotoBase64}" width="80" height="60" class="rounded" style="object-fit: cover;">
                </td>
                
                <td>
                    ${form.fechaRegistro}
                </td>
                
            `;

            const filaAcciones = document.createElement("td");

            const btnEliminar = document.createElement("button");

            btnEliminar.type = "button";
            btnEliminar.className = "btn btn-danger btn-sm ms-2";
            btnEliminar.textContent = "Eliminar";

            btnEliminar.addEventListener("click", () => {
               eliminarFormulario(form.id);
            });

            filaAcciones.appendChild(btnEliminar);
            fila.appendChild(filaAcciones);
            tabla.appendChild(fila);
        });
    }

    async function eliminarFormulario(idForm) {

        if (!confirm("¿Está seguro de que desea eliminar este formulario?")) {
            return;
        }

        try {

            const respuesta = await fetch(`/api/encuesta/${idForm}`, {
                method: "DELETE",
                headers: {
                    "Authorization": "Bearer " + localStorage.getItem("token"),
                    "Accept": "application/json"
                }
            });

            if (!respuesta.ok) {
                throw new Error(`HTTP error! Status: ${respuesta.status}`);
            }

            alert("Formulario eliminado correctamente");

            await cargarFormularios();

        } catch (error) {

            console.error("Error eliminando formulario:", error);
            alert("No se pudo eliminar el formulario");

        }
    }




    cargarFormularios();


})();