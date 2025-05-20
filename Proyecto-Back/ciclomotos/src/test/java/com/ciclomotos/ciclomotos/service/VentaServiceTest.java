package com.ciclomotos.ciclomotos.service;

import com.ciclomotos.ciclomotos.model.DetalleVenta;
import com.ciclomotos.ciclomotos.model.Producto;
import com.ciclomotos.ciclomotos.model.Venta;
import com.ciclomotos.ciclomotos.repository.DetalleVentaRepository;
import com.ciclomotos.ciclomotos.repository.VentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VentaServiceTest {
    @Mock
    private VentaRepository ventaRepository;
    @Mock
    private ProductoService productoService;
    @Mock
    private DetalleVentaRepository detalleVentaRepository;
    @InjectMocks
    private VentaService ventaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearVenta_StockSuficiente() {
        Venta venta = new Venta();
        DetalleVenta detalle = new DetalleVenta();
        detalle.setCantidad(2);
        List<DetalleVenta> detalles = List.of(detalle);
        when(productoService.verificarYDescontarStock(detalles)).thenReturn(true);
        when(ventaRepository.save(venta)).thenReturn(venta);
        when(detalleVentaRepository.save(detalle)).thenReturn(detalle);
        Venta result = ventaService.crearVenta(venta, detalles);
        assertNotNull(result);
        verify(productoService, times(1)).verificarYDescontarStock(detalles);
        verify(ventaRepository, times(1)).save(venta);
        verify(detalleVentaRepository, times(1)).save(detalle);
    }

    @Test
    void testCrearVenta_StockInsuficiente() {
        Venta venta = new Venta();
        DetalleVenta detalle = new DetalleVenta();
        detalle.setCantidad(10);
        List<DetalleVenta> detalles = List.of(detalle);
        when(productoService.verificarYDescontarStock(detalles)).thenReturn(false);
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            ventaService.crearVenta(venta, detalles);
        });
        assertEquals("Stock insuficiente o se viola el stock mínimo para uno o más productos.", thrown.getMessage());
        verify(productoService, times(1)).verificarYDescontarStock(detalles);
        verify(ventaRepository, never()).save(any());
        verify(detalleVentaRepository, never()).save(any());
    }
}
