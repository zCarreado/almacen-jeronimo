package gestorDeInventariosYVentas.example.mapper;

import gestorDeInventariosYVentas.example.dto.input.OrderDetailsInputDTO;
import gestorDeInventariosYVentas.example.dto.output.OrderDetailsOutputDTO;
import gestorDeInventariosYVentas.example.model.OrderDetails;
import org.springframework.stereotype.Component;

@Component
public class OrderDetailsMapper {

    public OrderDetailsOutputDTO toDto(OrderDetails orderDetails){

        return new OrderDetailsOutputDTO(
                orderDetails.getId(),
                orderDetails.getQuantity(),
                orderDetails.getSubTotal(),
                orderDetails.getProduct().getId(),
                orderDetails.getOrder().getId()
        );
    }

    public OrderDetails toEntity (OrderDetailsInputDTO orderDetailsInputDTO){

        return new OrderDetails(
                orderDetailsInputDTO.getQuantity(),
                orderDetailsInputDTO.getSubTotal()
        );
    }
}
