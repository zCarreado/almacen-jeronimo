package com.ciclomotos.ciclomotos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ciclomotos.ciclomotos.model.Producto;
import com.ciclomotos.ciclomotos.service.ProductoService;

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
    public Producto crearProducto(@RequestBody Producto producto) {
        return productoService.crearProducto(producto);
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
    public Producto obtenerProductoPorId(@PathVariable Long id) {
        return productoService.obtenerProductoPorId(id);
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
    public Producto actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        return productoService.actualizarProducto(id, producto);
    }

    /**
     * DELETE /api/productos/eliminarProducto/{id}
     * Elimina un producto por su ID.
     * No requiere body.
     * Respuesta: Sin contenido (código 200 o 204).
     */
    @DeleteMapping("/eliminarProducto/{id}")
    public void eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
    }

    /**
     * POST /api/productos/registrarCompra
     * Registra una compra de un producto (aumenta el stock).
     * Parámetros (form-data o URL):
     *   productoId: 1
     *   cantidad: 5
     *   observaciones: "Compra de reposición"
     * Respuesta: true o false
     */
    @PostMapping("/registrarCompra")
    public boolean registrarCompra(@RequestParam Long productoId, @RequestParam int cantidad, @RequestParam(required = false) String observaciones) {
        return productoService.registrarCompra(productoId, cantidad, observaciones);
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
    public boolean agregarStock(@RequestBody AgregarStockRequest request) {
        return productoService.agregarStock(request.getProductoId(), request.getCantidad());
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
    public Producto agregarOActualizarPorNombre(@RequestBody Producto producto) {
        return productoService.agregarOActualizarProductoPorNombre(
            producto.getNombre(),
            producto.getCantidad(),
            producto.getPrecio()
        );
    }

    // DTO para agregar stock
    public static class AgregarStockRequest {
        private Long productoId;
        private int cantidad;
        public Long getProductoId() { return productoId; }
        public void setProductoId(Long productoId) { this.productoId = productoId; }
        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    }
}
