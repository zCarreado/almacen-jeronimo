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

    // Crear un producto
    @PostMapping("/crearProducto")
    public Producto crearProducto(@RequestBody Producto producto) {
        return productoService.crearProducto(producto);
    }

    // Obtener todos los productos
    @GetMapping("/obtenerProductos")
    public List<Producto> obtenerProductos() {
        return productoService.obtenerProductos();
    }

    // Obtener un producto por ID
    @GetMapping("/obtenerProducto/{id}")
    public Producto obtenerProductoPorId(@PathVariable Long id) {
        return productoService.obtenerProductoPorId(id);
    }

    // Actualizar un producto
    @PutMapping("/actualizarProducto/{id}")
    public Producto actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        return productoService.actualizarProducto(id, producto);
    }

    // Eliminar un producto
    @DeleteMapping("/eliminarProducto/{id}")
    public void eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
    }

    // Registrar una compra
    @PostMapping("/registrarCompra")
    public boolean registrarCompra(@RequestParam Long productoId, @RequestParam int cantidad, @RequestParam(required = false) String observaciones) {
        return productoService.registrarCompra(productoId, cantidad, observaciones);
    }

    @PostMapping("/registrarCompraMultiple")
    public boolean registrarCompraMultiple(@RequestBody List<CompraRequest> compras) {
        boolean allOk = true;
        for (CompraRequest compra : compras) {
            boolean ok = productoService.registrarCompra(compra.getProductoId(), compra.getCantidad(), compra.getObservaciones());
            if (!ok) allOk = false;
        }
        return allOk;
    }

    @GetMapping("/alertaStockBajo")
    public List<Producto> alertaStockBajo() {
        return productoService.obtenerProductosConStockBajo();
    }

    // DTO para compras múltiples
    public static class CompraRequest {
        private Long productoId;
        private int cantidad;
        private String observaciones;
        public Long getProductoId() { return productoId; }
        public void setProductoId(Long productoId) { this.productoId = productoId; }
        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }
        public String getObservaciones() { return observaciones; }
        public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    }
}
