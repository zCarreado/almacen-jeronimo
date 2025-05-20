package gestorDeInventariosYVentas.example.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse {
    private LocalDateTime localDateTime;
    private int status;
    private String error;
    private String message;

    public ErrorResponse(LocalDateTime localDateTime, int status, String error, String message) {
        this.localDateTime = localDateTime;
        this.status = status;
        this.error = error;
        this.message = message;
    }
}
