package gestorDeInventariosYVentas.example.controller;

import gestorDeInventariosYVentas.example.controller.controllerDoc.OrderDetailsRestControllerDoc;
import gestorDeInventariosYVentas.example.dto.input.OrderDetailsInputDTO;
import gestorDeInventariosYVentas.example.dto.output.OrderDetailsOutputDTO;
import gestorDeInventariosYVentas.example.dto.output.ProductOutputDTO;
import gestorDeInventariosYVentas.example.model.Product;
import gestorDeInventariosYVentas.example.service.OrderDetailsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders-details")
@Tag( name = "Order Details RestController", description = "Controller for order details")
public class OrderDetailsRestController implements OrderDetailsRestControllerDoc {

    private final OrderDetailsService orderDetailsService;

    public OrderDetailsRestController(OrderDetailsService orderDetailsService){
        this.orderDetailsService = orderDetailsService;
    }

    @PostMapping
    public ResponseEntity<OrderDetailsOutputDTO> createOrderDetails(@RequestBody OrderDetailsInputDTO orderDetailsInputDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderDetailsService.createOrderDetails(orderDetailsInputDTO));
    }

    //No se utiliza directamente, es invocada por otro método
    @GetMapping("/subtotal")
    public ResponseEntity<Double> calculateSubTotal(Product product,@PathVariable Long quantity){
        return ResponseEntity.ok(orderDetailsService.calculateSubTotal(product,quantity));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ProductOutputDTO> getProduct(@PathVariable Long id){
        return ResponseEntity.ok(orderDetailsService.getProduct(id));
    }

    @GetMapping("/quantity/{id}")
    public ResponseEntity<String> getQuantity(@PathVariable Long id){
        return ResponseEntity.ok(orderDetailsService.getQuantity(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailsOutputDTO> getOrderDetails(@PathVariable Long id){
        return ResponseEntity.ok(orderDetailsService.getOrderDetails(id));
    }
}
