package gestorDeInventariosYVentas.example.dto.input;

import gestorDeInventariosYVentas.example.model.Order;
import gestorDeInventariosYVentas.example.model.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class OrderDetailsInputDTO {

    private Long quantity;

    private Double subTotal;

    private Product product;

    private Order order;

    public OrderDetailsInputDTO(Long quantity, Double subTotal, Product product, Order order) {
        this.quantity = quantity;
        this.subTotal = subTotal;
        this.product = product;
        this.order = order;
    }
}
