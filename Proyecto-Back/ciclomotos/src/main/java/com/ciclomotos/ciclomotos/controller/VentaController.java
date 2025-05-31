package com.ciclomotos.ciclomotos.controller;

import com.ciclomotos.ciclomotos.model.Venta;
import com.ciclomotos.ciclomotos.model.DetalleVenta;
import com.ciclomotos.ciclomotos.service.FacturaPdfService;
import com.ciclomotos.ciclomotos.service.VentaService;
import com.ciclomotos.ciclomotos.controller.dto.VentaConDetallesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private FacturaPdfService facturaPdfService;

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
    public ResponseEntity<?> crearVenta(@Valid @RequestBody VentaConDetallesRequest request) {
        try {
            Venta ventaCreada = ventaService.crearVenta(request.getVenta(), request.getDetalles());
            VentaConDetallesResponse response = ventaService.obtenerVentaSimplePorId(ventaCreada.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear venta: " + e.getMessage());
        }
    }

    @GetMapping("/factura-pdf/{id}")
    public ResponseEntity<?> descargarFacturaPdf(@PathVariable Long id) {
        try {
            Venta venta = ventaService.obtenerVentaPorId(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

            byte[] pdfBytes = facturaPdfService.generarFacturaPdf(venta);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("factura_" + venta.getId() + ".pdf").build());

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error al generar el PDF: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
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
    public ResponseEntity<?> obtenerVentaPorId(@PathVariable Long id) {
        Optional<Venta> venta = ventaService.obtenerVentaPorId(id);
        return venta.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body("Venta no encontrada con id: " + id));
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
    public ResponseEntity<?> obtenerVentaSimplePorId(@PathVariable Long id) {
        VentaConDetallesResponse response = ventaService.obtenerVentaSimplePorId(id);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(404).body("Venta no encontrada con id: " + id);
        }
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
    public ResponseEntity<?> actualizarVenta(@PathVariable Long id, @Valid @RequestBody Venta venta) {
        try {
            Venta actualizada = ventaService.actualizarVenta(id, venta);
            if (actualizada != null) {
                return ResponseEntity.ok(actualizada);
            } else {
                return ResponseEntity.status(404).body("No se encontró la venta con id: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar venta: " + e.getMessage());
        }
    }

    /**
     * DELETE /api/ventas/eliminarVenta/{id}
     * Elimina una venta por su ID.
     * No requiere body.
     * Respuesta: Sin contenido (código 204).
     */
    @DeleteMapping("/eliminarVenta/{id}")
    public ResponseEntity<?> eliminarVenta(@PathVariable Long id) {
        Optional<Venta> venta = ventaService.obtenerVentaPorId(id);
        if (venta.isPresent()) {
            ventaService.eliminarVenta(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(404).body("No se encontró la venta con id: " + id);
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

    // DTO para recibir la venta y sus detalles en una sola petición
    public static class VentaConDetallesRequest {
        
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
}
