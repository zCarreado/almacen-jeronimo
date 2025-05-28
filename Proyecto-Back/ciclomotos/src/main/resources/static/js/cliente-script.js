document.getElementById('formCliente').addEventListener('submit', function (e) {
    e.preventDefault();

    const cliente = {
        nombre: document.getElementById('nombre').value,
        apellido: document.getElementById('apellido').value,
        correo: document.getElementById('correo').value,
        telefono: document.getElementById('telefono').value,
        direccion: document.getElementById('direccion').value,
        contraseña: document.getElementById('contraseña').value
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
            alert("Cliente registrado correctamente" + JSON.stringify(data, null, 2));
            document.getElementById('formCliente').reset();
        })
        .catch(err => alert(err.message));
});