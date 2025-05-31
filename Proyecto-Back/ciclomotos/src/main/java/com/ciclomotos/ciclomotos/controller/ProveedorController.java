package com.ciclomotos.ciclomotos.controller;

import com.ciclomotos.ciclomotos.model.Producto;
import com.ciclomotos.ciclomotos.model.Proveedor;
import com.ciclomotos.ciclomotos.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    /**
     * POST /api/proveedores/crearProveedor
     * Crea un nuevo proveedor.
     * JSON de entrada:
     * {
     *   "nombre": "Proveedor X",
     *   "email": "proveedorx@email.com",
     *   "telefono": "3001234567"
     * }
     * Respuesta (JSON de salida):
     * {
     *   "id": 1,
     *   "nombre": "Proveedor X",
     *   "email": "proveedorx@email.com",
     *   "telefono": "3001234567",
     *   "productos": []
     * }
     */
    @PostMapping("/crearProveedor")
    public ResponseEntity<?> crearProveedor(@Valid @RequestBody Proveedor proveedor) {
        try {
            Proveedor creado = proveedorService.crearProveedor(proveedor);
            return ResponseEntity.ok(creado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear proveedor: " + e.getMessage());
        }
    }

    /**
     * GET /api/proveedores/obtenerProveedores
     * Obtiene todos los proveedores.
     * Respuesta (JSON de salida):
     * [
     *   {
     *     "id": 1,
     *     "nombre": "Proveedor X",
     *     "email": "proveedorx@email.com",
     *     "telefono": "3001234567",
     *     "productos": [
     *       {
     *         "id": 10,
     *         "nombre": "Rines 26 aluminio",
     *         "precio": 50000.00,
     *         "cantidad": 10,
     *         "stockMinimo": 2,
     *         "categoria": { "id": 1, "nombre": "Bicicletas" },
     *         "proveedor": { "id": 1, "nombre": "Proveedor X", "email": "proveedorx@email.com", "telefono": "3001234567" }
     *       },
     *       ...
     *     ]
     *   },
     *   ...
     * ]
     */
    @GetMapping("/obtenerProveedores")
    public List<Proveedor> obtenerProveedores() {
        return proveedorService.obtenerProveedores();
    }

    /**
     * GET /api/proveedores/obtenerProveedor/{id}
     * Obtiene un proveedor por su ID.
     * Respuesta (JSON de salida):
     * {
     *   "id": 1,
     *   "nombre": "Proveedor X",
     *   "email": "proveedorx@email.com",
     *   "telefono": "3001234567",
     *   "productos": [
     *     {
     *       "id": 10,
     *       "nombre": "Rines 26 aluminio",
     *       "precio": 50000.00,
     *       "cantidad": 10,
     *       "stockMinimo": 2,
     *       "categoria": { "id": 1, "nombre": "Bicicletas" },
     *       "proveedor": { "id": 1, "nombre": "Proveedor X", "email": "proveedorx@email.com", "telefono": "3001234567" }
     *     },
     *     ...
     *   ]
     * }
     */
    @GetMapping("/obtenerProveedor/{id}")
    public ResponseEntity<?> obtenerProveedorPorId(@PathVariable Long id) {
        Optional<Proveedor> proveedor = proveedorService.obtenerProveedorPorId(id);
        if (proveedor.isPresent()) {
            return ResponseEntity.ok(proveedor.get());
        } else {
            return ResponseEntity.status(404).body("Proveedor no encontrado con id: " + id);
        }
    }

    /**
     * GET /api/proveedores/productos/{id}
     * Obtiene los productos de un proveedor por su ID.
     * Respuesta (JSON de salida):
     * [
     *   {
     *     "id": 10,
     *     "nombre": "Rines 26 aluminio",
     *     "precio": 50000.00,
     *     "cantidad": 10,
     *     "stockMinimo": 2,
     *     "categoria": { "id": 1, "nombre": "Bicicletas" },
     *     "proveedor": { "id": 1, "nombre": "Proveedor X", "email": "proveedorx@email.com", "telefono": "3001234567" }
     *   },
     *   ...
     * ]
     */
    @GetMapping("/productos/{id}")
    public ResponseEntity<?> obtenerProductosPorProveedor(@PathVariable Long id) {
        Optional<Proveedor> proveedor = proveedorService.obtenerProveedorPorId(id);
        if (proveedor.isPresent()) {
            return ResponseEntity.ok(proveedor.get().getProductos());
        } else {
            return ResponseEntity.status(404).body("Proveedor no encontrado con id: " + id);
        }
    }

    /**
     * PUT /api/proveedores/actualizarProveedor/{id}
     * Actualiza un proveedor existente.
     * JSON de entrada:
     * {
     *   "nombre": "Proveedor X",
     *   "email": "proveedorx@email.com",
     *   "telefono": "3001234567"
     * }
     * Respuesta (JSON de salida):
     * {
     *   "id": 1,
     *   "nombre": "Proveedor X",
     *   "email": "proveedorx@email.com",
     *   "telefono": "3001234567",
     *   "productos": [...]
     * }
     */
    @PutMapping("/actualizarProveedor/{id}")
    public ResponseEntity<?> actualizarProveedor(@PathVariable Long id, @Valid @RequestBody Proveedor proveedor) {
        try {
            Proveedor actualizado = proveedorService.actualizarProveedor(id, proveedor);
            if (actualizado != null) {
                return ResponseEntity.ok(actualizado);
            } else {
                return ResponseEntity.status(404).body("No se encontró el proveedor con id: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar proveedor: " + e.getMessage());
        }
    }

    /**
     * DELETE /api/proveedores/eliminarProveedor/{id}
     * Elimina un proveedor por su ID.
     * No requiere body.
     * Respuesta: Sin contenido (código 200 o 204).
     */
    @DeleteMapping("/eliminarProveedor/{id}")
    public ResponseEntity<?> eliminarProveedor(@PathVariable Long id) {
        Optional<Proveedor> proveedor = proveedorService.obtenerProveedorPorId(id);
        if (proveedor.isPresent()) {
            proveedorService.eliminarProveedor(id);
            return ResponseEntity.ok("Proveedor eliminado correctamente.");
        } else {
            return ResponseEntity.status(404).body("No se encontró el proveedor con id: " + id);
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
