package com.ciclomotos.ciclomotos.controller;

import com.ciclomotos.ciclomotos.model.Venta;
import com.ciclomotos.ciclomotos.model.DetalleVenta;
import com.ciclomotos.ciclomotos.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @PostMapping("/crearVenta")
    public Venta crearVenta(@RequestBody VentaConDetallesRequest request) {
        return ventaService.crearVenta(request.getVenta(), request.getDetalles());
    }

    @GetMapping("/obtenerVentas")
    public List<Venta> obtenerVentas() {
        return ventaService.obtenerVentas();
    }

    @GetMapping("/obtenerVenta/{id}")
    public Optional<Venta> obtenerVentaPorId(@PathVariable Long id) {
        return ventaService.obtenerVentaPorId(id);
    }

    @PutMapping("/actualizarVenta/{id}")
    public Venta actualizarVenta(@PathVariable Long id, @RequestBody Venta venta) {
        return ventaService.actualizarVenta(id, venta);
    }

    @DeleteMapping("/eliminarVenta/{id}")
    public void eliminarVenta(@PathVariable Long id) {
        ventaService.eliminarVenta(id);
    }
}

// DTO para recibir la venta y sus detalles en una sola petición
class VentaConDetallesRequest {
    private Venta venta;
    private List<DetalleVenta> detalles;
    public Venta getVenta() { return venta; }
    public void setVenta(Venta venta) { this.venta = venta; }
    public List<DetalleVenta> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVenta> detalles) { this.detalles = detalles; }
}
