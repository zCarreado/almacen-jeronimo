package gestorDeInventariosYVentas.example.dto.output;

import gestorDeInventariosYVentas.example.model.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ProductOutputDTO {

    private Long id;

    private String name;

    private String description;

    private Double price;

    private Long stock;

    private Long category;

    private LocalDateTime creationDate;

    public ProductOutputDTO(Long id, String name, String description, Double price, Long stock, Long category, LocalDateTime creationDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.creationDate = creationDate;
    }
}
