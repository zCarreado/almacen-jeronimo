document.getElementById('loginForm').addEventListener('submit', async function(e) {
    e.preventDefault();

    const usuario = document.getElementById('username').value;
    const contrasena = document.getElementById('password').value;

    try {
        const response = await fetch(`/usuarios/username/${usuario}`);

        if (!response.ok) {
            alert('Usuario no encontrado');
            return;
        }

        const usuarioData = await response.json();

        if (usuarioData.password === contrasena) {
            window.location.href = "/venta";
        } else {
            alert('Contraseña incorrecta');
        }
    } catch (error) {
        console.error('Error al obtener usuario:', error);
        alert('Error al conectar con el servidor');
    }
});
