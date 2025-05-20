package gestorDeInventariosYVentas.example.dto.input;

import gestorDeInventariosYVentas.example.model.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ProductInputDTO {

    private String name;

    private String description;

    private Double price;

    private Long stock;

    private Category category;

    private LocalDateTime creationDate;

    public ProductInputDTO(String name, String description, Double price, Long stock, Category category, LocalDateTime creationDate) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.creationDate = creationDate;
    }
}
