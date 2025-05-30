package com.ciclomotos.ciclomotos.controller;

import com.ciclomotos.ciclomotos.model.Usuario;
import com.ciclomotos.ciclomotos.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    /**
     * POST /usuarios
     * Crea un nuevo usuario.
     * JSON de entrada:
     * {
     *   "nombre": "Juan Pérez",
     *   "username": "juanperez",
     *   "password": "clave123",
     *   "tipo": "ADMIN" // o "EMPLEADO"
     * }
     * Respuesta (JSON de salida):
     * {
     *   "id": 1,
     *   "nombre": "Juan Pérez",
     *   "username": "juanperez",
     *   "password": "clave123",
     *   "tipo": "ADMIN"
     * }
     */
    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        Usuario creado = usuarioService.crearUsuario(usuario);
        return ResponseEntity.ok(creado);
    }

    /**
     * GET /usuarios
     * Obtiene todos los usuarios.
     * Respuesta (JSON de salida):
     * [
     *   {
     *     "id": 1,
     *     "nombre": "Juan Pérez",
     *     "username": "juanperez",
     *     "password": "clave123",
     *     "tipo": "ADMIN"
     *   },
     *   ...
     * ]
     */
    @GetMapping
    public List<Usuario> obtenerUsuarios() {
        return usuarioService.obtenerUsuarios();
    }

    /**
     * GET /usuarios/{id}
     * Obtiene un usuario por su ID.
     * Respuesta (JSON de salida):
     * {
     *   "id": 1,
     *   "nombre": "Juan Pérez",
     *   "username": "juanperez",
     *   "password": "clave123",
     *   "tipo": "ADMIN"
     * }
     */
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(id);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * GET /usuarios/username/{username}
     * Obtiene un usuario por su username.
     * Respuesta (JSON de salida):
     * {
     *   "id": 1,
     *   "nombre": "Juan Pérez",
     *   "username": "juanperez",
     *   "password": "clave123",
     *   "tipo": "ADMIN"
     * }
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<Usuario> obtenerUsuarioPorUsername(@PathVariable String username) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorUsername(username);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * PUT /usuarios/{id}
     * Actualiza un usuario existente.
     * JSON de entrada:
     * {
     *   "nombre": "Juan Pérez",
     *   "username": "juanperez",
     *   "password": "nuevaClave",
     *   "tipo": "EMPLEADO"
     * }
     * Respuesta (JSON de salida):
     * {
     *   "id": 1,
     *   "nombre": "Juan Pérez",
     *   "username": "juanperez",
     *   "password": "nuevaClave",
     *   "tipo": "EMPLEADO"
     * }
     */
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        Usuario actualizado = usuarioService.actualizarUsuario(id, usuario);
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /usuarios/{id}
     * Elimina un usuario por su ID.
     * No requiere body.
     * Respuesta: Sin contenido (código 204).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
