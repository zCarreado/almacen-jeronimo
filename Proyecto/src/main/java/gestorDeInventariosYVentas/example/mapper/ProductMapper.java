package gestorDeInventariosYVentas.example.mapper;

import gestorDeInventariosYVentas.example.dto.input.ProductInputDTO;
import gestorDeInventariosYVentas.example.dto.output.ProductOutputDTO;
import gestorDeInventariosYVentas.example.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductOutputDTO toDto (Product product){
        return new ProductOutputDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory().getId(),
                product.getCreationDate()
        );
    }

    public Product toEntity (ProductInputDTO productInputDTO){
        return new Product(
                productInputDTO.getCreationDate(),
                productInputDTO.getCategory(),
                productInputDTO.getStock(),
                productInputDTO.getPrice(),
                productInputDTO.getDescription(),
                productInputDTO.getName()
        );
    }
}
