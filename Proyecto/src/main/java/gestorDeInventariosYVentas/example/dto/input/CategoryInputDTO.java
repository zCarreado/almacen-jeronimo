package gestorDeInventariosYVentas.example.dto.input;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class CategoryInputDTO {

    private String name;

    private String description;

    public CategoryInputDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
