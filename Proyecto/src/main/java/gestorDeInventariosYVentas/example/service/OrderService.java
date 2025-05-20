package gestorDeInventariosYVentas.example.service;

import gestorDeInventariosYVentas.example.dto.input.OrderInputDTO;
import gestorDeInventariosYVentas.example.dto.output.CustomerOutputDTO;
import gestorDeInventariosYVentas.example.dto.output.OrderDetailsOutputDTO;
import gestorDeInventariosYVentas.example.dto.output.OrderOutputDTO;
import gestorDeInventariosYVentas.example.model.Order;
import gestorDeInventariosYVentas.example.model.OrderDetails;

import java.util.List;

public interface OrderService {

    OrderOutputDTO createOrder (OrderInputDTO orderInputDTO);

    void calculateTotal(Long id);

    void addOrderDetail(Long idOrder,Long idOrderDetails);

    void removeOrderDetails(Long idOrder,Long idOrderDetails);

    CustomerOutputDTO getCustomer(Long id);

    OrderOutputDTO getOrder(Long id);

    List<OrderDetailsOutputDTO> getOrdersDetailsByOrder(Long id);

    OrderOutputDTO completeOrder(Long id);

    List<OrderOutputDTO> GetAllOrders();
}
