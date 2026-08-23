(() => {
    console.log("CRUD FORMULARIOS");
    const worker = new Worker('js/worker.js');
    const token = localStorage.getItem('token');
    worker.postMessage({ tipo: 'token', valor: token });

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
            if (form.id === null){
                estado.textContent = "Pendiente";
                estado.className = "text-warning";
            }
            else{
                estado.textContent = "Sincronizada";
                estado.className = "text-success";
            }

            filaEstado.appendChild(estado);
            fila.appendChild(filaEstado);

            const filaAcciones = document.createElement("td");

            const btnVer = document.createElement("button");
            btnVer.type = "button";
            btnVer.className = "btn btn-secondary btn-sm ms-2";
            btnVer.textContent = "Visualizar";

            btnVer.addEventListener("click", () => {
                visualizarFormulario(form);

            });

            const btnEditar = document.createElement("button");
            btnEditar.type = "button";
            btnEditar.className = "btn btn-primary btn-sm ms-2";
            btnEditar.textContent = "Editar";

            btnEditar.addEventListener("click", () => {
               editarFormulario(form);
            });

            const btnEliminar = document.createElement("button");
            btnEliminar.type = "button";
            btnEliminar.className = "btn btn-danger btn-sm ms-2";
            btnEliminar.textContent = "Eliminar";

            btnEliminar.addEventListener("click", () => {

                localStorage.setItem("accionForm", "eliminar");
                localStorage.setItem("form", form.uuid);

                eliminarFormulario();
            });

            const btnEnviar = document.createElement("button");
            btnEnviar.onclick = () => {
                btnEnviar.disabled = true;
                worker.postMessage({ tipo: 'enviar', valor: form });
            };
            btnEnviar.type = "button";
            btnEnviar.className = "btn btn-success btn-sm ms-2";
            btnEnviar.textContent = "Enviar al servidor";


            filaAcciones.appendChild(btnVer);
            if (form.id === null){
                filaAcciones.appendChild(btnEditar);
                filaAcciones.appendChild(btnEliminar);
                filaAcciones.appendChild(btnEnviar);
            }

            fila.appendChild(filaAcciones);
            tabla.appendChild(fila);


        });

        function visualizarFormulario(form) {
            localStorage.setItem("accionForm", "visualizar");
            localStorage.setItem("form", form.uuid);

            console.log("Acción:", localStorage.getItem("accionForm"));
            console.log("UUID:", localStorage.getItem("form"));


            window.location.href = "formulario.html";
        }

        function editarFormulario(form) {
            localStorage.setItem("accionForm", "editar");
            localStorage.setItem("form", form.uuid);

            console.log("Acción:", localStorage.getItem("accionForm"));
            console.log("UUID:", localStorage.getItem("form"));


            window.location.href = "formulario.html";
        }

        function eliminarFormulario() {
            const uuid = localStorage.getItem("form");

            if (!uuid) {
                alert("Formulario no encontrado");
                return;
            }

            if (!confirm("¿Está seguro de que desea eliminar este formulario?")) {
                return;
            }

            const formularios = JSON.parse(localStorage.getItem("formularios")) || [];

            const formulariosActualizados = formularios.filter(form => form.uuid !== uuid);

            localStorage.setItem("formularios", JSON.stringify(formulariosActualizados));

            localStorage.removeItem("accionForm");
            localStorage.removeItem("form");

            cargarFormularios();
        }
    }

    worker.onmessage = (evento) => {
        console.log('worker:', evento.data);
        
        const { estado, datos } = evento.data;
        if (estado !== 'ack') return;


        if (datos.ok) {
            const todas = JSON.parse(localStorage.getItem('formularios') || '[]');
            const i = todas.findIndex(e => e.uuid === datos.uuid);
            if (i >= 0) {
                todas[i].id = datos.id;
                localStorage.setItem('formularios', JSON.stringify(todas));
            }
            avisar('Encuesta sincronizada', 'ok');
        } else {
            avisar('No se pudo enviar: ' + datos.motivo, 'error');
        }

        cargarFormularios();
    };

    cargarFormularios();


})();