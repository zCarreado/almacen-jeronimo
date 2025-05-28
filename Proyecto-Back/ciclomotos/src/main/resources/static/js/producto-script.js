function registrarProducto() {
    const form = document.getElementById('formProducto');

    form.addEventListener('submit', function (e) {
        e.preventDefault();

        const producto = {
            nombre: document.getElementById('nombre').value,
            categoria: document.getElementById('categoria').value,
            precio: document.getElementById('telefono').value,
            cantidad: document.getElementById('direccion').value,
            stockMinimo: document.getElementById('stock-minimo').value,
        };

        fetch('/api/productos/crearProducto', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(producto)
        })
            .then(res => {
                if (!res.ok) throw new Error('Error al registrar producto');
                return res.json();
            })
            .then(data => {
                console.log("Respuesta del servidor:", data); // 👈 verifica en consola
                alert("Producto registrado correctamente:\n" + JSON.stringify(data, null, 2));
                form.reset();
            })
            .catch(err => alert("Error: " + err.message));
    });
}
document.addEventListener('DOMContentLoaded', registrarProducto);

function obtenerProductoes() {
    fetch('/api/productos/')
        .then(res => res.json())
        .then(data => {
            const lista = document.getElementById('listaProductoes');
            lista.innerHTML = '';
            data.forEach(p => {
                const item = document.createElement('li');
                item.textContent = `${p.nombre} - ${p.correo}`;
                lista.appendChild(item);
            });
        })
        .catch(err => console.error('Error al cargar productoes:', err));
}
function obtenerProductoPorId(id) {
    fetch(`/api/productos/obtenerProducto/${id}`)
        .then(res => {
            if (!res.ok) throw new Error('producto no encontrado');
            return res.json();
        })
        .then(data => {
            alert("Producto:\n" + JSON.stringify(data, null, 2));
        })
        .catch(err => alert(err.message));
}
function actualizarProducto(id) {

    const productoActualizado = {
        nombre: document.getElementById('nombre').value,
        categoria: document.getElementById('categoria').value,
        precio: document.getElementById('telefono').value,
        cantidad: document.getElementById('direccion').value,
        stockMinimo: document.getElementById('direccion').value,
    };

    fetch(`/api/productos/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(productoActualizado)
    })
        .then(res => {
            if (!res.ok) throw new Error('Error al actualizar producto');
            return res.json();
        })
        .then(data => alert('Producto actualizado:\n' + JSON.stringify(data, null, 2)))
        .catch(err => alert(err.message));
}
function eliminarProducto(id) {
    if (!confirm('¿Seguro que deseas eliminar este producto?')) return;

    fetch(`/api/productos/${id}`, {
        method: 'DELETE'
    })
        .then(res => {
            if (!res.ok) throw new Error('Error al eliminar producto');
            alert('Producto eliminado correctamente');
            cargarProductoes(); // Recargar lista después de eliminar
        })
        .catch(err => alert(err.message));
}
