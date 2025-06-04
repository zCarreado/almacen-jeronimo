package com.ciclomotos.ciclomotos.service;

import com.ciclomotos.ciclomotos.model.DetalleVenta;
import com.ciclomotos.ciclomotos.model.Producto;
import com.ciclomotos.ciclomotos.model.Venta;
import com.ciclomotos.ciclomotos.repository.DetalleVentaRepository;
import com.ciclomotos.ciclomotos.repository.VentaRepository;
import com.ciclomotos.ciclomotos.controller.dto.VentaConDetallesResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;
    @Mock
    private ProductoService productoService;
    @Mock
    private DetalleVentaRepository detalleVentaRepository;

    @InjectMocks
    private VentaService ventaService;

    private Producto producto;
    private DetalleVenta detalleVenta;
    private Venta venta;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Producto Test");
        producto.setPrecio(new BigDecimal("100.00"));

        detalleVenta = new DetalleVenta();
        detalleVenta.setProducto(producto);
        detalleVenta.setCantidad(2);

        venta = new Venta();
        venta.setId(1L);
    }

    @Test
    void testCrearVenta_ok() {
        when(productoService.obtenerProductoPorId(1L)).thenReturn(producto);
        when(productoService.verificarYDescontarStock(anyList())).thenReturn(true);
        when(ventaRepository.save(any(Venta.class))).thenReturn(venta);
        when(ventaRepository.findVentaWithClienteAndDetalles(anyLong())).thenReturn(venta);

        Venta result = ventaService.crearVenta(venta, Collections.singletonList(detalleVenta));

        assertNotNull(result);
        verify(ventaRepository).save(any(Venta.class));
        verify(detalleVentaRepository).save(any(DetalleVenta.class));
    }

    @Test
    void testObtenerVentas() {
        when(ventaRepository.findAll()).thenReturn(Arrays.asList(venta));
        assertEquals(1, ventaService.obtenerVentas().size());
    }

    @Test
    void testObtenerVentaPorId() {
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));
        Optional<Venta> result = ventaService.obtenerVentaPorId(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testActualizarVenta() {
        when(ventaRepository.existsById(1L)).thenReturn(true);
        when(ventaRepository.save(any(Venta.class))).thenReturn(venta);
        Venta result = ventaService.actualizarVenta(1L, venta);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testEliminarVenta() {
        ventaService.eliminarVenta(1L);
        verify(ventaRepository, times(1)).deleteById(1L);
    }
}
