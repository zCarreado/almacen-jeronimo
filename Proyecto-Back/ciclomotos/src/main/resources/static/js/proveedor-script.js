function registrarProveedor() {
    const form = document.getElementById('formProveedor');

    form.addEventListener('submit', function (e) {
        e.preventDefault();
        // Validaciones
        const nombre = document.getElementById('nombre').value.trim();
        const correo = document.getElementById('correo').value.trim();
        const telefono = document.getElementById('telefono').value.trim();
        let advertencia = '';
        if (!nombre) advertencia += 'El nombre es obligatorio.\n';
        const regexCorreo = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!correo) {
            advertencia += 'El correo es obligatorio.\n';
        } else if (!regexCorreo.test(correo)) {
            advertencia += 'El correo no tiene un formato válido.\n';
        }
        const regexTelefono = /^\d{7,12}$/;
        if (!telefono) {
            advertencia += 'El teléfono es obligatorio.\n';
        } else if (!regexTelefono.test(telefono)) {
            advertencia += 'El teléfono debe contener solo números (7 a 12 dígitos).\n';
        }
        if (advertencia) {
            mostrarAdvertenciaProveedor(advertencia);
            return;
        }
        const esEdicion = form.getAttribute('data-edit-id');
        const proveedor = {
            nombre: document.getElementById('nombre').value,
            email: document.getElementById('correo').value,
            telefono: document.getElementById('telefono').value,
        };
        let url = '/api/proveedores/crearProveedor';
        let method = 'POST';
        if (esEdicion) {
            url = `/api/proveedores/actualizarProveedor/${esEdicion}`;
            method = 'PUT';
        }
        fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(proveedor)
        })
            .then(res => {
                if (!res.ok) throw new Error('Error al ' + (esEdicion ? 'actualizar' : 'registrar') + ' proveedor');
                return res.json();
            })
            .then(data => {
                alert("Proveedor " + (esEdicion ? "actualizado" : "registrado") + " correctamente:\n");
                form.reset();
                form.removeAttribute('data-edit-id');
                document.getElementById('btnProveedor').textContent = 'Registrar Proveedor';
                obtenerProveedores();
            })
            .catch(err => alert("Error: " + err.message));
    });
}
document.addEventListener('DOMContentLoaded', registrarProveedor);

async function obtenerProveedores() {
    const tbody = document.querySelector("table tbody");
    tbody.innerHTML = "";

    try {
        const response = await fetch('api/proveedores/obtenerProveedores');
        const proveedores = await response.json();

        proveedores.forEach(proveedor => {
            const fila = document.createElement("tr");
            fila.innerHTML = `
        <td>${proveedor.id}</td>
        <td>${proveedor.nombre}</td>
        <td>${proveedor.email}</td>
        <td>${proveedor.telefono}</td>
        <td>
          <button class="btn btn-warning btn-sm" onclick="editarProveedor('${proveedor.id}')">Editar</button>
        </td>
      `;
            tbody.appendChild(fila);
        });

    } catch (error) {
        console.error('Error al cargar proveedor:', error);
    }
}

document.addEventListener('DOMContentLoaded', obtenerProveedores);

function obtenerProveedorPorId(id) {
    fetch(`/api/proveedores/obtenerProveedor/${id}`)
        .then(res => {
            if (!res.ok) throw new Error('proveedor no encontrado');
            return res.json();
        })
        .then(data => {
            alert("Proveedor:\n");
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
        .then(data => alert('Proveedor actualizado:\n'))
        .catch(err => alert(err.message));
}

// Función para buscar proveedores
function registrarBusquedaProveedor() {
    const formBuscar = document.getElementById('formBuscarProveedor');
    const inputBuscar = document.getElementById('buscarProveedor');
    formBuscar.addEventListener('submit', async function (e) {
        e.preventDefault();
        const valor = inputBuscar.value.trim();
        if (!valor) {
            obtenerProveedores();
            return;
        }
        let proveedores = [];
        try {
            // Buscar por ID si es número
            if (/^\d+$/.test(valor)) {
                const res = await fetch(`/api/proveedores/obtenerProveedor/${valor}`);
                if (res.ok) {
                    const prov = await res.json();
                    proveedores = [prov];
                }
            } else {
                // Buscar por nombre o correo (filtrado en frontend)
                const res = await fetch('/api/proveedores/obtenerProveedores');
                if (res.ok) {
                    const todos = await res.json();
                    proveedores = todos.filter(p =>
                        p.nombre.toLowerCase().includes(valor.toLowerCase()) ||
                        p.email.toLowerCase().includes(valor.toLowerCase())
                    );
                }
            }
            mostrarProveedoresEnTabla(proveedores);
        } catch (err) {
            mostrarProveedoresEnTabla([]);
        }
    });
    inputBuscar.addEventListener('input', function () {
        if (!inputBuscar.value.trim()) obtenerProveedores();
    });
}

document.addEventListener('DOMContentLoaded', registrarBusquedaProveedor);

// Función para mostrar proveedores en la tabla
function mostrarProveedoresEnTabla(proveedores) {
    const tbody = document.querySelector("table tbody");
    tbody.innerHTML = "";
    if (!proveedores || proveedores.length === 0) {
        const fila = document.createElement("tr");
        fila.innerHTML = '<td colspan="5" class="text-center">No se encontraron proveedores</td>';
        tbody.appendChild(fila);
        return;
    }
    proveedores.forEach(proveedor => {
        const fila = document.createElement("tr");
        fila.innerHTML = `
            <td>${proveedor.id}</td>
            <td>${proveedor.nombre}</td>
            <td>${proveedor.email}</td>
            <td>${proveedor.telefono}</td>
            <td>
                <button class="btn btn-warning btn-sm" onclick="editarProveedor('${proveedor.id}')">Editar</button>
            </td>
        `;
        tbody.appendChild(fila);
    });
}

// Función para editar proveedor
function editarProveedor(id) {
    fetch(`/api/proveedores/obtenerProveedor/${id}`)
        .then(res => {
            if (!res.ok) throw new Error('Proveedor no encontrado');
            return res.json();
        })
        .then(proveedor => {
            document.getElementById('nombre').value = proveedor.nombre;
            document.getElementById('correo').value = proveedor.email;
            document.getElementById('telefono').value = proveedor.telefono;
            document.getElementById('formProveedor').setAttribute('data-edit-id', id);
            document.getElementById('btnProveedor').textContent = 'Actualizar';
        })
        .catch(err => alert(err.message));
}

// Mostrar advertencia visual para proveedor
function mostrarAdvertenciaProveedor(mensaje) {
    let alerta = document.getElementById('alerta-validacion-proveedor');
    if (!alerta) {
        alerta = document.createElement('div');
        alerta.id = 'alerta-validacion-proveedor';
        alerta.className = 'alert alert-danger mt-2';
        const form = document.getElementById('formProveedor');
        form.parentNode.insertBefore(alerta, form);
    }
    alerta.textContent = mensaje;
    setTimeout(() => {
        if (alerta) alerta.remove();
    }, 4000);
}
