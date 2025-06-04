package com.ciclomotos.ciclomotos.service;

import com.ciclomotos.ciclomotos.model.Categoria;
import com.ciclomotos.ciclomotos.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Bicicletas");
    }

    @Test
    void testCrearCategoria() {
        when(categoriaRepository.save(categoria)).thenReturn(categoria);
        Categoria result = categoriaService.crearCategoria(categoria);
        assertNotNull(result);
        assertEquals("Bicicletas", result.getNombre());
        verify(categoriaRepository, times(1)).save(categoria);
    }

    @Test
    void testObtenerCategorias() {
        when(categoriaRepository.findAll()).thenReturn(Arrays.asList(categoria));
        List<Categoria> result = categoriaService.obtenerCategorias();
        assertEquals(1, result.size());
        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    void testObtenerCategoriaPorId() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        Optional<Categoria> result = categoriaService.obtenerCategoriaPorId(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testActualizarCategoria_existente() {
        when(categoriaRepository.existsById(1L)).thenReturn(true);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);
        Categoria result = categoriaService.actualizarCategoria(1L, categoria);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(categoriaRepository).save(categoria);
    }

    @Test
    void testActualizarCategoria_noExistente() {
        when(categoriaRepository.existsById(2L)).thenReturn(false);
        Categoria result = categoriaService.actualizarCategoria(2L, categoria);
        assertNull(result);
        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    void testEliminarCategoria() {
        categoriaService.eliminarCategoria(1L);
        verify(categoriaRepository, times(1)).deleteById(1L);
    }
}