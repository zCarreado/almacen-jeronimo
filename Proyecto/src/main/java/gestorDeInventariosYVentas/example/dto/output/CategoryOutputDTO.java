package gestorDeInventariosYVentas.example.dto.output;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class CategoryOutputDTO {

    private Long id;

    private String name;

    private String description;

    public CategoryOutputDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
