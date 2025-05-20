package gestorDeInventariosYVentas.example.controller.controllerDoc;

import gestorDeInventariosYVentas.example.dto.input.CustomerInputDTO;
import gestorDeInventariosYVentas.example.dto.output.CustomerOutputDTO;
import gestorDeInventariosYVentas.example.dto.output.OrderOutputDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface CustomerRestControllerDoc {
    @Operation(
            summary = "Create customer",
            description = "This method allows creating a new customer.",
            requestBody = @RequestBody(
                    description = "requires receiving the following attributes: name, email, address, phoneNumber",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomerInputDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Example input",
                                            value = "{" +
                                                    "\"name\":\"Julian Contreras\"," +
                                                    "\"email\":\"contreju@gmail.com\"," +
                                                    "\"address\":\"mz 22 cs 3\"," +
                                                    "\"phoneNumber\":\"564897\"" +
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
    ResponseEntity<CustomerOutputDTO> createCustomer(@RequestBody CustomerInputDTO customerInputDTO);

    @Operation(
            summary = "Get full name",
            description = "This method allows returning the full name of the customer.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "The unique ID of the customer to retrieve. This ID corresponds" +
                                    " to an existing customer in the system..",
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
                                                    value = "name: Julian Contreras"
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
                                                    " \"message\" : \"Customer with ID 2 not found\"}"
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
                                                    " \"message\" : \"Customer ID cannot be null.\"}"
                                    )
                            )
                    )
            }
    )
    ResponseEntity<String> getFullName(@PathVariable Long id);

    @Operation(
            summary = "get orders",
            description = "Returns all orders that are linked to the customer.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "The unique ID of the customer to retrieve. This ID corresponds" +
                                    " to an existing customer in the system.",
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
                                                    name = "Example Response",
                                                    value = "[" +
                                                            "{" +
                                                            "\"id\": 1," +
                                                            "\"date\":\"2025-01-25T15:30:00\"," +
                                                            "\"total\": 535.5," +
                                                            "\"status\": \"PENDING\"," +
                                                            "\"customerId\": 1" +
                                                            "}," +
                                                            "{" +
                                                            "\"id\": 2," +
                                                            "\"date\":\"2025-01-25T15:30:00\"," +
                                                            "\"total\": 892.5," +
                                                            "\"status\": \"PENDING\"," +
                                                            "\"customerId\": 1" +
                                                            "}" +
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
                                                    " \"message\" : \"Customer with ID 2 not found\"}"
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
                                                    " \"message\" : \"Customer ID cannot be null.\"}"
                                    )
                            )
                    )
            }
    )
    ResponseEntity<List<OrderOutputDTO>> getOrders(@PathVariable Long id);

    @Operation(
            summary = "get customer",
            description = "Returns the DTO with the customer's data.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "The unique ID of the customer to retrieve. This ID corresponds" +
                                    " to an existing customer in the system.",
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
                                                    name = "Example Response",
                                                    value = "{" +
                                                            "\"id\": 1," +
                                                            "\"name\":\"Julian Contreras\"," +
                                                            "\"email\":\"contreju@gmail.com\"," +
                                                            "\"address\": \"mz 22 cs 3\"," +
                                                            "\"phoneNumber\": \"564897\"" +
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
                                                    " \"message\" : \"Customer with ID 2 not found\"}"
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
                                                    " \"message\" : \"Customer ID cannot be null.\"}"
                                    )
                            )
                    )
            }
    )
    ResponseEntity<CustomerOutputDTO> getCustomer(@PathVariable Long id);

    @Operation(
            summary = "get all customers",
            description = "Returns all customers that have been created.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CustomerOutputDTO.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example Response",
                                                    value = "["+
                                                            "{" +
                                                            "\"id\": 1," +
                                                            "\"name\":\"Julian Contreras\"," +
                                                            "\"email\":\"contreju@gmail.com\"," +
                                                            "\"address\": \"mz 22 cs 3\"," +
                                                            "\"phoneNumber\": \"564897\"" +
                                                            "}," +
                                                            "{" +
                                                            "\"id\": 2," +
                                                            "\"name\":\"Catalina Velez\"," +
                                                            "\"email\":\"catalina22@yahoo.com\"," +
                                                            "\"address\": \"mz 11 cs 12\"," +
                                                            "\"phoneNumber\": \"564897\"" +
                                                            "}"+
                                                            "]"
                                            )
                                    }
                            )
                    )
            }
    )
    ResponseEntity<List<CustomerOutputDTO>> getAllCustomers();
}
