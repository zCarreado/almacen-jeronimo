package gestorDeInventariosYVentas.example.dto.input;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor

public class AuthCreateUserRequest {

    private String username;
    private String password;
    private Set<String> roles;

    public AuthCreateUserRequest(Set<String> roles, String password, String username) {
        this.roles = roles;
        this.password = password;
        this.username = username;
    }
}
