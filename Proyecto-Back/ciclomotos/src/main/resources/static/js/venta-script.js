let productosCargados = [];
let listaClientes = [];

async function obtenerProductos() {
    const tbody = document.querySelector("#formProducto table tbody");
    tbody.innerHTML = "";

    try {
        const response = await fetch('api/productos/obtenerProductos');
        const productos = await response.json();

        productosCargados = productos;

        productos.forEach(producto => {
            const fila = document.createElement("tr");
            fila.innerHTML = `
                <td>${producto.id}</td>
                <td>${producto.nombre}</td>
                <td>${producto.precio}</td>
                <td>${producto.cantidad}</td>
                <td><input type="number" min="1" value="1" class="col-md-8"></td>
                <td>${producto.categoria ? producto.categoria.nombre : 'Sin categoría'}</td>
                <td><button type="button" class="btn col-md-12 btn-agregar btn-primary bg-dark border-0">Agregar</button></td>
            `;
            tbody.appendChild(fila);
        });

    } catch (error) {
        console.error('Error al cargar producto:', error);
    }
}

function filtrarProductos() {
    const valor = document.getElementById('buscarProducto').value.trim().toLowerCase();
    const tbody = document.querySelector("#formProducto table tbody");

    const productosFiltrados = productosCargados.filter(p =>
        p.nombre.toLowerCase().includes(valor) || p.id.toString() === valor
    );

    tbody.innerHTML = "";

    productosFiltrados.forEach(producto => {
        const fila = document.createElement("tr");
        fila.innerHTML = `
            <td>${producto.id}</td>
            <td>${producto.nombre}</td>
            <td>${producto.precio}</td>
            <td>${producto.cantidad}</td>
            <td><input type="number" min="1" value="1" class="col-md-8"></td>
            <td>${producto.categoria ? producto.categoria.nombre : 'Sin categoría'}</td>
            <td><button type="button" class="btn col-md-12 btn-agregar btn-primary bg-dark border-0">Agregar</button></td>
        `;
        tbody.appendChild(fila);
    });
}

function agregarAlCarrito(producto) {
    const carritoBody = document.getElementById('carritoBody');
    const filaExistente = carritoBody.querySelector(`tr[data-id='${producto.id}']`);

    if (filaExistente) {
        const cantidadInput = filaExistente.querySelector('.cantidad-input');
        cantidadInput.value = parseInt(cantidadInput.value) + producto.cantidad;
        actualizarSubtotalFila(filaExistente);
        validarFormularioVenta();
        return;
    }

    const fila = document.createElement("tr");
    fila.setAttribute("data-id", producto.id);

    const cantidad = producto.cantidad;
    const precioUnitario = producto.precio;
    const subtotal = cantidad * precioUnitario;
    const iva = subtotal * 0.19;
    const total = subtotal + iva;

    fila.innerHTML = `
        <td class="id-producto">${producto.id}</td>
        <td>${producto.nombre}</td>
        <td><input type="number" class="cantidad-input" min="1" value="${cantidad}" style="width: 60px;"></td>
        <td class="precio-producto">${precioUnitario.toFixed(2)}</td>
        <td class="subtotal">${subtotal.toFixed(2)}</td>
        <td class="iva">${iva.toFixed(2)}</td>
        <td class="total">${total.toFixed(2)}</td>
        <td><button class="btn btn-sm btn-danger btn-eliminar">🗑️</button></td>
    `;

    carritoBody.appendChild(fila);

    fila.querySelector('.cantidad-input').addEventListener('input', () => {
        actualizarSubtotalFila(fila);
        validarFormularioVenta();
    });

    validarFormularioVenta();
}

function actualizarSubtotalFila(fila) {
    const cantidad = parseInt(fila.querySelector('.cantidad-input').value);
    const precioUnitario = parseFloat(fila.querySelector('.precio-producto').textContent);
    const subtotal = cantidad * precioUnitario;
    const iva = subtotal * 0.19;
    const total = subtotal + iva;

    fila.querySelector('.subtotal').textContent = subtotal.toFixed(2);
    fila.querySelector('.iva').textContent = iva.toFixed(2);
    fila.querySelector('.total').textContent = total.toFixed(2);
}

