package gestorDeInventariosYVentas.example.service;

import gestorDeInventariosYVentas.example.dto.input.CategoryInputDTO;
import gestorDeInventariosYVentas.example.dto.output.CategoryOutputDTO;
import gestorDeInventariosYVentas.example.dto.output.ProductOutputDTO;

import java.util.List;

public interface CategoryService {

    CategoryOutputDTO createCategory (CategoryInputDTO categoryInputDTO);

    List<ProductOutputDTO> getProductsByCategory(Long id);

    List<CategoryOutputDTO> getAllCategories();
}
