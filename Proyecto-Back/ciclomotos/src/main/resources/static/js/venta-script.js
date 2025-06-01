let productosCargados = [];

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


document.addEventListener('DOMContentLoaded', obtenerProductos);

function agregarAlCarrito(producto) {
    const carritoBody = document.getElementById('carritoBody');

    const filaExistente = carritoBody.querySelector(`tr[data-id='${producto.id}']`);
    if (filaExistente) {

        const cantidadInput = filaExistente.querySelector('.cantidad-input');
        cantidadInput.value = parseInt(cantidadInput.value) + 1;
        actualizarSubtotalFila(filaExistente);
        return;
    }

    // Crear una nueva fila para el producto
    const fila = document.createElement("tr");
    fila.setAttribute("data-id", producto.id);

    const cantidad = 1;
    const precioUnitario = producto.precio;
    const subtotal = cantidad * precioUnitario;
    const iva = subtotal * 0.19;
    const total = subtotal + iva;

    fila.innerHTML = `
        <td class="id-producto">${producto.id}</td>
        <td>${producto.nombre}</td>
        <td>
            <input type="number" class="cantidad-input" min="1" value="${cantidad}" style="width: 60px;">
        </td>
        <td class="precio-producto">${precioUnitario.toFixed(2)}</td>
        <td class="subtotal">${subtotal.toFixed(2)}</td>
        <td class="iva">${iva.toFixed(2)}</td>
        <td class="total">${total.toFixed(2)}</td>
    `;

    carritoBody.appendChild(fila);

    const inputCantidad = fila.querySelector('.cantidad-input');
    inputCantidad.addEventListener('input', () => {
        if (parseInt(inputCantidad.value) < 1) inputCantidad.value = 1;
        actualizarSubtotalFila(fila);
    });
}


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
});

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


function crearVenta() {
    const form = document.getElementById('formVenta');

    form.addEventListener('submit', async function (e) {
        e.preventDefault();

        const clienteId = parseInt(document.getElementById('cliente').value);
        const detalles = obtenerDetallesDeTabla();

        if (!clienteId || detalles.length === 0) {
            alert('Debes seleccionar un cliente y al menos un producto.');
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

            if (!response.ok) {
                const errorText = await response.text();
                console.error("Error:", errorText);
                throw new Error('Error al registrar la venta');
            }

            const ventaRegistrada = await response.json();

            form.reset();
            document.querySelector('#tablaCarrito tbody').innerHTML = '';

            crearFactura(ventaRegistrada.id);

            return ventaRegistrada;

        } catch (error) {
            console.error('Error al enviar la venta:', error);
            alert('Ocurrió un error al registrar la venta');
        }
    });
}
document.addEventListener('DOMContentLoaded', crearVenta);

function obtenerDetallesDeTabla() {
    const filas = document.querySelectorAll("#tablaCarrito tbody tr");
    const detalles = [];

    filas.forEach(fila => {
        const productoId = parseInt(fila.querySelector(".id-producto").textContent);
        const productoCompleto = productosCargados.find(p => p.id === productoId);

        if (!productoCompleto) {
            console.warn("Producto no encontrado con ID:", productoId);
            return;
        }

        const cantidad = parseInt(fila.querySelector(".cantidad-input").value);
        const precioUnitario = parseFloat(fila.querySelector(".precio-producto").textContent);
        const subtotal = cantidad * precioUnitario;

        detalles.push({
            producto: productoCompleto,
            cantidad: cantidad,
            precioUnitario: precioUnitario,
            subtotal: subtotal
        });
    });

    return detalles;
}

document.addEventListener('DOMContentLoaded', crearVenta);
function obtenerClientes() {
    fetch('/api/clientes/obtenerClientes')
        .then(response => {
            if (!response.ok) throw new Error('Error al obtener clientes');
            return response.json();
        })
        .then(clientes => {
            const select = document.getElementById('cliente');
            select.innerHTML = '<option value="" disabled selected>Seleccione un cliente</option>';

            clientes.forEach(cliente => {
                const option = document.createElement('option');
                option.value = cliente.id;
                option.textContent = cliente.nombre;
                select.appendChild(option);
            });
        })
        .catch(e => {
            console.error('Error al cargar los clientes:', e);
        });
}

document.addEventListener("DOMContentLoaded", obtenerClientes);

async function crearFactura(idVenta) {
    try {
        const response = await fetch(`/api/ventas/factura-pdf/${idVenta}`, {
            method: 'GET'
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error('Error al crear factura: ' + errorText);
        }

        // Obtener el PDF como blob
        const pdfBlob = await response.blob();

        // Crear un URL para el blob
        const url = window.URL.createObjectURL(pdfBlob);

        // Abrir el PDF en una nueva pestaña o ventana
        window.open(url);

    } catch (error) {
        console.error('Error en crearFactura:', error);
        alert('Error al crear factura');
    }
}


