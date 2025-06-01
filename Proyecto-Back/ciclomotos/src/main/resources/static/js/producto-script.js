function registrarProducto() {
    const form = document.getElementById('formProducto');

    form.addEventListener('submit', function (e) {
        e.preventDefault();
        const idEditar = form.getAttribute('data-edit-id');
        const producto = {
            nombre: document.getElementById('nombre').value,
            categoria: { id: parseInt(document.getElementById('categoria').value) },
            precio: document.getElementById('precio').value,
            cantidad: document.getElementById('cantidad').value,
            stockMinimo: document.getElementById('stock-minimo').value,
            proveedor: { id: parseInt(document.getElementById('preveedor').value) },
        };
        let url = '/api/productos/crearProducto';
        let method = 'POST';
        if (idEditar) {
            url = `/api/productos/actualizarProducto/${idEditar}`;
            method = 'PUT';
        }
        fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(producto)
        })
            .then(res => {
                if (!res.ok) throw new Error('Error al registrar/actualizar producto');
                return res.json();
            })
            .then(data => {
                alert("Producto " + (idEditar ? "actualizado" : "registrado") + " correctamente:\n" + JSON.stringify(data, null, 2));
                form.reset();
                form.removeAttribute('data-edit-id');
                obtenerProductos();
            })
            .catch(err => alert("Error: " + err.message));
    });
}

async function obtenerProductos() {
    const tbody = document.querySelector("table tbody");
    tbody.innerHTML = "";

    try {
        const response = await fetch('/api/productos/obtenerProductos');
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
        <td>
            <button class="btn btn-warning btn-sm me-2" onclick="editarProducto(${producto.id})">Editar</button>
        </td>
      `;
            tbody.appendChild(fila);
        });

    } catch (error) {
        console.error('Error al cargar producto:', error);
    }
}

async function cargarCategoriasYProveedores() {
    // Cargar categorías
    const selectCategoria = document.getElementById('categoria');
    selectCategoria.innerHTML = '<option value="">Seleccione una categoría</option>';
    try {
        const resCat = await fetch('/api/categorias/obtenerCategorias');
        const categorias = await resCat.json();
        categorias.forEach(cat => {
            const option = document.createElement('option');
            option.value = cat.id;
            option.textContent = cat.nombre;
            selectCategoria.appendChild(option);
        });
    } catch (e) {
        selectCategoria.innerHTML = '<option value="">Error al cargar categorías</option>';
    }
    // Cargar proveedores
    const selectProveedor = document.getElementById('preveedor');
    selectProveedor.innerHTML = '<option value="">Seleccione un proveedor</option>';
    try {
        const resProv = await fetch('/api/proveedores/obtenerProveedores');
        const proveedores = await resProv.json();
        proveedores.forEach(prov => {
            const option = document.createElement('option');
            option.value = prov.id;
            option.textContent = prov.nombre + (prov.telefono ? ' (' + prov.telefono + ')' : '');
            selectProveedor.appendChild(option);
        });
    } catch (e) {
        selectProveedor.innerHTML = '<option value="">Error al cargar proveedores</option>';
    }
}

document.addEventListener('DOMContentLoaded', function () {
    cargarCategoriasYProveedores();
    obtenerProductos();
    registrarProducto();
});

function editarProducto(id) {
    fetch(`/api/productos/obtenerProducto/${id}`)
        .then(res => {
            if (!res.ok) throw new Error('producto no encontrado');
            return res.json();
        })
        .then(producto => {
            document.getElementById('nombre').value = producto.nombre;
            document.getElementById('categoria').value = producto.categoria.id;
            document.getElementById('precio').value = producto.precio;
            document.getElementById('cantidad').value = producto.cantidad;
            document.getElementById('stock-minimo').value = producto.stockMinimo;
            document.getElementById('preveedor').value = producto.proveedor.id;
            document.getElementById('formProducto').setAttribute('data-edit-id', id);
        })
        .catch(err => alert(err.message));
}

function eliminarProducto(id) {
    if (!confirm('¿Seguro que deseas eliminar este producto?')) return;
    fetch(`/api/productos/eliminarProducto/${id}`, {
        method: 'DELETE'
    })
        .then(res => {
            if (!res.ok) throw new Error('Error al eliminar producto');
            alert('Producto eliminado correctamente');
            obtenerProductos();
        })
        .catch(err => alert(err.message));
}
