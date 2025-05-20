package gestorDeInventariosYVentas.example.controller.controllerDoc;

import gestorDeInventariosYVentas.example.dto.input.AuthCreateUserRequest;
import gestorDeInventariosYVentas.example.dto.input.AuthLoginRequest;
import gestorDeInventariosYVentas.example.dto.output.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthenticationRestControllerDoc {

    @Operation(
        summary = "Register user",
        description = "The method allows you to register a new user.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "requires receiving the following attributes: username, password, roles",
                required = true,
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = AuthCreateUserRequest.class),
                        examples = {
                                @ExampleObject(
                                        name = "Example input",
                                        value = "{" +
                                                "\"username\":\"juancardona\"," +
                                                "\"password\":\"contra123\"," +
                                                "\"roles\": [" +
                                                "\"ADMIN\"" +
                                                "]"+
                                                "}"
                                )
                        }
                )
        ),
        responses = {
                @ApiResponse(
                        responseCode = "201",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = AuthResponse.class),
                                examples = {
                                        @ExampleObject(
                                                name = "Example Response",
                                                value = "{"+
                                                        "\"username\":\"juancardona\"," +
                                                        "\"message\":\"User created successfully\"," +
                                                        "\"accessToken\":\"eyJhbGci...brezO0xY\"," +
                                                        "\"success\": true" +
                                                        "}"
                                        )
                                }
                        )
                )
        }
    )
    ResponseEntity<AuthResponse> register(@RequestBody AuthCreateUserRequest authCreateUser);

    @Operation(
            summary = "Login user",
            description = "This method allows users to log in",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "requires receiving the following attributes: username, password",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthLoginRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Example input",
                                            value = "{" +
                                                    "\"username\":\"juancardona\"," +
                                                    "\"password\":\"contra123\"," +
                                                    "}"
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example Response",
                                                    value = "{"+
                                                            "\"username\":\"juancardona\"," +
                                                            "\"message\":\"User logged in successfully\"\"," +
                                                            "\"accessToken\":\"eyJhbGci...brezO0xY\"," +
                                                            "\"success\": true" +
                                                            "}"
                                            )
                                    }
                            )
                    )
            }
    )
    ResponseEntity<AuthResponse> login(@RequestBody AuthLoginRequest userRequest);
}
