package gestorDeInventariosYVentas.example.controller.controllerDoc;

import gestorDeInventariosYVentas.example.dto.input.OrderDetailsInputDTO;
import gestorDeInventariosYVentas.example.dto.input.ProductInputDTO;
import gestorDeInventariosYVentas.example.dto.output.OrderDetailsOutputDTO;
import gestorDeInventariosYVentas.example.dto.output.ProductOutputDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ProductRestControllerDoc {

    @Operation(
            summary = "Create product",
            description = "This method allows creating a new product.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "requires receiving the following attributes: name, description, price, stock, " +
                            "category ID, creation date",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductInputDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Example input",
                                            value = "{" +
                                                    "\"name\": \"GTA V\"," +
                                                    "\"description\": \"video game\"," +
                                                    "\"price\": 250," +
                                                    "\"stock\": 18," +
                                                    "\"category\": { \"id\" : 1 }," +
                                                    "\"creationDate\": \"2025-01-25T15:30:00\"" +
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
                                    schema = @Schema(implementation = ProductOutputDTO.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example response",
                                                    value = "{" +
                                                            "\"id\": 1," +
                                                            "\"name\": \"GTA V\"," +
                                                            "\"description\": \"video game\"," +
                                                            "\"price\": 250," +
                                                            "\"stock\": 18," +
                                                            "\"category\": { \"id\" : 1 }," +
                                                            "\"creationDate\": \"2025-01-25T15:30:00\"" +
                                                            "}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"localDateTime\":\"2024-12-13 15:30:00\"," +
                                                    " \"status\" : \"400\"," +
                                                    " \"error\" : \"Invalid JSON Format\"," +
                                                    " \"message\" : \"The JSON payload is malformed. Ensure it is well-formed and properly structured.\"}"
                                    )
                            )
                    )
            }
    )
    ResponseEntity<ProductOutputDTO> createProduct(@RequestBody ProductInputDTO productInputDTO);

    @Operation(
            summary = "Get stock",
            description = "This method returns the available stock quantity of a product.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "The unique ID of the product to retrieve. This ID corresponds" +
                                    " to an existing product in the system.",
                            example = "2",
                            in = ParameterIn.PATH
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "text/plain",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example response",
                                                    value = "The stock quantity of the product is: 18"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"localDateTime\":\"2024-12-13 15:30:00\"," +
                                                    " \"status\" : \"404\"," +
                                                    " \"error\" : \"Not Found\"," +
                                                    " \"message\" : \"Product with ID 2 not found\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"localDateTime\":\"2024-12-13T15:30:00\"," +
                                                    " \"status\" : \"400\"," +
                                                    " \"error\" : \"NullPointerException\"," +
                                                    " \"message\" : \"Product ID cannot be null.\"}"
                                    )
                            )
                    )
            }
    )
    ResponseEntity<String> getStock(@PathVariable Long id);

    @Operation(
            summary = "Reduce stock",
            description = "This method allows reducing the stock of a product.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "The unique ID of the product to retrieve. This ID corresponds" +
                                    " to an existing product in the system.",
                            example = "2",
                            in = ParameterIn.PATH
                    ),
                    @Parameter(
                            name = "quantity",
                            description = "The stock quantity we want to reduce from the product's current stock.",
                            example = "7",
                            in = ParameterIn.PATH
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "text/plain",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example response",
                                                    value = "The stock of the product with the ID 2 has been reduced."
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"localDateTime\":\"2024-12-13 15:30:00\"," +
                                                    " \"status\" : \"404\"," +
                                                    " \"error\" : \"Not Found\"," +
                                                    " \"message\" : \"Product with ID 2 not found\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"localDateTime\":\"2024-12-13T15:30:00\"," +
                                                    " \"status\" : \"400\"," +
                                                    " \"error\" : \"NullPointerException\"," +
                                                    " \"message\" : \"Product / quantity cannot be null.\"}"
                                    )
                            )
                    )
            }
    )
    ResponseEntity<String> reduceStock(@PathVariable Long id,@PathVariable Long quantity);

    @Operation(
            summary = "Increase stock",
            description = "This method allows increase the stock of a product.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "The unique ID of the product to retrieve. This ID corresponds" +
                                    " to an existing product in the system.",
                            example = "2",
                            in = ParameterIn.PATH
                    ),
                    @Parameter(
                            name = "quantity",
                            description = "The stock quantity we want to increase from the product's current stock.",
                            example = "7",
                            in = ParameterIn.PATH
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "text/plain",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example response",
                                                    value = "The stock of the product with the ID 2 has been increased."
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"localDateTime\":\"2024-12-13 15:30:00\"," +
                                                    " \"status\" : \"404\"," +
                                                    " \"error\" : \"Not Found\"," +
                                                    " \"message\" : \"Product with ID 2 not found\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"localDateTime\":\"2024-12-13T15:30:00\"," +
                                                    " \"status\" : \"400\"," +
                                                    " \"error\" : \"NullPointerException\"," +
                                                    " \"message\" : \"Product / quantity cannot be null.\"}"
                                    )
                            )
                    )
            }
    )
    ResponseEntity<String> increaseStock(@PathVariable Long id,@PathVariable Long quantity);

    @Operation(
            summary = "Get price",
            description = "This method returns the price of the product.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "The unique ID of the product to retrieve. This ID corresponds" +
                                    " to an existing product in the system.",
                            example = "2",
                            in = ParameterIn.PATH
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "text/plain",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example response",
                                                    value = "the price of the product is: 250.0"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"localDateTime\":\"2024-12-13 15:30:00\"," +
                                                    " \"status\" : \"404\"," +
                                                    " \"error\" : \"Not Found\"," +
                                                    " \"message\" : \"Product with ID 2 not found\"}"
                                    )
                            )
                    )
            }
    )
    ResponseEntity<String> getPrice(@PathVariable Long id);

    @Operation(
            summary = "Update price",
            description = "This method returns the price of the product.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "The unique ID of the product to retrieve. This ID corresponds" +
                                    " to an existing product in the system.",
                            example = "2",
                            in = ParameterIn.PATH
                    ),
                    @Parameter(
                            name = "newPrice",
                            description = "The new price of the product is specified.",
                            example = "200",
                            in = ParameterIn.PATH
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "text/plain",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example response",
                                                    value = "The price of the product with ID 2 has been updated."
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"localDateTime\":\"2024-12-13 15:30:00\"," +
                                                    " \"status\" : \"404\"," +
                                                    " \"error\" : \"Not Found\"," +
                                                    " \"message\" : \"Product with ID 2 not found\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"localDateTime\":\"2024-12-13T15:30:00\"," +
                                                    " \"status\" : \"400\"," +
                                                    " \"error\" : \"NullPointerException\"," +
                                                    " \"message\" : \"Product / price cannot be null.\"}"
                                    )
                            )
                    )
            }
    )
    ResponseEntity<String> updatePrice(@PathVariable Long id,@PathVariable Double newPrice);

    @Operation(
            summary = "Get product",
            description = "This method returns a DTO of the product.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "The unique ID of the product to retrieve. This ID corresponds" +
                                    " to an existing product in the system.",
                            example = "2",
                            in = ParameterIn.PATH
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProductOutputDTO.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example response",
                                                    value = "{" +
                                                            "\"id\": 1," +
                                                            "\"name\": \"GTA V\"," +
                                                            "\"description\": \"video game\"," +
                                                            "\"price\": 250," +
                                                            "\"stock\": 18," +
                                                            "\"category\": { \"id\" : 1 }," +
                                                            "\"creationDate\": \"2025-01-25T15:30:00\"" +
                                                            "}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"localDateTime\":\"2024-12-13 15:30:00\"," +
                                                    " \"status\" : \"404\"," +
                                                    " \"error\" : \"Not Found\"," +
                                                    " \"message\" : \"Product ID cannot be null.\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"localDateTime\":\"2024-12-13 15:30:00\"," +
                                                    " \"status\" : \"400\"," +
                                                    " \"error\" : \"Invalid JSON Format\"," +
                                                    " \"message\" : \"The JSON payload is malformed. Ensure it is well-formed and properly structured.\"}"
                                    )
                            )
                    )
            }
    )
    ResponseEntity<ProductOutputDTO> getProduct(@PathVariable Long id);

    @Operation(
            summary = "Get all products",
            description = "This method returns all created products.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "The unique ID of the product to retrieve. This ID corresponds" +
                                    " to an existing product in the system.",
                            example = "2",
                            in = ParameterIn.PATH
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProductOutputDTO.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example response",
                                                    value = "["+
                                                            "{" +
                                                            "\"id\": 1," +
                                                            "\"name\": \"GTA V\"," +
                                                            "\"description\": \"video game\"," +
                                                            "\"price\": 250," +
                                                            "\"stock\": 18," +
                                                            "\"category\": { \"id\" : 1 }," +
                                                            "\"creationDate\": \"2025-01-25T15:30:00\"" +
                                                            "},"+
                                                            "{"+
                                                            "\"id\": 2," +
                                                            "\"name\": \"Minecraft\"," +
                                                            "\"description\": \"video game\"," +
                                                            "\"price\": 130," +
                                                            "\"stock\": 7," +
                                                            "\"category\": { \"id\" : 1 }," +
                                                            "\"creationDate\": \"2025-01-25T15:30:00\"" +
                                                            "}"+
                                                            "]"
                                            )
                                    }
                            )
                    )
            }
    )
    ResponseEntity<List<ProductOutputDTO>> getAllProducts();
}
