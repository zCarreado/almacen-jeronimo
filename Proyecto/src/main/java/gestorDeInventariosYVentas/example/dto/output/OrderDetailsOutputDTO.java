package gestorDeInventariosYVentas.example.dto.output;

import gestorDeInventariosYVentas.example.model.Order;
import gestorDeInventariosYVentas.example.model.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

@Getter
@Setter
@NoArgsConstructor
public class OrderDetailsOutputDTO {

    private Long id;

    private Long quantity;

    private Double subTotal;

    private Long productId;

    private Long orderId;

    public OrderDetailsOutputDTO(Long id, Long quantity, Double subTotal, Long productId, Long orderId) {
        this.id = id;
        this.quantity = quantity;
        this.subTotal = subTotal;
        this.productId = productId;
        this.orderId = orderId;
    }
}
