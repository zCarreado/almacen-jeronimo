package gestorDeInventariosYVentas.example.dto.input;

import gestorDeInventariosYVentas.example.model.Customer;
import gestorDeInventariosYVentas.example.model.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class OrderInputDTO {

    private LocalDateTime date;

    private Order.Status status;

    private Customer customer;

    public OrderInputDTO(Customer customer, Order.Status status, Double total, LocalDateTime date) {
        this.customer = customer;
        this.status = status;
        this.date = date;
    }
}
