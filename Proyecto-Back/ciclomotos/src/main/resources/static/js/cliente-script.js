function registrarCliente() {
    const form = document.getElementById('formCliente');

    form.addEventListener('submit', function (e) {
        e.preventDefault();

        const cliente = {
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

function obtenerClientes() {
    fetch('/api/clientes/obtenerClientes')
        .then(res => {
            if (!res.ok) throw new Error('Error al obtener clientes');
            return res.json();
        })
        .then(data => {
            console.log("Clientes:", data);
            alert("Clientes obtenidos:\n" + JSON.stringify(data, null, 2));
        })
        .catch(err => alert(err.message));
}
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
