document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('id').value = `${Date.now() + Math.floor(Math.random() * 900) - 1748557187000}`;
});

function registrarCliente() {
    const form = document.getElementById('formCliente');

    form.addEventListener('submit', function (e) {
        e.preventDefault();

        const cliente = {
            id: document.getElementById('id').value,
            nombre: document.getElementById('nombre').value,
            email: document.getElementById('correo').value,
            telefono: document.getElementById('telefono').value,
            direccion: document.getElementById('direccion').value,
            contraseña: document.getElementById('contraseña').value,
            //usuario: {
            //    usuario: document.getElementById('usuario').value,
            //    contraseña: document.getElementById('contraseña').value
            //}
        };

        fetch('/api/clientes/crearCliente', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(cliente)
        })
            .then(res => {
                if (!res.ok) throw new Error('Error al registrar cliente');
                return res.json();
            })
            .then(data => {
                alert("Cliente registrado correctamente:\n" + JSON.stringify(data, null, 2));
                form.reset();
            })
            .catch(err => alert("Error: " + err.message));
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
