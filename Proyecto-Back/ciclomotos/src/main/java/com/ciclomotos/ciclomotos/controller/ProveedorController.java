package com.ciclomotos.ciclomotos.controller;

import com.ciclomotos.ciclomotos.model.Producto;
import com.ciclomotos.ciclomotos.model.Proveedor;
import com.ciclomotos.ciclomotos.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Proveedor crearProveedor(@RequestBody Proveedor proveedor) {
        return proveedorService.crearProveedor(proveedor);
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
    public Optional<Proveedor> obtenerProveedorPorId(@PathVariable Long id) {
        return proveedorService.obtenerProveedorPorId(id);
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
    public List<Producto> obtenerProductosPorProveedor(@PathVariable Long id) {
        return proveedorService.obtenerProveedorPorId(id)
                .map(proveedor -> proveedor.getProductos())
                .orElse(List.of());
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
    public Proveedor actualizarProveedor(@PathVariable Long id, @RequestBody Proveedor proveedor) {
        return proveedorService.actualizarProveedor(id, proveedor);
    }

    /**
     * DELETE /api/proveedores/eliminarProveedor/{id}
     * Elimina un proveedor por su ID.
     * No requiere body.
     * Respuesta: Sin contenido (código 200 o 204).
     */
    @DeleteMapping("/eliminarProveedor/{id}")
    public void eliminarProveedor(@PathVariable Long id) {
        proveedorService.eliminarProveedor(id);
    }
}
