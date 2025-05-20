package gestorDeInventariosYVentas.example.service.impl;

import gestorDeInventariosYVentas.example.dto.input.ProductInputDTO;
import gestorDeInventariosYVentas.example.dto.output.ProductOutputDTO;
import gestorDeInventariosYVentas.example.mapper.ProductMapper;
import gestorDeInventariosYVentas.example.model.Product;
import gestorDeInventariosYVentas.example.repository.ProductRepository;
import gestorDeInventariosYVentas.example.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository,ProductMapper productMapper){
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductOutputDTO createProduct(ProductInputDTO productInputDTO) {

        Product product = productMapper.toEntity(productInputDTO);

        productRepository.save(product);

        return productMapper.toDto(product);
    }

    @Override
    public String getStock(Long id) {

        Objects.requireNonNull(id,"Product ID cannot be null.");

        ProductOutputDTO product = productRepository.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(()-> new EntityNotFoundException("Product with ID " + id + " not found"));

        return "The stock quantity of the product is: " + product.getStock();
    }

    @Override
    public void reduceStock(Long id,Long quantity) {

        Objects.requireNonNull(id,"Product ID cannot be null.");
        Objects.requireNonNull(quantity,"Quantity cannot be null.");

        Product product = productRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Product with ID " + id + " not found"));

        if (product.getStock() < quantity){
            throw new IllegalArgumentException("Not enough stock to reduce. Current stock: " + product.getStock());
        }

        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }

    @Override
    public void increaseStock(Long id,Long quantity) {

        Objects.requireNonNull(id,"Product ID cannot be null.");
        Objects.requireNonNull(quantity,"Quantity cannot be null.");

        Product product = productRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Product with ID " + id + " not found"));

        product.setStock(product.getStock() + quantity);
        productRepository.save(product);
    }

    @Override
    public String getPrice(Long id) {

        ProductOutputDTO product = productRepository.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(()-> new EntityNotFoundException("Product with ID " + id + " not found"));

        return "the price of the product is: " + product.getPrice();
    }

    @Override
    public void updatePrice(Long id,Double newPrice) {

        Objects.requireNonNull(id,"Product ID cannot be null.");
        Objects.requireNonNull(newPrice,"price value cannot be null.");

        Product product = productRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Product with ID " + id + " not found"));

        product.setPrice(newPrice);
        productRepository.save(product);
    }

    @Override
    public ProductOutputDTO getProduct(Long id) {

        Objects.requireNonNull(id,"Product ID cannot be null.");

        return productRepository.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(()-> new EntityNotFoundException("Product with ID " + id + " not found"));
    }

    @Override
    public List<ProductOutputDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }
}
