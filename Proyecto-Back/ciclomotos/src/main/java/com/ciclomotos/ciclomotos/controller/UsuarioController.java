package com.ciclomotos.ciclomotos.controller;

import com.ciclomotos.ciclomotos.model.Usuario;
import com.ciclomotos.ciclomotos.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;


    @PostMapping("/crearUsuario")
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        Usuario creado = usuarioService.crearUsuario(usuario);
        return ResponseEntity.ok(creado);
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
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody Usuario usuario) {
        try {
            Usuario creado = usuarioService.crearUsuario(usuario);
            return ResponseEntity.ok(creado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear usuario: " + e.getMessage());
        }
    }

    @GetMapping("/obtenerUsuarios")
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
    public ResponseEntity<?> obtenerUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(id);
        return usuario.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body("Usuario no encontrado con id: " + id));
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
    public ResponseEntity<?> obtenerUsuarioPorUsername(@PathVariable String username) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorUsername(username);
        return usuario.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body("Usuario no encontrado con username: " + username));
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
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody Usuario usuario) {
        try {
            Usuario actualizado = usuarioService.actualizarUsuario(id, usuario);
            if (actualizado != null) {
                return ResponseEntity.ok(actualizado);
            } else {
                return ResponseEntity.status(404).body("No se encontró el usuario con id: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar usuario: " + e.getMessage());
        }
    }

    /**
     * DELETE /usuarios/{id}
     * Elimina un usuario por su ID.
     * No requiere body.
     * Respuesta: Sin contenido (código 204).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(id);
        if (usuario.isPresent()) {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.ok("Usuario eliminado correctamente.");
        } else {
            return ResponseEntity.status(404).body("No se encontró el usuario con id: " + id);
        }
    }

    // Manejo global de errores de validación
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errores = new StringBuilder("Errores de validación: ");
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errores.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
        }
        return ResponseEntity.badRequest().body(errores.toString());
    }
}
