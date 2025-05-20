package gestorDeInventariosYVentas.example.controller;

import gestorDeInventariosYVentas.example.controller.controllerDoc.CategoryRestControllerDoc;
import gestorDeInventariosYVentas.example.dto.input.CategoryInputDTO;
import gestorDeInventariosYVentas.example.dto.output.CategoryOutputDTO;
import gestorDeInventariosYVentas.example.dto.output.ProductOutputDTO;
import gestorDeInventariosYVentas.example.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@Tag( name = "Category RestController", description = "Controller for category")
public class CategoryRestController implements CategoryRestControllerDoc{

    private final CategoryService categoryService;

    public CategoryRestController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryOutputDTO> createCategory(@RequestBody CategoryInputDTO categoryInputDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(categoryInputDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ProductOutputDTO>> getProductsByCategory(@PathVariable Long id){
        return ResponseEntity.ok(categoryService.getProductsByCategory(id));
    }

    @GetMapping
    public ResponseEntity<List<CategoryOutputDTO>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}
