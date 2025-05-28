function registrarUsuario() {
    const form = document.getElementById('formUsuario');

    form.addEventListener('submit', function (e) {
        e.preventDefault();

        const usuario = {
            nombre: document.getElementById('nombre').value,
            username: document.getElementById('usuario').value,
            password: document.getElementById('contraseña').value,
            tipo: document.getElementById('tipo').value,
        };

        fetch('/api/usuarios/crearUsuario', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(usuario)
        })
            .then(res => {
                if (!res.ok) throw new Error('Error al registrar usuario');
                return res.json();
            })
            .then(data => {
                alert("Usuario registrado correctamente:\n" + JSON.stringify(data, null, 2));
                form.reset();
            })
            .catch(err => alert("Error: " + err.message));
    });
}
document.addEventListener('DOMContentLoaded', registrarUsuario);

function obtenerUsuarios() {
    fetch('/api/usuarios/tipoUsuario')
        .then(response => {
            if (!response.ok) throw new Error('Error al obtener tipos');
            return response.json();
        })
        .then(tipos => {
            const select = document.getElementById('tipo');
            tipos.forEach(tipo => {
                const option = document.createElement('option');
                option.value = tipo;
                option.textContent = tipo.charAt(0).toUpperCase() + tipo.slice(1).toLowerCase();
                select.appendChild(option);
            });
        })
        .catch(e => {
            console.error('Error al cargar los tipos:', e);
        });
}
document.addEventListener('DOMContentLoaded', obtenerUsuarios);
