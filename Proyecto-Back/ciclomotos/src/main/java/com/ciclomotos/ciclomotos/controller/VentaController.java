package com.ciclomotos.ciclomotos.controller;

import com.ciclomotos.ciclomotos.model.Venta;
import com.ciclomotos.ciclomotos.model.DetalleVenta;
import com.ciclomotos.ciclomotos.service.VentaService;
import com.ciclomotos.ciclomotos.controller.dto.VentaConDetallesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    /**
     * POST /api/ventas/crearVenta
     * Crea una venta con sus detalles.
     * JSON de entrada:
     * {
     *   "venta": {
     *     "fecha": "2025-05-30T14:30:00",
     *     "cliente": { "id": 1 }
     *   },
     *   "detalles": [
     *     {
     *       "producto": { "id": 10 },
     *       "cantidad": 2
     *     },
     *     {
     *       "producto": { "id": 11 },
     *       "cantidad": 1
     *     }
     *   ]
     * }
     * Respuesta (JSON de salida):
     * {
     *   "id": 100,
     *   "fecha": "2025-05-30T14:30:00",
     *   "subtotal": 150000.00,
     *   "iva": 28500.00,
     *   "total": 178500.00,
     *   "cliente": {
     *     "id": 1,
     *     "nombre": "Juan Pérez"
     *   },
     *   "detalles": [
     *     {
     *       "idProducto": 10,
     *       "nombreProducto": "Rines 26 aluminio",
     *       "precioUnitario": 50000.00,
     *       "cantidad": 2,
     *       "subtotal": 100000.00
     *     },
     *     {
     *       "idProducto": 11,
     *       "nombreProducto": "Cadena reforzada",
     *       "precioUnitario": 50000.00,
     *       "cantidad": 1,
     *       "subtotal": 50000.00
     *     }
     *   ]
     * }
     */
    @PostMapping("/crearVenta")
    public VentaConDetallesResponse crearVenta(@RequestBody VentaConDetallesRequest request) {
        Venta ventaCreada = ventaService.crearVenta(request.getVenta(), request.getDetalles());
        return ventaService.obtenerVentaSimplePorId(ventaCreada.getId());
    }

    /**
     * GET /api/ventas/obtenerVentas
     * Obtiene todas las ventas con detalles simplificados.
     * Respuesta (JSON de salida):
     * [
     *   {
     *     "id": 100,
     *     "fecha": "2025-05-30T14:30:00",
     *     "subtotal": 150000.00,
     *     "iva": 28500.00,
     *     "total": 178500.00,
     *     "cliente": {
     *       "id": 1,
     *       "nombre": "Juan Pérez"
     *     },
     *     "detalles": [
     *       {
     *         "idProducto": 10,
     *         "nombreProducto": "Rines 26 aluminio",
     *         "precioUnitario": 50000.00,
     *         "cantidad": 2,
     *         "subtotal": 100000.00
     *       }
     *     ]
     *   },
     *   ...
     * ]
     */
    @GetMapping("/obtenerVentas")
    public List<VentaConDetallesResponse> obtenerVentas() {
        return ventaService.obtenerTodasVentasSimples();
    }

    /**
     * GET /api/ventas/obtenerVenta/{id}
     * Obtiene una venta por su ID (incluye todos los campos de la entidad Venta).
     * Respuesta (JSON de salida):
     * {
     *   "id": 100,
     *   "fecha": "2025-05-30T14:30:00",
     *   "subtotal": 150000.00,
     *   "iva": 28500.00,
     *   "total": 178500.00,
     *   "cliente": { ... },
     *   "detalles": [ ... ]
     * }
     */
    @GetMapping("/obtenerVenta/{id}")
    public Optional<Venta> obtenerVentaPorId(@PathVariable Long id) {
        return ventaService.obtenerVentaPorId(id);
    }

    /**
     * GET /api/ventas/obtenerVentaSimple/{id}
     * Obtiene una venta simplificada por su ID (formato igual al de crearVenta).
     * Respuesta (JSON de salida):
     * {
     *   "id": 100,
     *   "fecha": "2025-05-30T14:30:00",
     *   "subtotal": 150000.00,
     *   "iva": 28500.00,
     *   "total": 178500.00,
     *   "cliente": {
     *     "id": 1,
     *     "nombre": "Juan Pérez"
     *   },
     *   "detalles": [
     *     {
     *       "idProducto": 10,
     *       "nombreProducto": "Rines 26 aluminio",
     *       "precioUnitario": 50000.00,
     *       "cantidad": 2,
     *       "subtotal": 100000.00
     *     }
     *   ]
     * }
     */
    @GetMapping("/obtenerVentaSimple/{id}")
    public VentaConDetallesResponse obtenerVentaSimplePorId(@PathVariable Long id) {
        return ventaService.obtenerVentaSimplePorId(id);
    }

    /**
     * PUT /api/ventas/actualizarVenta/{id}
     * Actualiza una venta existente.
     * JSON de entrada:
     * {
     *   "fecha": "2025-05-30T14:30:00",
     *   "cliente": { "id": 1 },
     *   "total": 178500.00
     * }
     * Respuesta: Venta actualizada (igual formato que GET /obtenerVenta/{id}).
     */
    @PutMapping("/actualizarVenta/{id}")
    public Venta actualizarVenta(@PathVariable Long id, @RequestBody Venta venta) {
        return ventaService.actualizarVenta(id, venta);
    }

    /**
     * DELETE /api/ventas/eliminarVenta/{id}
     * Elimina una venta por su ID.
     * No requiere body.
     * Respuesta: Sin contenido (código 204).
     */
    @DeleteMapping("/eliminarVenta/{id}")
    public void eliminarVenta(@PathVariable Long id) {
        ventaService.eliminarVenta(id);
    }
}

// DTO para recibir la venta y sus detalles en una sola petición
/**
 * Ejemplo de JSON de entrada para crear una venta:
 * {
 *   "venta": {
 *     "fecha": "2025-05-30T14:30:00",
 *     "cliente": { "id": 1 }
 *   },
 *   "detalles": [
 *     {
 *       "producto": { "id": 10 },
 *       "cantidad": 2
 *     }
 *   ]
 * }
 */
class VentaConDetallesRequest {
    private Venta venta;
    private List<DetalleVenta> detalles;

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }
}
