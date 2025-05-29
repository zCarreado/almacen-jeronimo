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
import java.util.ArrayList;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

public Venta crearVenta(Venta venta, List<DetalleVenta> detalles) {
    for (DetalleVenta detalle : detalles) {
        if (detalle.getProducto() != null && detalle.getProducto().getId() != null && detalle.getCantidad() != null) {
            Producto producto = productoService.obtenerProductoPorId(detalle.getProducto().getId());
            if (producto == null || producto.getPrecio() == null) {
                throw new RuntimeException("No se encontró el producto o el precio es nulo para el producto con id: " + detalle.getProducto().getId());
            }
            detalle.setProducto(producto);
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(producto.getPrecio().multiply(new java.math.BigDecimal(detalle.getCantidad())));
        } else {
            throw new RuntimeException("Producto o cantidad no puede ser nulo en el detalle de venta.");
        }
    }
    // Ahora sí, todos los subtotales están calculados y no son null
    BigDecimal subtotalVenta = detalles.stream()
        .map(DetalleVenta::getSubtotal)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal iva = subtotalVenta.multiply(new BigDecimal("0.19")).setScale(2, RoundingMode.HALF_UP);
    BigDecimal totalVenta = subtotalVenta.add(iva);

    venta.setSubtotal(subtotalVenta);
    venta.setIva(iva);
    venta.setTotal(totalVenta);

    boolean stockOk = productoService.verificarYDescontarStock(detalles);
    if (!stockOk) {
        throw new RuntimeException("Stock insuficiente o se viola el stock mínimo para uno o más productos.");
    }

    Venta ventaGuardada = ventaRepository.save(venta);
    for (DetalleVenta detalle : detalles) {
        detalle.setVenta(ventaGuardada);
        detalleVentaRepository.save(detalle);
    }
    return ventaRepository.findVentaWithClienteAndDetalles(ventaGuardada.getId());
}


    // Nuevo método para listar ventas con DTO (sin proveedor ni categoria completa)
    public List<VentaConDetallesResponse> obtenerVentasSimples() {
        List<Venta> ventas = ventaRepository.findAll();
        List<VentaConDetallesResponse> dtos = new ArrayList<>();

        for (Venta venta : ventas) {
            VentaConDetallesResponse dto = new VentaConDetallesResponse();
            dto.setId(venta.getId());
            dto.setFecha(venta.getFecha());
            dto.setSubtotal(venta.getSubtotal()); // <-- Asignar subtotal
            dto.setIva(venta.getIva());           // <-- Asignar iva
            dto.setTotal(venta.getTotal());

            VentaConDetallesResponse.ClienteSimple clienteDto = new VentaConDetallesResponse.ClienteSimple();
            clienteDto.setId(venta.getCliente().getId());
            clienteDto.setNombre(venta.getCliente().getNombre());
            dto.setCliente(clienteDto);

            List<VentaConDetallesResponse.DetalleVentaSimple> detallesDto = new ArrayList<>();
            if (venta.getDetalles() != null) {
                for (DetalleVenta d : venta.getDetalles()) {
                    Producto producto = d.getProducto();
                    // Si el nombre es null, recarga el producto desde el repositorio
                    if (producto != null && (producto.getNombre() == null || producto.getNombre().isEmpty())) {
                        producto = productoService.obtenerProductoPorId(producto.getId());
                        d.setProducto(producto);
                    }
                    VentaConDetallesResponse.DetalleVentaSimple detDto = new VentaConDetallesResponse.DetalleVentaSimple();
                    detDto.setIdProducto(producto != null ? producto.getId() : null);
                    detDto.setNombreProducto(producto != null ? producto.getNombre() : null);
                    detDto.setPrecioUnitario(d.getPrecioUnitario());
                    detDto.setCantidad(d.getCantidad());
                    detDto.setSubtotal(d.getSubtotal());
                    detallesDto.add(detDto);
                }
            }
            dto.setDetalles(detallesDto);

            dtos.add(dto);
        }
        return dtos;
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
        Venta venta = ventaRepository.findVentaWithClienteAndDetalles(id);
        if (venta == null) return null;
        // Forzar la carga de detalles desde el repositorio si están vacíos
        if (venta.getDetalles() == null || venta.getDetalles().isEmpty()) {
            venta.setDetalles(detalleVentaRepository.findByVentaIdWithProducto(id));
        }
        VentaConDetallesResponse dto = new VentaConDetallesResponse();
        dto.setId(venta.getId());
        dto.setFecha(venta.getFecha());
        dto.setSubtotal(venta.getSubtotal()); // <-- Asignar subtotal
        dto.setIva(venta.getIva());           // <-- Asignar iva
        dto.setTotal(venta.getTotal());
        VentaConDetallesResponse.ClienteSimple clienteDto = new VentaConDetallesResponse.ClienteSimple();
        clienteDto.setId(venta.getCliente().getId());
        clienteDto.setNombre(venta.getCliente().getNombre());
        dto.setCliente(clienteDto);
        java.util.List<VentaConDetallesResponse.DetalleVentaSimple> detallesDto = new java.util.ArrayList<>();
        if (venta.getDetalles() != null) {
            for (DetalleVenta d : venta.getDetalles()) {
                VentaConDetallesResponse.DetalleVentaSimple detDto = new VentaConDetallesResponse.DetalleVentaSimple();
                detDto.setIdProducto(d.getProducto().getId());
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

    public List<VentaConDetallesResponse> obtenerTodasVentasSimples() {
        List<Venta> ventas = ventaRepository.findAll();
        List<VentaConDetallesResponse> dtos = new ArrayList<>();

        for (Venta venta : ventas) {
            VentaConDetallesResponse dto = new VentaConDetallesResponse();
            dto.setId(venta.getId());
            dto.setFecha(venta.getFecha());
            // Si subtotal o iva están en null, los calculamos en tiempo real
            BigDecimal subtotal = venta.getSubtotal();
            BigDecimal iva = venta.getIva();
            if (subtotal == null || iva == null) {
                subtotal = BigDecimal.ZERO;
                if (venta.getDetalles() != null) {
                    for (DetalleVenta d : venta.getDetalles()) {
                        if (d.getSubtotal() != null) {
                            subtotal = subtotal.add(d.getSubtotal());
                        }
                    }
                }
                iva = subtotal.multiply(new BigDecimal("0.19")).setScale(2, java.math.RoundingMode.HALF_UP);
            }
            dto.setSubtotal(subtotal);
            dto.setIva(iva);
            dto.setTotal(venta.getTotal());

            VentaConDetallesResponse.ClienteSimple clienteDto = new VentaConDetallesResponse.ClienteSimple();
            clienteDto.setId(venta.getCliente().getId());
            clienteDto.setNombre(venta.getCliente().getNombre());
            dto.setCliente(clienteDto);

            List<VentaConDetallesResponse.DetalleVentaSimple> detallesDto = new ArrayList<>();
            if (venta.getDetalles() != null) {
                for (DetalleVenta d : venta.getDetalles()) {
                    Producto producto = d.getProducto();
                    if (producto != null && (producto.getNombre() == null || producto.getNombre().isEmpty())) {
                        producto = productoService.obtenerProductoPorId(producto.getId());
                        d.setProducto(producto);
                    }
                    VentaConDetallesResponse.DetalleVentaSimple detDto = new VentaConDetallesResponse.DetalleVentaSimple();
                    detDto.setIdProducto(producto != null ? producto.getId() : null);
                    detDto.setNombreProducto(producto != null ? producto.getNombre() : null);
                    detDto.setPrecioUnitario(d.getPrecioUnitario());
                    detDto.setCantidad(d.getCantidad());
                    detDto.setSubtotal(d.getSubtotal());
                    detallesDto.add(detDto);
                }
            }
            dto.setDetalles(detallesDto);

            dtos.add(dto);
        }
        return dtos;
    }


}
