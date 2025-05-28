document.getElementById('formProveedor').addEventListener('submit', function (e) {
    e.preventDefault();

    const proveedor = {
        nombre: document.getElementById('nombre').value,
        correo: document.getElementById('correo').value,
        telefono: document.getElementById('telefono').value,
        direccion: document.getElementById('direccion').value
    };

    fetch('api/proveedores/crearProveedor', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(proveedor)
    })
        .then(res => {
            if (!res.ok) throw new Error('Error al registrar proveedor');
            return res.json();
        })
        .then(data => {
            alert("Proveedor registrado:\n" + JSON.stringify(data, null, 2));
            document.getElementById('formProveedor').reset();
        })
        .catch(err => alert(err.message));
});
