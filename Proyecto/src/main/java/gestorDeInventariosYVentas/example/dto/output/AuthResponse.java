package gestorDeInventariosYVentas.example.dto.output;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class AuthResponse {

    private String username;
    private String message;
    private String accessToken;
    private boolean success;

    public AuthResponse(String username, String message, String accessToken, boolean success) {
        this.username = username;
        this.message = message;
        this.accessToken = accessToken;
        this.success = success;
    }
}
