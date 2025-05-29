package com.ciclomotos.ciclomotos.service;

import com.ciclomotos.ciclomotos.model.DetalleVenta;
import com.ciclomotos.ciclomotos.model.Producto;
import com.ciclomotos.ciclomotos.model.MovimientoInventario;
import com.ciclomotos.ciclomotos.repository.ProductoRepository;
import com.ciclomotos.ciclomotos.repository.MovimientoInventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductoService {

    private static final Logger logger = LoggerFactory.getLogger(ProductoService.class);

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private MovimientoInventarioRepository movimientoInventarioRepository;

    @Transactional
    public boolean verificarYDescontarStock(List<DetalleVenta> detalles) {
        logger.info("Iniciando verificación y descuento de stock para {} detalles", detalles.size());
        for (DetalleVenta detalle : detalles) {
            logger.info("Verificando producto con ID {}: cantidad solicitada = {}", detalle.getProducto().getId(), detalle.getCantidad());
            Producto producto = productoRepository.findById(detalle.getProducto().getId()).orElse(null);
            if (producto == null) {
                logger.warn("Producto con ID {} no encontrado", detalle.getProducto().getId());
                return false;
            }
            if (producto.getCantidad() < detalle.getCantidad()) {
                logger.warn("Stock insuficiente para producto {}. Stock actual: {}, solicitado: {}", producto.getId(), producto.getCantidad(), detalle.getCantidad());
                return false;
            }
            if ((producto.getCantidad() - detalle.getCantidad()) < producto.getStockMinimo()) {
                logger.warn("Descuento violaría el stock mínimo para producto {}. Stock actual: {}, mínimo: {}, solicitado: {}", producto.getId(), producto.getCantidad(), producto.getStockMinimo(), detalle.getCantidad());
                return false;
            }
        }
        // Si todos los productos tienen stock suficiente, descontar y registrar movimiento
        for (DetalleVenta detalle : detalles) {
            Producto producto = productoRepository.findById(detalle.getProducto().getId()).orElse(null);
            int cantidadAnterior = producto.getCantidad();
            producto.setCantidad(producto.getCantidad() - detalle.getCantidad());
            productoRepository.save(producto);
            logger.info("Descontado {} unidades del producto {}. Stock anterior: {}, nuevo stock: {}", detalle.getCantidad(), producto.getId(), cantidadAnterior, producto.getCantidad());
            // Registrar movimiento de inventario
            MovimientoInventario mov = new MovimientoInventario();
            mov.setProducto(producto);
            mov.setFecha(LocalDateTime.now());
            mov.setTipoMovimiento(MovimientoInventario.TipoMovimiento.SALIDA);
            mov.setCantidad(detalle.getCantidad());
            mov.setObservaciones("Venta realizada");
            movimientoInventarioRepository.save(mov);
            logger.info("Movimiento de inventario registrado para producto {}: {} unidades descontadas", producto.getId(), detalle.getCantidad());
        }
        logger.info("Verificación y descuento de stock completados exitosamente");
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

    @Transactional
    public boolean agregarStock(Long productoId, int cantidad) {
        Producto producto = productoRepository.findById(productoId).orElse(null);
        if (producto == null || cantidad <= 0) {
            return false;
        }
        if (producto.getCantidad() == null) {
            producto.setCantidad(0);
        }
        producto.setCantidad(producto.getCantidad() + cantidad);
        productoRepository.save(producto);
        // Registrar movimiento de inventario
        MovimientoInventario mov = new MovimientoInventario();
        mov.setProducto(producto);
        mov.setFecha(java.time.LocalDateTime.now());
        mov.setTipoMovimiento(MovimientoInventario.TipoMovimiento.ENTRADA);
        mov.setCantidad(cantidad);
        mov.setObservaciones("Stock sumado manualmente");
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

    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }
}
