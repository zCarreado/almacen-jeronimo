package com.ciclomotos.ciclomotos.controller;

import com.ciclomotos.ciclomotos.model.Categoria;
import com.ciclomotos.ciclomotos.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
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
     */
    @PostMapping("/crearCategoria")
    public ResponseEntity<?> crearCategoria(@Valid @RequestBody Categoria categoria) {
        try {
            Categoria creada = categoriaService.crearCategoria(categoria);
            return ResponseEntity.ok(creada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear categoría: " + e.getMessage());
        }
    }

    /**
     * GET /api/categorias/obtenerCategorias
     * Obtiene todas las categorías.
     */
    @GetMapping("/obtenerCategorias")
    public List<Categoria> obtenerCategorias() {
        return categoriaService.obtenerCategorias();
    }

    /**
     * GET /api/categorias/obtenerCategoria/{id}
     * Obtiene una categoría por su ID.
     */
    @GetMapping("/obtenerCategoria/{id}")
    public ResponseEntity<?> obtenerCategoriaPorId(@PathVariable Long id) {
        Optional<Categoria> categoria = categoriaService.obtenerCategoriaPorId(id);
        if (categoria.isPresent()) {
            return ResponseEntity.ok(categoria.get());
        } else {
            return ResponseEntity.status(404).body("Categoría no encontrada con id: " + id);
        }
    }

    /**
     * PUT /api/categorias/actualizarCategoria/{id}
     * Actualiza una categoría existente.
     */
    @PutMapping("/actualizarCategoria/{id}")
    public ResponseEntity<?> actualizarCategoria(@PathVariable Long id, @Valid @RequestBody Categoria categoria) {
        try {
            Categoria actualizada = categoriaService.actualizarCategoria(id, categoria);
            if (actualizada != null) {
                return ResponseEntity.ok(actualizada);
            } else {
                return ResponseEntity.status(404).body("No se encontró la categoría con id: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar categoría: " + e.getMessage());
        }
    }

    /**
     * DELETE /api/categorias/eliminarCategoria/{id}
     * Elimina una categoría por su ID.
     */
    @DeleteMapping("/eliminarCategoria/{id}")
    public ResponseEntity<?> eliminarCategoria(@PathVariable Long id) {
        Optional<Categoria> categoria = categoriaService.obtenerCategoriaPorId(id);
        if (categoria.isPresent()) {
            categoriaService.eliminarCategoria(id);
            return ResponseEntity.ok("Categoría eliminada correctamente.");
        } else {
            return ResponseEntity.status(404).body("No se encontró la categoría con id: " + id);
        }
    }

    // Manejo global de errores de validación
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errores = new StringBuilder("Errores de validación: ");
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errores.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
        }
        return ResponseEntity.badRequest().body(errores.toString());
    }
}
