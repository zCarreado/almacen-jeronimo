package gestorDeInventariosYVentas.example.mapper;

import gestorDeInventariosYVentas.example.dto.input.CategoryInputDTO;
import gestorDeInventariosYVentas.example.dto.output.CategoryOutputDTO;
import gestorDeInventariosYVentas.example.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryOutputDTO toDto (Category category){

        return new CategoryOutputDTO(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }

    public Category toEntity (CategoryInputDTO categoryInputDTO){

        return new Category(
                categoryInputDTO.getName(),
                categoryInputDTO.getDescription()
        );
    }

}
