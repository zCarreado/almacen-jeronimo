package com.ciclomotos.ciclomotos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ciclomotos.ciclomotos.model.Producto;
import com.ciclomotos.ciclomotos.repository.MovimientoInventarioRepository;
import com.ciclomotos.ciclomotos.repository.ProductoRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
/**
 * Clase de prueba para el servicio ProductoService.
 *  Esta clase contiene pruebas unitarias para la clase de Producto del servicio ProductoService.
 */
@ExtendWith(MockitoExtension.class)

public class ProductoServiceTest {
    /**
     * Repositorio simulado para productos.
     * Este repositorio se utiliza para simular las operaciones de base de datos
     */
    @Mock
    private ProductoRepository productoRepository;
/**
     * Repositorio simulado para movimientos de inventario.
     * Este repositorio se utiliza para simular las operaciones de base de datos relacionadas 
     * con los movimientos de inventario.
     */
    @Mock
    private MovimientoInventarioRepository movimientoInventarioRepository;
/**
 * Servicio que se va a probar.
 * Este servicio contiene la lógica de negocio relacionada con los productos.
 */
    @InjectMocks
    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    /**
     * Metodo para probar la creación de un producto.
     * Se crea un producto, se simula su guardado en el repositorio y se verifica que el nombre sea correcto.
     */
    @Test
    void testCrearProducto() {
        Producto producto = new Producto();
        producto.setNombre("Producto 1");
        when(productoRepository.save(producto)).thenReturn(producto);

        Producto result = productoService.crearProducto(producto);

        assertEquals("Producto 1", result.getNombre());
        verify(productoRepository, times(1)).save(producto);
    }
    /**
     * Metodo para probar la obtención de todos los productos.
     * Se simula la recuperación de una lista de productos y se verifica que el tamaño de la lista sea correcto.
     */
     @Test
    void testObtenerProductos() {
        Producto p1 = new Producto();
        Producto p2 = new Producto();
        when(productoRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Producto> productos = productoService.obtenerProductos();

        assertEquals(2, productos.size());
        verify(productoRepository, times(1)).findAll();
    }
    /**
     * Metodo para probar la obtención de un producto por su ID.
     * Se simula la recuperación de un producto por su ID y se verifica que el ID del producto obtenido sea correcto.
     */
    @Test
    void testObtenerProductoPorId() {
        Producto producto = new Producto();
        producto.setId(9L);
        when(productoRepository.findById(9L)).thenReturn(Optional.of(producto));

        Producto result = productoService.obtenerProductoPorId(9L);

        assertNotNull(result);
        assertEquals(9L, result.getId());
    }
    /**
     * Metodo para probar la actualización de un producto.
     * Se simula la existencia del producto en el repositorio y se verifica que el nombre del producto actualizado sea correcto.
     */
     @Test
    void testActualizarProducto() {
        Producto producto = new Producto();
        producto.setNombre("Casco Nuevo");
        when(productoRepository.existsById(9L)).thenReturn(true);
        when(productoRepository.save(producto)).thenReturn(producto);
         Producto result = productoService.actualizarProducto(9L, producto);

        assertNotNull(result);
        assertEquals("Casco Nuevo", result.getNombre());
    }
    /**
     * Metodo para probar la eliminación de un producto.
     * Se simula la eliminación del producto por su ID y se verifica que el método de eliminación del repositorio sea llamado una vez.
     */
    
    @Test
    void testEliminarProducto() {
        productoService.eliminarProducto(1L);
        verify(productoRepository, times(1)).deleteById(1L);
    }
}