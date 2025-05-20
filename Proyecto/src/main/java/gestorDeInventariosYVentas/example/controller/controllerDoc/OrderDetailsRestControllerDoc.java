package gestorDeInventariosYVentas.example.controller.controllerDoc;

import gestorDeInventariosYVentas.example.dto.input.OrderDetailsInputDTO;
import gestorDeInventariosYVentas.example.dto.output.CustomerOutputDTO;
import gestorDeInventariosYVentas.example.dto.output.OrderDetailsOutputDTO;
import gestorDeInventariosYVentas.example.dto.output.ProductOutputDTO;
import gestorDeInventariosYVentas.example.model.Product;
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

public interface OrderDetailsRestControllerDoc {

    @Operation(
            summary = "Create order details",
            description = "This method allows creating a new order details.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "requires receiving the following attributes: quantity, subtotal, product ID, order ID",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderDetailsInputDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Example input",
                                            value = "{" +
                                                    "\"quantity\": 2," +
                                                    "\"subtotal\": 250," +
                                                    "\"product\": { \"id\" : 1 }," +
                                                    "\"order\": { \"id\" : 1 }" +
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
                                    schema = @Schema(implementation = OrderDetailsOutputDTO.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example response",
                                                    value = "{" +
                                                            "\"id\": 1," +
                                                            "\"quantity\": 2," +
                                                            "\"subtotal\": 250," +
                                                            "\"product\": { \"id\" : 1 }," +
                                                            "\"order\": { \"id\" : 1 }" +
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
    ResponseEntity<OrderDetailsOutputDTO> createOrderDetails(@RequestBody OrderDetailsInputDTO orderDetailsInputDTO);

    @Operation(
            summary = "Calculate subTotal",
            description = "This method calculates the subtotal of the order details."
    )
    ResponseEntity<Double> calculateSubTotal(Product product, @PathVariable Long quantity);

    @Operation(
            summary = "Get product",
            description = "This method returns the product contained in the order details, " +
                    "It is used within the create method.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "The unique ID of the order details to retrieve. This ID corresponds" +
                                    " to an existing order details in the system..",
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
                                                    name = "Example Response",
                                                    value = "{" +
                                                            "\"id\": 1," +
                                                            "\"name\":\"GTA V\"," +
                                                            "\"description\":\"video games\"," +
                                                            "\"price\": 250," +
                                                            "\"stock\": 12," +
                                                            "\"category\": 1," +
                                                            "\"creationDate\":\"2025-01-25T15:30:00\"" +
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
                                                    " \"message\" : \"Product with ID 2 not found\"}"
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
                                                    " \"error\" : \"NullPointerException\"," +
                                                    " \"message\" : \"ID cannot be null\"}"
                                    )
                            )
                    )
            }
    )
    ResponseEntity<ProductOutputDTO> getProduct(@PathVariable Long id);

    @Operation(
            summary = "Get quantity",
            description = "This method returns the quantity of products that were added to the order details.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "The unique ID of the order details to retrieve. This ID corresponds" +
                                    " to an existing order details in the system..",
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
                                                    value = "The quantity is 3"
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
                                                    " \"message\" : \"Order Details with ID 2 not found\"}"
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
                                                    " \"message\" : \"ID cannot be null\"}"
                                    )
                            )
                    )
            }
    )
    ResponseEntity<String> getQuantity(@PathVariable Long id);

    @Operation(
            summary = "Get order details",
            description = "This method returns a DTO of the order details.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "The unique ID of the order details to retrieve. This ID corresponds" +
                                    " to an existing order details in the system..",
                            example = "2",
                            in = ParameterIn.PATH
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderDetailsOutputDTO.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example Response",
                                                    value = "{" +
                                                            "\"id\": 1," +
                                                            "\"quantity\": 2," +
                                                            "\"subtotal\": 250," +
                                                            "\"product\": { \"id\" : 1 }," +
                                                            "\"order\": { \"id\" : 1 }" +
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
                                            example = "{\"localDateTime\":\"2024-12-13T15:30:00\"," +
                                                    " \"status\" : \"404\"," +
                                                    " \"error\" : \"Not Found\"," +
                                                    " \"message\" : \"Order Details with ID 2 not found\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"localDateTime\":\"2024-12-13T05:30:00\"," +
                                                    " \"status\" : \"400\"," +
                                                    " \"error\" : \"NullPointerException\"," +
                                                    " \"message\" : \"Order Details ID cannot be null.\"}"
                                    )
                            )
                    )
            }
    )
    ResponseEntity<OrderDetailsOutputDTO> getOrderDetails(@PathVariable Long id);
}
