package com.ciclomotos.ciclomotos.controller;

import com.ciclomotos.ciclomotos.model.Factura;
import com.ciclomotos.ciclomotos.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    //esta no se esta usando, pero la dejo por si acaso

    @Autowired
    private FacturaService facturaService;

    @PostMapping("/crearFactura")
    public Factura crearFactura(@RequestBody Factura factura) {
        return facturaService.crearFactura(factura);
    }

    @GetMapping("/obtenerFacturas")
    public List<Factura> obtenerFacturas() {
        return facturaService.obtenerFacturas();
    }

    @GetMapping("/obtenerFactura/{id}")
    public Optional<Factura> obtenerFacturaPorId(@PathVariable Long id) {
        return facturaService.obtenerFacturaPorId(id);
    }

    @PutMapping("/actualizarFactura/{id}")
    public Factura actualizarFactura(@PathVariable Long id, @RequestBody Factura factura) {
        return facturaService.actualizarFactura(id, factura);
    }

    @DeleteMapping("/eliminarFactura/{id}")
    public void eliminarFactura(@PathVariable Long id) {
        facturaService.eliminarFactura(id);
    }
}
