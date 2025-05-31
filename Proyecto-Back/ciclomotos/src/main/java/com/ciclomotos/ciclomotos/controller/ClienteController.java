package com.ciclomotos.ciclomotos.controller;

import com.ciclomotos.ciclomotos.model.Cliente;
import com.ciclomotos.ciclomotos.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    /**
     * POST /api/clientes/crearCliente
     * Crea un nuevo cliente.
     * JSON de entrada:
     * {
     *   "id": 1234567890,
     *   "nombre": "Juan Pérez",
     *   "telefono": "3001234567",
     *   "direccion": "Calle 123"
     * }
     * Respuesta: Cliente creado (incluye el campo id).
     */
    @PostMapping("/crearCliente")
    public ResponseEntity<?> crearCliente(@Valid @RequestBody Cliente cliente) {
        try {
            Cliente creado = clienteService.crearCliente(cliente);
            return ResponseEntity.ok(creado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear cliente: " + e.getMessage());
        }
    }

    /**
     * GET /api/clientes/obtenerClientes
     * Obtiene todos los clientes.
     * Respuesta:
     * [
     *   {
     *     "id": 1234567890,
     *     "nombre": "Juan Pérez",
     *     "telefono": "3001234567",
     *     "direccion": "Calle 123"
     *   },
     *   ...
     * ]
     */
    @GetMapping("/obtenerClientes")
    public List<Cliente> obtenerClientes() {
        return clienteService.obtenerClientes();
    }

    /**
     * GET /api/clientes/obtenerCliente/{id}
     * Obtiene un cliente por su ID.
     * Respuesta:
     * {
     *   "id": 1234567890,
     *   "nombre": "Juan Pérez",
     *   "telefono": "3001234567",
     *   "direccion": "Calle 123"
     * }
     */
    @GetMapping("/obtenerCliente/{id}")
    public ResponseEntity<?> obtenerClientePorId(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteService.obtenerClientePorId(id);
        if (cliente.isPresent()) {
            return ResponseEntity.ok(cliente.get());
        } else {
            return ResponseEntity.status(404).body("Cliente no encontrado con id: " + id);
        }
    }

    /**
     * PUT /api/clientes/actualizarCliente/{id}
     * Actualiza un cliente existente.
     * JSON de entrada:
     * {
     *   "nombre": "Juan Pérez",
     *   "telefono": "3009876543",
     *   "direccion": "Nueva dirección"
     * }
     * Respuesta: Cliente actualizado.
     */
    @PutMapping("/actualizarCliente/{id}")
    public ResponseEntity<?> actualizarCliente(@PathVariable Long id, @Valid @RequestBody Cliente cliente) {
        try {
            Cliente actualizado = clienteService.actualizarCliente(id, cliente);
            if (actualizado != null) {
                return ResponseEntity.ok(actualizado);
            } else {
                return ResponseEntity.status(404).body("No se encontró el cliente con id: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar cliente: " + e.getMessage());
        }
    }

    /**
     * DELETE /api/clientes/eliminarCliente/{id}
     * Elimina un cliente por su ID.
     * No requiere body.
     * Respuesta: Sin contenido (código 200 o 204).
     */
    @DeleteMapping("/eliminarCliente/{id}")
    public ResponseEntity<?> eliminarCliente(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteService.obtenerClientePorId(id);
        if (cliente.isPresent()) {
            clienteService.eliminarCliente(id);
            return ResponseEntity.ok("Cliente eliminado correctamente.");
        } else {
            return ResponseEntity.status(404).body("No se encontró el cliente con id: " + id);
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
