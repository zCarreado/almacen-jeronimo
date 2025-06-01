package com.ciclomotos.ciclomotos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.ciclomotos.ciclomotos.model.Producto;
import com.ciclomotos.ciclomotos.service.ProductoService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    /**
     * POST /api/productos/crearProducto
     * Crea un nuevo producto.
     * JSON de entrada:
     * {
     *   "nombre": "Rines 26 aluminio",
     *   "precio": 50000.00,
     *   "cantidad": 10,
     *   "stockMinimo": 2,
     *   "categoria": { "id": 1 },
     *   "proveedor": { "id": 1 }
     * }
     * Respuesta: Producto creado (incluye el campo id).
     */
    @PostMapping("/crearProducto")
    public ResponseEntity<?> crearProducto(@Valid @RequestBody Producto producto) {
        try {
            Producto creado = productoService.crearProducto(producto);
            return ResponseEntity.ok(creado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear producto: " + e.getMessage());
        }
    }

    /**
     * GET /api/productos/obtenerProductos
     * Obtiene todos los productos.
     * Respuesta:
     * [
     *   {
     *     "id": 1,
     *     "nombre": "Rines 26 aluminio",
     *     "precio": 50000.00,
     *     "cantidad": 10,
     *     "stockMinimo": 2,
     *     "categoria": { "id": 1, "nombre": "Bicicletas" },
     *     "proveedor": { "id": 1, "nombre": "Proveedor X" }
     *   },
     *   ...
     * ]
     */
    @GetMapping("/obtenerProductos")
    public List<Producto> obtenerProductos() {
        return productoService.obtenerProductos();
    }

    /**
     * GET /api/productos/obtenerProducto/{id}
     * Obtiene un producto por su ID.
     * Respuesta:
     * {
     *   "id": 1,
     *   "nombre": "Rines 26 aluminio",
     *   "precio": 50000.00,
     *   "cantidad": 10,
     *   "stockMinimo": 2,
     *   "categoria": { "id": 1, "nombre": "Bicicletas" },
     *   "proveedor": { "id": 1, "nombre": "Proveedor X" }
     * }
     */
    @GetMapping("/obtenerProducto/{id}")
    public ResponseEntity<?> obtenerProductoPorId(@PathVariable Long id) {
        Producto producto = productoService.obtenerProductoPorId(id);
        if (producto != null) {
            return ResponseEntity.ok(producto);
        } else {
            return ResponseEntity.status(404).body("Producto no encontrado con id: " + id);
        }
    }

    /**
     * PUT /api/productos/actualizarProducto/{id}
     * Actualiza un producto existente.
     * JSON de entrada:
     * {
     *   "nombre": "Rines 26 aluminio",
     *   "precio": 52000.00,
     *   "cantidad": 15,
     *   "stockMinimo": 2,
     *   "categoria": { "id": 1 },
     *   "proveedor": { "id": 1 }
     * }
     * Respuesta: Producto actualizado.
     */
    @PutMapping("/actualizarProducto/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        try {
            Producto actualizado = productoService.actualizarProducto(id, producto);
            if (actualizado != null) {
                return ResponseEntity.ok(actualizado);
            } else {
                return ResponseEntity.status(404).body("No se encontró el producto con id: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar producto: " + e.getMessage());
        }
    }

    /**
     * DELETE /api/productos/eliminarProducto/{id}
     * Elimina un producto por su ID.
     * No requiere body.
     * Respuesta: Sin contenido (código 200 o 204).
     */
    @DeleteMapping("/eliminarProducto/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        Producto producto = productoService.obtenerProductoPorId(id);
        if (producto != null) {
            try {
                productoService.eliminarProducto(id);
                return ResponseEntity.ok("Producto eliminado correctamente.");
            } catch (DataIntegrityViolationException ex) {
                return ResponseEntity.status(409).body("No se puede eliminar el producto porque está asociado a ventas u otros registros.");
            } catch (Exception ex) {
                return ResponseEntity.status(500).body("Error al eliminar producto: " + ex.getMessage());
            }
        } else {
            return ResponseEntity.status(404).body("No se encontró el producto con id: " + id);
        }
    }

    /**
     * PUT /api/productos/agregarStock
     * Agrega stock a un producto existente.
     * JSON de entrada:
     * {
     *   "productoId": 1,
     *   "cantidad": 5
     * }
     * Respuesta: true o false
     */
    @PutMapping("/agregarStock")
    public ResponseEntity<?> agregarStock(@Valid @RequestBody AgregarStockRequest request) {
        try {
            boolean resultado = productoService.agregarStock(request.getProductoId(), request.getCantidad());
            if (resultado) {
                return ResponseEntity.ok(true);
            } else {
                return ResponseEntity.status(404).body("No se encontró el producto con id: " + request.getProductoId());
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al agregar stock: " + e.getMessage());
        }
    }

    /**
     * GET /api/productos/buscarPorNombre/{nombre}
     * Busca productos por nombre (búsqueda parcial, insensible a mayúsculas/minúsculas).
     * Respuesta: Igual que el GET de todos los productos, pero filtrado.
     */
    @GetMapping("/buscarPorNombre/{nombre}")
    public List<Producto> buscarPorNombre(@PathVariable String nombre) {
        return productoService.buscarPorNombre(nombre);
    }

    /**
     * POST /api/productos/agregarOActualizarPorNombre
     * Agrega un producto nuevo o actualiza el stock y precio si ya existe por nombre.
     * JSON de entrada:
     * {
     *   "nombre": "Rines 26 aluminio",
     *   "cantidad": 5,
     *   "precio": 52000
     * }
     * Respuesta: El producto creado o actualizado.
     */
    @PostMapping("/agregarOActualizarPorNombre")
    public ResponseEntity<?> agregarOActualizarPorNombre(@Valid @RequestBody Producto producto) {
        try {
            Producto resultado = productoService.agregarOActualizarProductoPorNombre(
                producto.getNombre(),
                producto.getCantidad(),
                producto.getPrecio()
            );
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al agregar o actualizar producto: " + e.getMessage());
        }
    }

    // DTO para agregar stock
    public static class AgregarStockRequest {
        @NotNull(message = "El id del producto es obligatorio")
        private Long productoId;

        @Min(value = 1, message = "La cantidad debe ser mayor que cero")
        private int cantidad;

        public Long getProductoId() { return productoId; }
        public void setProductoId(Long productoId) { this.productoId = productoId; }
        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }
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
