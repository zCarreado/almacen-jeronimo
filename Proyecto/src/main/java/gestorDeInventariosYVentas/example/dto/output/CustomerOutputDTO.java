package gestorDeInventariosYVentas.example.dto.output;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class CustomerOutputDTO {

    private Long id;

    private String name;

    private String email;

    private String address;

    private String phoneNumber;

    public CustomerOutputDTO(Long id, String name, String email, String address, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
