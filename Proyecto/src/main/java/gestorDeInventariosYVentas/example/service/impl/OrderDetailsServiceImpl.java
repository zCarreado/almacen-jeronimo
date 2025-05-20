package gestorDeInventariosYVentas.example.service.impl;

import gestorDeInventariosYVentas.example.dto.input.OrderDetailsInputDTO;
import gestorDeInventariosYVentas.example.dto.output.OrderDetailsOutputDTO;
import gestorDeInventariosYVentas.example.dto.output.ProductOutputDTO;
import gestorDeInventariosYVentas.example.mapper.OrderDetailsMapper;
import gestorDeInventariosYVentas.example.mapper.ProductMapper;
import gestorDeInventariosYVentas.example.model.Order;
import gestorDeInventariosYVentas.example.model.OrderDetails;
import gestorDeInventariosYVentas.example.model.Product;
import gestorDeInventariosYVentas.example.repository.OrderDetailsRepository;
import gestorDeInventariosYVentas.example.repository.OrderRepository;
import gestorDeInventariosYVentas.example.repository.ProductRepository;
import gestorDeInventariosYVentas.example.service.OrderDetailsService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {

    private final OrderDetailsRepository orderDetailsRepository;
    private final OrderDetailsMapper orderDetailsMapper;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final OrderRepository orderRepository;

    public OrderDetailsServiceImpl(OrderDetailsRepository orderDetailsRepository, OrderDetailsMapper orderDetailsMapper,
                                   ProductRepository productRepository, ProductMapper productMapper, OrderRepository orderRepository){
        this.orderDetailsRepository = orderDetailsRepository;
        this.orderDetailsMapper = orderDetailsMapper;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderDetailsOutputDTO createOrderDetails(OrderDetailsInputDTO orderDetailsInputDTO) {

        OrderDetails orderDetails = orderDetailsMapper.toEntity(orderDetailsInputDTO);

        Product product = productRepository.findById(orderDetailsInputDTO.getProduct().getId())
                        .orElseThrow(()-> new  EntityNotFoundException("Product with ID " + orderDetailsInputDTO.getProduct().getId() + " not found"));

        Order order = orderRepository.findById(orderDetailsInputDTO.getOrder().getId())
                .orElseThrow(()->new EntityNotFoundException("Order with ID " + orderDetailsInputDTO.getOrder().getId()));

        if (orderDetailsInputDTO.getQuantity() == null || orderDetailsInputDTO.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        if (product.getPrice() == null) {
            throw new IllegalStateException("The product price cannot be null");
        }

        Double subtotal = calculateSubTotal(product, orderDetails.getQuantity());

        orderDetails.setSubTotal(subtotal);

        orderDetails.setOrder(order);

        orderDetails.setProduct(product);

        orderDetailsRepository.save(orderDetails);

        return orderDetailsMapper.toDto(orderDetails);
    }

    //No se utiliza directamente, es invocada por otro método
    @Override
    public Double calculateSubTotal(Product product, Long quantity) {

        Objects.requireNonNull(product, "ID product cannot be null");

        Objects.requireNonNull(quantity, "Quantity must be greater than 0");

        return product.getPrice() * quantity;
    }

    @Override
    public ProductOutputDTO getProduct(Long id) {
        Objects.requireNonNull(id,"ID cannot be null");

        Product product = productRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Product with ID " + id + " not found"));

        return productMapper.toDto(product);
    }

    @Override
    public String getQuantity(Long id) {
        Objects.requireNonNull(id,"ID cannot be null");

        OrderDetails orderDetails = orderDetailsRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Order Details with ID " + id + " not found"));

        return "The quantity is " + orderDetails.getQuantity();
    }

    @Override
    public OrderDetailsOutputDTO getOrderDetails(Long id) {
        Objects.requireNonNull(id,"ID cannot be null");

        OrderDetails orderDetails = orderDetailsRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Order Details with ID " + id + " not found"));

        return orderDetailsMapper.toDto(orderDetails);
    }
}
