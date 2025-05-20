package com.ciclomotos.ciclomotos.service;

import com.ciclomotos.ciclomotos.model.DetalleVenta;
import com.ciclomotos.ciclomotos.model.Producto;
import com.ciclomotos.ciclomotos.model.Venta;
import com.ciclomotos.ciclomotos.repository.VentaRepository;
import com.ciclomotos.ciclomotos.repository.DetalleVentaRepository;
import com.ciclomotos.ciclomotos.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    public Venta crearVenta(Venta venta, List<DetalleVenta> detalles) {
        // Verificar y descontar stock
        boolean stockOk = productoService.verificarYDescontarStock(detalles);
        if (!stockOk) {
            throw new RuntimeException("Stock insuficiente o se viola el stock mínimo para uno o más productos.");
        }
        // Guardar la venta y los detalles
        Venta ventaGuardada = ventaRepository.save(venta);
        for (DetalleVenta detalle : detalles) {
            detalle.setVenta(ventaGuardada);
            detalleVentaRepository.save(detalle);
        }
        return ventaGuardada;
    }

    public List<Venta> obtenerVentas() {
        return ventaRepository.findAll();
    }

    public Optional<Venta> obtenerVentaPorId(Long id) {
        return ventaRepository.findById(id);
    }

    public Venta actualizarVenta(Long id, Venta venta) {
        if (ventaRepository.existsById(id)) {
            venta.setId(id);
            return ventaRepository.save(venta);
        }
        return null;
    }

    public void eliminarVenta(Long id) {
        ventaRepository.deleteById(id);
    }
}
