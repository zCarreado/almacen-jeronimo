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

        fetch('/usuarios/crearUsuario', {
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

function obtenerTipoUsuarios() {
    fetch('/api/tipoUsuario')
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
document.addEventListener("DOMContentLoaded", obtenerTipoUsuarios());

function obtenerUsuarioPorId(id) {
    fetch(`/api/usuarios/obtenerUsuario/${id}`)
        .then(res => {
            if (!res.ok) throw new Error('Usuario no encontrado');
            return res.json();
        })
        .then(data => {
            alert("Usuario:\n" + JSON.stringify(data, null, 2));
        })
        .catch(err => alert(err.message));
}
function actualizarUsuario(id) {

    const usuarioActualizado = {
        nombre: document.getElementById('nombre').value,
        email: document.getElementById('correo').value,
        telefono: document.getElementById('telefono').value,
        direccion: document.getElementById('direccion').value,
    };
    fetch(`/api/usuarios/actualizarUsuario/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(usuarioActualizado)
    })
        .then(res => {
            if (!res.ok) throw new Error('Error al actualizar usuario');
            return res.json();
        })
        .then(data => {
            alert("Usuario actualizado:\n" + JSON.stringify(data, null, 2));
        })
        .catch(err => alert(err.message));
}
function eliminarUsuario(id) {
    fetch(`/api/usuarios/eliminarUsuario/${id}`, {
        method: 'DELETE'
    })
        .then(res => {
            if (!res.ok) throw new Error('Error al eliminar usuario');
            alert("Usuario eliminado con éxito");
        })
        .catch(err => alert(err.message));
}