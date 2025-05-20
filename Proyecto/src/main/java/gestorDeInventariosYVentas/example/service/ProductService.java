package gestorDeInventariosYVentas.example.service;

import gestorDeInventariosYVentas.example.dto.input.ProductInputDTO;
import gestorDeInventariosYVentas.example.dto.output.ProductOutputDTO;

import java.util.List;

public interface ProductService {

    ProductOutputDTO createProduct (ProductInputDTO productInputDTO);

    String getStock(Long id);

    void reduceStock (Long id,Long quantity);

    void increaseStock(Long id,Long quantity);

    String getPrice(Long id);

    void updatePrice(Long id,Double newPrice);

    ProductOutputDTO getProduct(Long id);

    List<ProductOutputDTO> getAllProducts();
}
