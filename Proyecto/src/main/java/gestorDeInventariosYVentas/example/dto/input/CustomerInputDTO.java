package gestorDeInventariosYVentas.example.dto.input;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class CustomerInputDTO {

    private String name;

    private String email;

    private String address;

    private String phoneNumber;

    public CustomerInputDTO(String name, String email, String address, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
