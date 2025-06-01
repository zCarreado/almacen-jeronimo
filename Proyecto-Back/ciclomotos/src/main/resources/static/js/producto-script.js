function registrarProducto() {
    const form = document.getElementById('formProducto');

    form.addEventListener('submit', function (e) {
        e.preventDefault();

        const producto = {
            nombre: document.getElementById('nombre').value,
            categoria: document.getElementById('categoria').value,
            precio: document.getElementById('precio').value,
            cantidad: document.getElementById('cantidad').value,
            stockMinimo: document.getElementById('stock-minimo').value,
            proveedor: document.getElementById('preveedor').value,
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
                console.log("Respuesta del servidor:", data);
                alert("Producto registrado correctamente:\n" + JSON.stringify(data, null, 2));
                form.reset();
            })
            .catch(err => alert("Error: " + err.message));
    });
}
//document.addEventListener('DOMContentLoaded', registrarProducto);

async function obtenerProductos() {
    const tbody = document.querySelector("table tbody");
    tbody.innerHTML = "";

    try {
        const response = await fetch('api/productos/obtenerProductos');
        const productos = await response.json();

        productos.forEach(producto => {
            const fila = document.createElement("tr");
            fila.innerHTML = `
        <td>${producto.id}</td>
        <td>${producto.nombre}</td>
        <td>${producto.precio}</td>
        <td>${producto.cantidad}</td>
        <td>${producto.stockMinimo}</td>
        <td>${producto.categoria.nombre}</td>
        <td>${producto.proveedor.telefono}</td>

      `;
            tbody.appendChild(fila);
        });

    } catch (error) {
        console.error('Error al cargar producto:', error);
    }
}

document.addEventListener('DOMContentLoaded', obtenerProductos);

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
            cargarProductoes();
        })
        .catch(err => alert(err.message));
}
