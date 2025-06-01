document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('id').value = `${Date.now() + Math.floor(Math.random() * 900) - 1748557187000}`;
});

function registrarCliente() {
    const form = document.getElementById('formCliente');

    form.addEventListener('submit', function (e) {
        e.preventDefault();
        // Validación básica de campos requeridos y formato
        const nombre = document.getElementById('nombre').value.trim();
        const correo = document.getElementById('correo').value.trim();
        const telefono = document.getElementById('telefono').value.trim();
        const direccion = document.getElementById('direccion').value.trim();
        let advertencia = '';
        // Validación de nombre
        if (!nombre) advertencia += 'El nombre es obligatorio.\n';
        // Validación de correo
        const regexCorreo = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!correo) {
            advertencia += 'El correo es obligatorio.\n';
        } else if (!regexCorreo.test(correo)) {
            advertencia += 'El correo no tiene un formato válido.\n';
        }
        // Validación de teléfono (solo números, 7-12 dígitos)
        const regexTelefono = /^\d{7,12}$/;
        if (!telefono) {
            advertencia += 'El teléfono es obligatorio.\n';
        } else if (!regexTelefono.test(telefono)) {
            advertencia += 'El teléfono debe contener solo números (7 a 12 dígitos).\n';
        }
        // Validación de dirección
        if (!direccion) advertencia += 'La dirección es obligatoria.';
        if (advertencia) {
            mostrarAdvertenciaCliente(advertencia);
            return;
        }
        const id = document.getElementById('id').value;
        const esEdicion = form.getAttribute('data-edit-id');
        const cliente = {
            id: id,
            nombre: nombre,
            email: correo,
            telefono: telefono,
            direccion: direccion
        };
        let url = '/api/clientes/crearCliente';
        let method = 'POST';
        if (esEdicion) {
            url = `/api/clientes/actualizarCliente/${id}`;
            method = 'PUT';
        }
        fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(cliente)
        })
            .then(res => {
                if (!res.ok) throw new Error('Error al ' + (esEdicion ? 'actualizar' : 'registrar') + ' cliente');
                return res.json();
            })
            .then(data => {
                alert("Cliente " + (esEdicion ? "actualizado" : "registrado") + " correctamente:\n" + JSON.stringify(data, null, 2));
                form.reset();
                form.removeAttribute('data-edit-id');
                document.getElementById('btnCliente').textContent = 'Registrar';
                obtenerClientes();
            })
            .catch(err => mostrarAdvertenciaCliente("Error: " + err.message));
    });
}
document.addEventListener('DOMContentLoaded', registrarCliente);

async function obtenerClientes() {
    const tbody = document.querySelector("table tbody");
    tbody.innerHTML = "";

    try {
        const response = await fetch('api/clientes/obtenerClientes');
        const clientes = await response.json();

        clientes.forEach(cliente => {
            const fila = document.createElement("tr");
            fila.innerHTML = `
        <td>${cliente.id}</td>
        <td>${cliente.nombre}</td>
        <td>${cliente.email}</td>
        <td>${cliente.telefono}</td>
        <td>${cliente.direccion}</td>
        <td>
          <button class="btn btn-warning btn-sm" onclick="editarCliente('${cliente.id}')">Editar</button>
        </td>
      `;
            tbody.appendChild(fila);
        });

    } catch (error) {
        console.error('Error al cargar cliente:', error);
    }
}

document.addEventListener('DOMContentLoaded', obtenerClientes);

function obtenerClientePorId(id) {
    fetch(`/api/clientes/obtenerCliente/${id}`)
        .then(res => {
            if (!res.ok) throw new Error('Cliente no encontrado');
            return res.json();
        })
        .then(data => {
            alert("Cliente:\n" + JSON.stringify(data, null, 2));
        })
        .catch(err => alert(err.message));
}
function actualizarCliente(id) {

    const clienteActualizado = {
        nombre: document.getElementById('nombre').value,
        email: document.getElementById('correo').value,
        telefono: document.getElementById('telefono').value,
        direccion: document.getElementById('direccion').value,
    };
    fetch(`/api/clientes/actualizarCliente/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(clienteActualizado)
    })
        .then(res => {
            if (!res.ok) throw new Error('Error al actualizar cliente');
            return res.json();
        })
        .then(data => {
            alert("Cliente actualizado:\n" + JSON.stringify(data, null, 2));
        })
        .catch(err => alert(err.message));
}
function eliminarCliente(id) {
    fetch(`/api/clientes/eliminarCliente/${id}`, {
        method: 'DELETE'
    })
        .then(res => {
            if (!res.ok) throw new Error('Error al eliminar cliente');
            alert("Cliente eliminado con éxito");
        })
        .catch(err => alert(err.message));
}

// Mostrar advertencia visual para cliente
function mostrarAdvertenciaCliente(mensaje) {
    let alerta = document.getElementById('alerta-validacion-cliente');
    if (!alerta) {
        alerta = document.createElement('div');
        alerta.id = 'alerta-validacion-cliente';
        alerta.className = 'alert alert-danger mt-2';
        const form = document.getElementById('formCliente');
        form.parentNode.insertBefore(alerta, form);
    }
    alerta.textContent = mensaje;
    setTimeout(() => {
        if (alerta) alerta.remove();
    }, 4000);
}

// Función para buscar clientes
function registrarBusquedaCliente() {
    const formBuscar = document.getElementById('formBuscarCliente');
    const inputBuscar = document.getElementById('buscarCliente');
    formBuscar.addEventListener('submit', async function (e) {
        e.preventDefault();
        const valor = inputBuscar.value.trim();
        if (!valor) {
            obtenerClientes();
            return;
        }
        let clientes = [];
        try {
            // Buscar por ID si es número
            if (/^\d+$/.test(valor)) {
                const res = await fetch(`/api/clientes/obtenerCliente/${valor}`);
                if (res.ok) {
                    const cli = await res.json();
                    clientes = [cli];
                }
            } else {
                // Buscar por nombre o correo (filtrado en frontend)
                const res = await fetch('/api/clientes/obtenerClientes');
                if (res.ok) {
                    const todos = await res.json();
                    clientes = todos.filter(c =>
                        c.nombre.toLowerCase().includes(valor.toLowerCase()) ||
                        c.email.toLowerCase().includes(valor.toLowerCase())
                    );
                }
            }
            mostrarClientesEnTabla(clientes);
        } catch (err) {
            mostrarClientesEnTabla([]);
        }
    });
    inputBuscar.addEventListener('input', function () {
        if (!inputBuscar.value.trim()) obtenerClientes();
    });
}
document.addEventListener('DOMContentLoaded', registrarBusquedaCliente);

// Función para editar cliente
function editarCliente(id) {
    fetch(`/api/clientes/obtenerCliente/${id}`)
        .then(res => {
            if (!res.ok) throw new Error('Cliente no encontrado');
            return res.json();
        })
        .then(cliente => {
            document.getElementById('id').value = cliente.id;
            document.getElementById('nombre').value = cliente.nombre;
            document.getElementById('correo').value = cliente.email;
            document.getElementById('telefono').value = cliente.telefono;
            document.getElementById('direccion').value = cliente.direccion;
            form.setAttribute('data-edit-id', id);
            document.getElementById('btnCliente').textContent = 'Actualizar';
        })
        .catch(err => mostrarAdvertenciaCliente(err.message));
}
