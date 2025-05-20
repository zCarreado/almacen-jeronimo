package gestorDeInventariosYVentas.example.controller.controllerDoc;

import gestorDeInventariosYVentas.example.dto.input.OrderInputDTO;
import gestorDeInventariosYVentas.example.dto.output.CustomerOutputDTO;
import gestorDeInventariosYVentas.example.dto.output.OrderDetailsOutputDTO;
import gestorDeInventariosYVentas.example.dto.output.OrderOutputDTO;
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

public interface OrderRestControllerDoc {

    @Operation(
            summary = "Create order",
            description = "This method allows creating a new order.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "requires receiving the following attributes: name, email, address, phoneNumber",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderInputDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Example input",
                                            value = "{" +
                                                    "\"date\":\"2025-01-25T15:30:00\"," +
                                                    "\"customer\": { \"id\" : 1 }" +
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
                                    schema = @Schema(implementation = OrderOutputDTO.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example response",
                                                    value = "{" +
                                                            "\"id\": 1," +
                                                            "\"date\":\"2025-01-25T15:30:00\"," +
                                                            "\"total\": 892.5," +
                                                            "\"status\":\"PENDING\"," +
                                                            "\"customerId\": 1" +
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
    ResponseEntity<OrderOutputDTO> createOrder(@RequestBody OrderInputDTO orderInputDTO);

    @Operation(
            summary = "Calculate total",
            description = "This method sums the subtotals of the order details and adds the taxes to calculate" +
                    " the total of the order.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "The unique ID of the order to retrieve. This ID corresponds" +
                                    " to an existing order in the system..",
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
                                                    value = "Total calculated successfully."
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
                                                    " \"message\" : \"Order with ID 2 not found\"}"
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
    ResponseEntity<String> calculateTotal(@PathVariable Long id);

    @Operation(
            summary = "Add order details",
            description = "This method allows linking an order detail to a previously created order.",
            parameters = {
                    @Parameter(
                            name = "idOrder",
                            description = "The unique ID of the order to retrieve. This ID corresponds" +
                                    " to an existing order in the system..",
                            example = "2",
                            in = ParameterIn.PATH
                    ),
                    @Parameter(
                            name = "idOrderDetails",
                            description = "The unique ID of the order to retrieve. This ID corresponds" +
                                    " to an existing order in the system..",
                            example = "5",
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
                                                    value = "The orderDetails with ID 5 has been added to the order with ID 2"
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
                                                    " \"message\" : \"Order / Order Details with ID 2 not found\"}"
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
    ResponseEntity<String> addOrderDetail(@PathVariable Long idOrder,@PathVariable Long idOrderDetails);

    @Operation(
            summary = "Remove order details",
            description = "This method allows removing/unlinking an order detail from an order.",
            parameters = {
                    @Parameter(
                            name = "idOrder",
                            description = "The unique ID of the order to retrieve. This ID corresponds" +
                                    " to an existing order in the system..",
                            example = "2",
                            in = ParameterIn.PATH
                    ),
                    @Parameter(
                            name = "idOrderDetails",
                            description = "The unique ID of the order to retrieve. This ID corresponds" +
                                    " to an existing order in the system..",
                            example = "5",
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
                                                    value = "The order details with ID  5 has been removed from the order with ID 2"
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
                                                    " \"message\" : \"Order / Order Details with ID 2 not found\"}"
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
    ResponseEntity<String> removeOrderDetails(@PathVariable Long idOrder,@PathVariable Long idOrderDetails);

    @Operation(
            summary = "Get customer",
            description = "This method returns the customer to whom the order belongs.",
            parameters ={
                @Parameter(
                    name = "id",
                    description = "The unique ID of the order to retrieve. This ID corresponds" +
                            " to an existing order in the system..",
                    example = "2",
                    in = ParameterIn.PATH
                )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerOutputDTO.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example response",
                                                    value = "{" +
                                                            "\"id\": 1," +
                                                            "\"name\":\"Julian Contreras\"," +
                                                            "\"email\":\"contreju@gmail.com\"," +
                                                            "\"address\":\"mz 22 cs 3\"," +
                                                            "\"phoneNumber\":\"564897\"" +
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
                                                    " \"message\" : \"Order / Order Details with ID 2 not found\"}"
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
    ResponseEntity<CustomerOutputDTO> getCustomer(@PathVariable Long id);

    @Operation(
            summary = "Get order",
            description = "This method returns a DTO of the order.",
            parameters ={
                    @Parameter(
                            name = "id",
                            description = "The unique ID of the order to retrieve. This ID corresponds" +
                                    " to an existing order in the system..",
                            example = "2",
                            in = ParameterIn.PATH
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderOutputDTO.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example response",
                                                    value = "{" +
                                                            "\"id\": 2," +
                                                            "\"date\":\"2025-01-25T15:30:00\"," +
                                                            "\"total\": 892.5," +
                                                            "\"status\":\"PENDING\"," +
                                                            "\"customerId\": 1" +
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
                                                    " \"message\" : \"Order with ID 2 not found\"}"
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
    ResponseEntity<OrderOutputDTO> getOrder(@PathVariable Long id);

    @Operation(
            summary = "Get orders details by order ",
            description = "This method returns all order details that are linked to an order.",
            parameters ={
                    @Parameter(
                            name = "id",
                            description = "The unique ID of the order to retrieve. This ID corresponds" +
                                    " to an existing order in the system..",
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
                                                    name = "Example response",
                                                    value = "["+
                                                            "{" +
                                                            "\"id\": 1," +
                                                            "\"quantity\": 2," +
                                                            "\"subtotal\": 250," +
                                                            "\"product\": { \"id\" : 1 }," +
                                                            "\"order\": { \"id\" : 2 }" +
                                                            "},"+
                                                            "{" +
                                                            "\"id\": 3," +
                                                            "\"quantity\": 4," +
                                                            "\"subtotal\": 560," +
                                                            "\"product\": { \"id\" : 5 }," +
                                                            "\"order\": { \"id\" : 2 }" +
                                                            "}"+
                                                            "]"
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
                                                    " \"message\" : \"Order with ID 2 not found\"}"
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
    ResponseEntity<List<OrderDetailsOutputDTO>> getOrdersDetailsByOrder(@PathVariable Long id);

    @Operation(
            summary = "Complete order ",
            description = "This method changes the order status from \"PENDING\" to \"COMPLETE\"",
            parameters ={
                    @Parameter(
                            name = "id",
                            description = "The unique ID of the order to retrieve. This ID corresponds" +
                                    " to an existing order in the system..",
                            example = "2",
                            in = ParameterIn.PATH
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderOutputDTO.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example response",
                                                    value = "{" +
                                                            "\"id\": 2," +
                                                            "\"date\":\"2025-01-25T15:30:00\"," +
                                                            "\"total\": 892.5," +
                                                            "\"status\":\"COMPLETE\"," +
                                                            "\"customerId\": 1" +
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
                                                    " \"message\" : \"Order with ID 2 not found\"}"
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
    ResponseEntity<OrderOutputDTO> completeOrder(@PathVariable Long id);

    @Operation(
            summary = "Get all orders",
            description = "This method returns all order details that are linked to an order.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = OrderOutputDTO.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example response",
                                                    value = "[" +
                                                            "{" +
                                                            "\"id\": 2," +
                                                            "\"date\":\"2025-01-25T15:30:00\"," +
                                                            "\"total\": 892.5," +
                                                            "\"status\":\"PENDING\"," +
                                                            "\"customerId\": 1" +
                                                            "}," +
                                                            "{" +
                                                            "\"id\": 5," +
                                                            "\"date\":\"2025-01-25T15:30:00\"," +
                                                            "\"total\": 99.5," +
                                                            "\"status\":\"COMPLETE\"," +
                                                            "\"customerId\": 1" +
                                                            "}" +
                                                            "]"

                                            )
                                    }
                            )
                    )
            }
    )
    ResponseEntity<List<OrderOutputDTO>> GetAllOrders();
}
