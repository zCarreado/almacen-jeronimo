function registrarProducto() {
    const form = document.getElementById('formProducto');

    form.addEventListener('submit', function (e) {
        e.preventDefault();
        // Validación de campos numéricos mayores que 0
        const precioInput = document.getElementById('precio');
        const cantidadInput = document.getElementById('cantidad');
        const stockMinimoInput = document.getElementById('stock-minimo');
        const precio = parseFloat(precioInput.value);
        const cantidad = parseInt(cantidadInput.value);
        const stockMinimo = parseInt(stockMinimoInput.value);
        let advertencia = '';
        if (precio <= 0) {
            advertencia += 'El precio debe ser mayor que 0.\n';
            precioInput.classList.add('is-invalid');
        } else {
            precioInput.classList.remove('is-invalid');
        }
        if (cantidad <= 0) {
            advertencia += 'La cantidad debe ser mayor que 0.\n';
            cantidadInput.classList.add('is-invalid');
        } else {
            cantidadInput.classList.remove('is-invalid');
        }
        if (stockMinimo <= 0) {
            advertencia += 'El stock mínimo debe ser mayor que 0.';
            stockMinimoInput.classList.add('is-invalid');
        } else {
            stockMinimoInput.classList.remove('is-invalid');
        }
        if (advertencia) {
            mostrarAdvertenciaValidacion(advertencia);
            return;
        }
        const idEditar = form.getAttribute('data-edit-id');
        const producto = {
            nombre: document.getElementById('nombre').value,
            categoria: { id: parseInt(document.getElementById('categoria').value) },
            precio: precio,
            cantidad: cantidad,
            stockMinimo: stockMinimo,
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

// Mostrar advertencia visual
function mostrarAdvertenciaValidacion(mensaje) {
    let alerta = document.getElementById('alerta-validacion');
    if (!alerta) {
        alerta = document.createElement('div');
        alerta.id = 'alerta-validacion';
        alerta.className = 'alert alert-danger mt-2';
        const form = document.getElementById('formProducto');
        form.parentNode.insertBefore(alerta, form);
    }
    alerta.textContent = mensaje;
    setTimeout(() => {
        if (alerta) alerta.remove();
    }, 4000);
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
    registrarBusquedaProducto(); // Nueva función para búsqueda
});

function registrarBusquedaProducto() {
    const formBuscar = document.getElementById('formBuscar');
    const inputBuscar = document.getElementById('buscarProducto');
    formBuscar.addEventListener('submit', async function (e) {
        e.preventDefault();
        const valor = inputBuscar.value.trim();
        if (!valor) {
            obtenerProductos();
            return;
        }
        let productos = [];
        try {
            if (/^\d+$/.test(valor)) { // Si es número, buscar por ID
                const res = await fetch(`/api/productos/obtenerProducto/${valor}`);
                if (res.ok) {
                    const prod = await res.json();
                    productos = [prod];
                } else {
                    productos = [];
                }
            } else { // Si es texto, buscar por nombre
                const res = await fetch(`/api/productos/buscarPorNombre/${encodeURIComponent(valor)}`);
                if (res.ok) {
                    productos = await res.json();
                }
            }
            mostrarProductosEnTabla(productos);
        } catch (err) {
            alert('Error al buscar producto: ' + err.message);
        }
    });
    // Opción para limpiar búsqueda al borrar el input
    inputBuscar.addEventListener('input', function () {
        if (!inputBuscar.value.trim()) obtenerProductos();
    });
}

function mostrarProductosEnTabla(productos) {
    const tbody = document.querySelector('table tbody');
    tbody.innerHTML = '';
    if (!productos || productos.length === 0) {
        const fila = document.createElement('tr');
        fila.innerHTML = '<td colspan="7" class="text-center">No se encontraron productos</td>';
        tbody.appendChild(fila);
        return;
    }
    productos.forEach(producto => {
        const fila = document.createElement('tr');
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
}

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
