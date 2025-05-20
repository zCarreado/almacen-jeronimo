package gestorDeInventariosYVentas.example.controller.controllerDoc;

import gestorDeInventariosYVentas.example.dto.input.CategoryInputDTO;
import gestorDeInventariosYVentas.example.dto.output.CategoryOutputDTO;
import gestorDeInventariosYVentas.example.dto.output.ProductOutputDTO;
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

public interface CategoryRestControllerDoc {

    @Operation(
            summary = "Create category",
            description = "This method allows creating a new category.",
            requestBody = @RequestBody(
                    description = "requires receiving the following attributes: name, description",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoryInputDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Example input",
                                            value = "{" +
                                                    "\"name\":\"accessories\"," +
                                                    "\"description\":\"peripherals for consoles\"" +
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
                                    schema = @Schema(implementation = CategoryOutputDTO.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example response",
                                                    value = "{" +
                                                            "\"id\": 1," +
                                                            "\"name\":\"accessories\"," +
                                                            "\"description\":\"peripherals for consoles\"" +
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
    ResponseEntity<CategoryOutputDTO> createCategory(@org.springframework.web.bind.annotation.RequestBody CategoryInputDTO categoryInputDTO);

    @Operation(
            summary = "Get products by category",
            description = "This method returns all products that belong to a category.",
            parameters = {
                @Parameter(
                    name = "id",
                    description = "The unique ID of the category to retrieve. This ID corresponds to an existing category in the system.",
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
                                                    value = "["+
                                                            "{" +
                                                            "\"id\": 1," +
                                                            "\"name\":\"control play station\"," +
                                                            "\"description\":\"wireless control\"," +
                                                            "\"price\": 220.0," +
                                                            "\"stock\": 15," +
                                                            "\"category\": 1," +
                                                            "\"creationDate\":\"2025-01-25T15:30:00\"" +
                                                            "},"+
                                                            "{" +
                                                            "\"id\": 2," +
                                                            "\"name\":\"headphones\"," +
                                                            "\"description\":\"sony brand headphones\"," +
                                                            "\"price\": 150.0," +
                                                            "\"stock\": 8," +
                                                            "\"category\": 1," +
                                                            "\"creationDate\":\"2025-01-25T15:30:00\"" +
                                                            "}"+
                                                            "]"
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
                                                    " \"error\" : \"NullPointerException\"," +
                                                    " \"message\" : \"Category ID cannot be null.\"}"
                                    )
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
                                                    " \"message\" : \"Category with ID 2 not found\"}"
                                    )
                            )
                    )
            }
    )
    ResponseEntity<List<ProductOutputDTO>> getProductsByCategory(@PathVariable Long id);

    @Operation(
            summary = "Get all categories",
            description = "This method returns all categories that have been created.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CategoryOutputDTO.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Example response",
                                                    value = "["+
                                                            "{" +
                                                            "\"id\": 1," +
                                                            "\"name\":\"accessories\"," +
                                                            "\"description\":\"peripherals for consoles\"" +
                                                            "},"+
                                                            "{" +
                                                            "\"id\": 2," +
                                                            "\"name\":\"video games\"," +
                                                            "\"description\":\"video games for console\"" +
                                                            "}"+
                                                            "]"
                                            )
                                    }
                            )
                    )
            }
    )
    ResponseEntity<List<CategoryOutputDTO>> getAllCategories();
}
