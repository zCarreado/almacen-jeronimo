package com.ciclomotos.ciclomotos.service;

import com.ciclomotos.ciclomotos.model.DetalleVenta;
import com.ciclomotos.ciclomotos.model.Producto;
import com.ciclomotos.ciclomotos.model.MovimientoInventario;
import com.ciclomotos.ciclomotos.repository.ProductoRepository;
import com.ciclomotos.ciclomotos.repository.MovimientoInventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private MovimientoInventarioRepository movimientoInventarioRepository;

    @Transactional
    public boolean verificarYDescontarStock(List<DetalleVenta> detalles) {
        for (DetalleVenta detalle : detalles) {
            Producto producto = productoRepository.findById(detalle.getProducto().getId()).orElse(null);
            if (producto == null || producto.getCantidad() < detalle.getCantidad() || (producto.getCantidad() - detalle.getCantidad()) < producto.getStockMinimo()) {
                return false; // No hay suficiente stock o se viola el stock mínimo
            }
        }
        // Si todos los productos tienen stock suficiente, descontar y registrar movimiento
        for (DetalleVenta detalle : detalles) {
            Producto producto = productoRepository.findById(detalle.getProducto().getId()).orElse(null);
            producto.setCantidad(producto.getCantidad() - detalle.getCantidad());
            productoRepository.save(producto);
            // Registrar movimiento de inventario
            MovimientoInventario mov = new MovimientoInventario();
            mov.setProducto(producto);
            mov.setFecha(LocalDateTime.now());
            mov.setTipoMovimiento(MovimientoInventario.TipoMovimiento.SALIDA);
            mov.setCantidad(detalle.getCantidad());
            mov.setObservaciones("Venta realizada");
            movimientoInventarioRepository.save(mov);
        }
        return true;
    }

    @Transactional
    public boolean registrarCompra(Long productoId, int cantidad, String observaciones) {
        Producto producto = productoRepository.findById(productoId).orElse(null);
        if (producto == null) {
            return false;
        }
        producto.setCantidad(producto.getCantidad() + cantidad);
        productoRepository.save(producto);
        // Registrar movimiento de inventario
        MovimientoInventario mov = new MovimientoInventario();
        mov.setProducto(producto);
        mov.setFecha(LocalDateTime.now());
        mov.setTipoMovimiento(MovimientoInventario.TipoMovimiento.ENTRADA);
        mov.setCantidad(cantidad);
        mov.setObservaciones(observaciones != null ? observaciones : "Compra registrada");
        movimientoInventarioRepository.save(mov);
        return true;
    }

    public Producto crearProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public List<Producto> obtenerProductos() {
        return productoRepository.findAll();
    }

    public Producto obtenerProductoPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    public Producto actualizarProducto(Long id, Producto producto) {
        if (productoRepository.existsById(id)) {
            producto.setId(id);
            return productoRepository.save(producto);
        }
        return null;
    }

    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }

    public List<Producto> obtenerProductosConStockBajo() {
        return productoRepository.findAll().stream()
                .filter(p -> p.getCantidad() != null && p.getStockMinimo() != null && p.getCantidad() <= p.getStockMinimo())
                .toList();
    }
}