function obtenerClientes() {
    fetch('/api/clientes/obtenerClientes')
        .then(response => response.json())
        .then(clientes => {
            listaClientes = clientes;

            const input = document.getElementById('clienteBuscar');
            const resultado = document.getElementById('clienteResultado');
            const lista = document.getElementById('clienteResultados');

            input.addEventListener('input', () => {
                const valor = input.value.trim().toLowerCase();
                lista.innerHTML = '';
                resultado.textContent = '';

                if (!valor) {
                    delete resultado.dataset.clienteId;
                    validarFormularioVenta();
                    return;
                }

                const coincidencias = listaClientes.filter(c =>
                    c.id.toString().startsWith(valor) || c.nombre.toLowerCase().includes(valor)
                );

                if (coincidencias.length === 0) {
                    const noItem = document.createElement('li');
                    noItem.className = 'list-group-item disabled';
                    noItem.textContent = 'No se encontraron coincidencias';
                    lista.appendChild(noItem);
                    delete resultado.dataset.clienteId;
                    validarFormularioVenta();
                    return;
                }

                coincidencias.forEach(cliente => {
                    const item = document.createElement('li');
                    item.className = 'list-group-item list-group-item-action';
                    item.textContent = `${cliente.id} - ${cliente.nombre}`;
                    item.addEventListener('click', () => {
                        input.value = cliente.nombre;
                        resultado.textContent = `Cliente seleccionado: ${cliente.nombre}`;
                        resultado.dataset.clienteId = cliente.id;
                        resultado.classList.remove('text-danger');
                        resultado.classList.add('text-success');
                        lista.innerHTML = '';
                        validarFormularioVenta();
                    });
                    lista.appendChild(item);
                });
            });
        })
        .catch(e => console.error('Error al obtener clientes:', e));
}

function crearVenta() {
    const form = document.getElementById('formVenta');

    form.addEventListener('submit', async function (e) {
        e.preventDefault();

        const clienteId = parseInt(document.getElementById('clienteResultado').dataset.clienteId || 0);
        const detalles = obtenerDetallesDeTabla();

        if (!clienteId) {
            alert('Debes ingresar un cliente válido antes de realizar la venta.');
            return;
        }

        if (detalles.length === 0) {
            alert('Debes agregar al menos un producto al carrito.');
            return;
        }

        const ventaParaEnviar = {
            venta: {
                fecha: new Date().toISOString(),
                cliente: { id: clienteId }
            },
            detalles: detalles.map(item => ({
                producto: { id: item.producto.id },
                cantidad: item.cantidad
            }))
        };

        try {
            const response = await fetch('/api/ventas/crearVenta', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(ventaParaEnviar)
            });

            if (!response.ok) throw new Error('Error al registrar la venta');

            const ventaRegistrada = await response.json();
            alert('Venta registrada correctamente:\n');

            form.reset();
            document.querySelector('#tablaCarrito tbody').innerHTML = '';
            document.getElementById('clienteBuscar').value = '';
            document.getElementById('clienteResultado').textContent = '';
            delete document.getElementById('clienteResultado').dataset.clienteId;

            validarFormularioVenta();
            crearFactura(ventaRegistrada.id);
            await obtenerProductos();

        } catch (error) {
            console.error('Error al enviar la venta:', error);
            alert('Ocurrió un error al registrar la venta');
        }
    });
}

function obtenerDetallesDeTabla() {
    const filas = document.querySelectorAll("#tablaCarrito tbody tr");
    const detalles = [];

    filas.forEach(fila => {
        const productoId = parseInt(fila.querySelector(".id-producto").textContent);
        const productoCompleto = productosCargados.find(p => p.id === productoId);
        const cantidad = parseInt(fila.querySelector(".cantidad-input").value);
        const precioUnitario = parseFloat(fila.querySelector(".precio-producto").textContent);
        const subtotal = cantidad * precioUnitario;

        detalles.push({
            producto: productoCompleto,
            cantidad,
            precioUnitario,
            subtotal
        });
    });

    return detalles;
}

function validarFormularioVenta() {
    const clienteValido = !!document.getElementById('clienteResultado').dataset.clienteId;
    const productosEnCarrito = document.querySelectorAll("#tablaCarrito tbody tr").length > 0;
    document.getElementById('btnRealizarVenta').disabled = !(clienteValido && productosEnCarrito);
}

async function crearFactura(idVenta) {
    try {
        const response = await fetch(`/api/ventas/factura-pdf/${idVenta}`);
        if (!response.ok) throw new Error('Error al generar factura');

        const pdfBlob = await response.blob();
        const url = window.URL.createObjectURL(pdfBlob);
        window.open(url);
    } catch (error) {
        console.error('Error en crearFactura:', error);
        alert('Error al crear factura');
    }
}

document.addEventListener('DOMContentLoaded', () => {
    obtenerProductos();
    obtenerClientes();
    crearVenta();
    document.getElementById('buscarProducto').addEventListener('input', filtrarProductos);
});

document.addEventListener('click', function (e) {
    if (e.target.classList.contains('btn-agregar')) {
        const fila = e.target.closest('tr');
        const producto = {
            id: parseInt(fila.children[0].textContent),
            nombre: fila.children[1].textContent,
            precio: parseFloat(fila.children[2].textContent),
            cantidad: parseInt(fila.querySelector('input[type="number"]').value)
        };
        agregarAlCarrito(producto);
    }

    if (e.target.classList.contains('btn-eliminar')) {
        const fila = e.target.closest('tr');
        fila.remove();
        validarFormularioVenta();
    }

    // Cierra la lista de clientes si se hace clic fuera
    const lista = document.getElementById('clienteResultados');
    const input = document.getElementById('clienteBuscar');
    if (!input.contains(e.target) && !lista.contains(e.target)) {
        lista.innerHTML = '';
    }
});
