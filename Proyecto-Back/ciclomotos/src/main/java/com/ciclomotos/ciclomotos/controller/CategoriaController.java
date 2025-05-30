package com.ciclomotos.ciclomotos.controller;

import com.ciclomotos.ciclomotos.model.Categoria;
import com.ciclomotos.ciclomotos.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    /**
     * POST /api/categorias/crearCategoria
     * Crea una nueva categoría.
     * JSON de entrada:
     * {
     *   "nombre": "Bicicletas"
     * }
     * Respuesta: Categoría creada (incluye el campo id).
     */
    @PostMapping("/crearCategoria")
    public Categoria crearCategoria(@RequestBody Categoria categoria) {
        return categoriaService.crearCategoria(categoria);
    }

    /**
     * GET /api/categorias/obtenerCategorias
     * Obtiene todas las categorías.
     * Respuesta:
     * [
     *   {
     *     "id": 1,
     *     "nombre": "Bicicletas"
     *   },
     *   ...
     * ]
     */
    @GetMapping("/obtenerCategorias")
    public List<Categoria> obtenerCategorias() {
        return categoriaService.obtenerCategorias();
    }

    /**
     * GET /api/categorias/obtenerCategoria/{id}
     * Obtiene una categoría por su ID.
     * Respuesta:
     * {
     *   "id": 1,
     *   "nombre": "Bicicletas"
     * }
     */
    @GetMapping("/obtenerCategoria/{id}")
    public Optional<Categoria> obtenerCategoriaPorId(@PathVariable Long id) {
        return categoriaService.obtenerCategoriaPorId(id);
    }

    /**
     * PUT /api/categorias/actualizarCategoria/{id}
     * Actualiza una categoría existente.
     * JSON de entrada:
     * {
     *   "nombre": "Nueva Bicicletas"
     * }
     * Respuesta: Categoría actualizada.
     */
    @PutMapping("/actualizarCategoria/{id}")
    public Categoria actualizarCategoria(@PathVariable Long id, @RequestBody Categoria categoria) {
        return categoriaService.actualizarCategoria(id, categoria);
    }

    /**
     * DELETE /api/categorias/eliminarCategoria/{id}
     * Elimina una categoría por su ID.
     * No requiere body.
     * Respuesta: Sin contenido (código 200 o 204).
     */
    @DeleteMapping("/eliminarCategoria/{id}")
    public void eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminarCategoria(id);
    }
}
