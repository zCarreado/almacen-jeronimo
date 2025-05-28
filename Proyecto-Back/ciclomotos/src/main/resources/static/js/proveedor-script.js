function registrarProveedor() {
    const form = document.getElementById('formProveedor');

    form.addEventListener('submit', function (e) {
        e.preventDefault();

        const proveedor = {
            nombre: document.getElementById('nombre').value,
            email: document.getElementById('correo').value,
            telefono: document.getElementById('telefono').value,
            direccion: document.getElementById('direccion').value,
        };

        fetch('/api/proveedores/crearProveedor', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(proveedor)
        })
            .then(res => {
                if (!res.ok) throw new Error('Error al registrar proveedor');
                return res.json();
            })
            .then(data => {
                alert("Proveedor registrado correctamente:\n" + JSON.stringify(data, null, 2));
                form.reset();
            })
            .catch(err => alert("Error: " + err.message));
    });
}
document.addEventListener('DOMContentLoaded', registrarProveedor);

function obtenerProveedores() {
    fetch('/api/proveedores')
        .then(res => res.json())
        .then(data => {
            const lista = document.getElementById('listaProveedores');
            lista.innerHTML = '';
            data.forEach(p => {
                const item = document.createElement('li');
                item.textContent = `${p.nombre} - ${p.correo}`;
                lista.appendChild(item);
            });
        })
        .catch(err => console.error('Error al cargar proveedores:', err));
}
function obtenerProveedorPorId(id) {
    fetch(`/api/proveedores/obtenerProveedor/${id}`)
        .then(res => {
            if (!res.ok) throw new Error('proveedor no encontrado');
            return res.json();
        })
        .then(data => {
            alert("Proveedor:\n" + JSON.stringify(data, null, 2));
        })
        .catch(err => alert(err.message));
}
function actualizarProveedor(id) {
    const proveedorActualizado = {
        nombre: document.getElementById('nombre').value,
        correo: document.getElementById('correo').value,
        telefono: document.getElementById('telefono').value,
        direccion: document.getElementById('direccion').value
    };

    fetch(`/api/proveedores/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(proveedorActualizado)
    })
        .then(res => {
            if (!res.ok) throw new Error('Error al actualizar proveedor');
            return res.json();
        })
        .then(data => alert('Proveedor actualizado:\n' + JSON.stringify(data, null, 2)))
        .catch(err => alert(err.message));
}
function eliminarProveedor(id) {
    if (!confirm('¿Seguro que deseas eliminar este proveedor?')) return;

    fetch(`/api/proveedores/${id}`, {
        method: 'DELETE'
    })
        .then(res => {
            if (!res.ok) throw new Error('Error al eliminar proveedor');
            alert('Proveedor eliminado correctamente');
            cargarProveedores(); // Recargar lista después de eliminar
        })
        .catch(err => alert(err.message));
}
