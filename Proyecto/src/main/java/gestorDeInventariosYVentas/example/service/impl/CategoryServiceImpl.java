package gestorDeInventariosYVentas.example.service.impl;

import gestorDeInventariosYVentas.example.dto.input.CategoryInputDTO;
import gestorDeInventariosYVentas.example.dto.output.CategoryOutputDTO;
import gestorDeInventariosYVentas.example.dto.output.ProductOutputDTO;
import gestorDeInventariosYVentas.example.mapper.CategoryMapper;
import gestorDeInventariosYVentas.example.mapper.ProductMapper;
import gestorDeInventariosYVentas.example.model.Category;
import gestorDeInventariosYVentas.example.repository.CategoryRepository;
import gestorDeInventariosYVentas.example.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository,CategoryMapper categoryMapper, ProductMapper productMapper){
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.productMapper = productMapper;
    }

    @Override
    public CategoryOutputDTO createCategory(CategoryInputDTO categoryInputDTO) {

        Category category = categoryMapper.toEntity(categoryInputDTO);

        categoryRepository.save(category);

        return categoryMapper.toDto(category);
    }

    @Override
    public List<ProductOutputDTO> getProductsByCategory(Long id) {
        Objects.requireNonNull(id,"Category ID cannot be null.");

        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Category with ID " + id + " not found"));

        return category.getProducts().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryOutputDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }
}
