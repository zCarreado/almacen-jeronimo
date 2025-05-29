package com.ciclomotos.ciclomotos.service;

import com.ciclomotos.ciclomotos.controller.dto.VentaConDetallesResponse;
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
        // Calcular subtotal de cada detalle y tomar el precio unitario del producto
        for (DetalleVenta detalle : detalles) {
            if (detalle.getProducto() != null && detalle.getProducto().getId() != null && detalle.getCantidad() != null) {
                Producto producto = productoService.obtenerProductoPorId(detalle.getProducto().getId());
                if (producto == null || producto.getPrecio() == null) {
                    throw new RuntimeException("No se encontró el producto o el precio es nulo para el producto con id: " + detalle.getProducto().getId());
                }
                detalle.setPrecioUnitario(producto.getPrecio());
                detalle.setSubtotal(producto.getPrecio().multiply(new java.math.BigDecimal(detalle.getCantidad())));
            } else {
                throw new RuntimeException("Producto o cantidad no puede ser nulo en el detalle de venta.");
            }
        }
        // Calcular el total de la venta
        java.math.BigDecimal totalVenta = detalles.stream()
            .map(DetalleVenta::getSubtotal)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        venta.setTotal(totalVenta);

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

    public VentaConDetallesResponse obtenerVentaSimplePorId(Long id) {
        Venta venta = obtenerVentaPorId(id).orElse(null);
        if (venta == null) return null;
        VentaConDetallesResponse dto = new VentaConDetallesResponse();
        dto.setId(venta.getId());
        dto.setFecha(venta.getFecha());
        dto.setTotal(venta.getTotal());
        // Cliente simple
        VentaConDetallesResponse.ClienteSimple clienteDto = new VentaConDetallesResponse.ClienteSimple();
        clienteDto.setId(venta.getCliente().getId());
        clienteDto.setNombre(venta.getCliente().getNombre());
        dto.setCliente(clienteDto);
        // Detalles simples
        java.util.List<VentaConDetallesResponse.DetalleVentaSimple> detallesDto = new java.util.ArrayList<>();
        if (venta.getDetalles() != null) {
            for (DetalleVenta d : venta.getDetalles()) {
                VentaConDetallesResponse.DetalleVentaSimple detDto = new VentaConDetallesResponse.DetalleVentaSimple();
                detDto.setNombreProducto(d.getProducto().getNombre());
                detDto.setPrecioUnitario(d.getPrecioUnitario());
                detDto.setCantidad(d.getCantidad());
                detDto.setSubtotal(d.getSubtotal());
                detallesDto.add(detDto);
            }
        }
        dto.setDetalles(detallesDto);
        return dto;
    }
}
