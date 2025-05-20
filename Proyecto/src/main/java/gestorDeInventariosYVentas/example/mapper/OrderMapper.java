package gestorDeInventariosYVentas.example.mapper;

import gestorDeInventariosYVentas.example.dto.input.OrderInputDTO;
import gestorDeInventariosYVentas.example.dto.output.OrderOutputDTO;
import gestorDeInventariosYVentas.example.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderOutputDTO toDto (Order order){

        return new OrderOutputDTO(
                order.getId(),
                order.getDate(),
                order.getTotal(),
                order.getStatus(),
                order.getCustomer().getId()
        );
    }

    public Order toEntity (OrderInputDTO orderInputDTO){

        return new Order(
                orderInputDTO.getDate(),
                orderInputDTO.getStatus(),
                orderInputDTO.getCustomer()
        );
    }
}
