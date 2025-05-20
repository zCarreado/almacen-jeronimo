package gestorDeInventariosYVentas.example.service;

import gestorDeInventariosYVentas.example.dto.input.OrderDetailsInputDTO;
import gestorDeInventariosYVentas.example.dto.output.OrderDetailsOutputDTO;
import gestorDeInventariosYVentas.example.dto.output.ProductOutputDTO;
import gestorDeInventariosYVentas.example.model.Product;

public interface OrderDetailsService {

    OrderDetailsOutputDTO createOrderDetails (OrderDetailsInputDTO orderDetailsInputDTO);

    Double  calculateSubTotal (Product product, Long quantity);

    ProductOutputDTO getProduct(Long id);

    String getQuantity (Long id);

    OrderDetailsOutputDTO getOrderDetails(Long id);
}
