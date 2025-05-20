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

    @PostMapping("/crearProveedor")
    public Proveedor crearProveedor(@RequestBody Proveedor proveedor) {
        return proveedorService.crearProveedor(proveedor);
    }

    @GetMapping("/obtenerProveedores")
    public List<Proveedor> obtenerProveedores() {
        return proveedorService.obtenerProveedores();
    }

    @GetMapping("/obtenerProveedor/{id}")
    public Optional<Proveedor> obtenerProveedorPorId(@PathVariable Long id) {
        return proveedorService.obtenerProveedorPorId(id);
    }

    @GetMapping("/productos/{id}")
    public List<Producto> obtenerProductosPorProveedor(@PathVariable Long id) {
        return proveedorService.obtenerProveedorPorId(id)
                .map(proveedor -> proveedor.getProductos())
                .orElse(List.of());
    }

    @PutMapping("/actualizarProveedor/{id}")
    public Proveedor actualizarProveedor(@PathVariable Long id, @RequestBody Proveedor proveedor) {
        return proveedorService.actualizarProveedor(id, proveedor);
    }

    @DeleteMapping("/eliminarProveedor/{id}")
    public void eliminarProveedor(@PathVariable Long id) {
        proveedorService.eliminarProveedor(id);
    }
}
