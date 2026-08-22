(() => {
    console.log("CRUD FORMULARIOS");

    function cargarFormularios() {

        const formularios = JSON.parse(localStorage.getItem("formularios")) || [];

        const tabla = document.getElementById("idTablaFormularios");
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
            const fila= document.createElement("tr");

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
                    ${form.usuario}
                </td>
                
                <td>
                    <img src="${form.foto}" width="80" height="60" class="rounded" style="object-fit: cover;">
                </td>
            `;

            const filaEstado = document.createElement("td");
            const estado = document.createElement("span");
            estado.textContent = "Pendiente";
            estado.className = "text-warning";
            filaEstado.appendChild(estado);
            fila.appendChild(filaEstado);

            const filaAcciones = document.createElement("td");

            const btnVer = document.createElement("button");
            btnVer.type = "button";
            btnVer.className = "btn btn-secondary btn-sm ms-2";
            btnVer.textContent = "Visualizar";

            const btnEditar = document.createElement("button");
            btnEditar.type = "button";
            btnEditar.className = "btn btn-primary btn-sm ms-2";
            btnEditar.textContent = "Editar";

            //

            const btnEliminar = document.createElement("button");
            btnEliminar.type = "button";
            btnEliminar.className = "btn btn-danger btn-sm ms-2";
            btnEliminar.textContent = "Eliminar";

            const btnEnviar = document.createElement("button");
            btnEnviar.type = "button";
            btnEnviar.className = "btn btn-success btn-sm ms-2";
            btnEnviar.textContent = "Enviar al servidor";

            filaAcciones.appendChild(btnVer);
            filaAcciones.appendChild(btnEditar);
            filaAcciones.appendChild(btnEliminar);
            filaAcciones.appendChild(btnEnviar);

            fila.appendChild(filaAcciones);
            tabla.appendChild(fila);


        });




    }

    cargarFormularios();


})();