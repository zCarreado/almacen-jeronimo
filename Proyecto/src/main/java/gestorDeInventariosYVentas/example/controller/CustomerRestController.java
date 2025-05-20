package gestorDeInventariosYVentas.example.controller;

import gestorDeInventariosYVentas.example.controller.controllerDoc.CustomerRestControllerDoc;
import gestorDeInventariosYVentas.example.dto.input.CustomerInputDTO;
import gestorDeInventariosYVentas.example.dto.output.CustomerOutputDTO;
import gestorDeInventariosYVentas.example.dto.output.OrderOutputDTO;
import gestorDeInventariosYVentas.example.service.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@Tag( name = "Customer RestController", description = "Controller for customer")
public class CustomerRestController implements CustomerRestControllerDoc {

    private final CustomerService customerService;

    public CustomerRestController(CustomerService customerService){
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerOutputDTO> createCustomer(@RequestBody CustomerInputDTO customerInputDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(customerInputDTO));
    }

    @GetMapping("/name/{id}")
    public ResponseEntity<String> getFullName(@PathVariable Long id){
        return ResponseEntity.ok(customerService.getFullName(id));
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<List<OrderOutputDTO>> getOrders(@PathVariable Long id){
        return ResponseEntity.ok(customerService.getOrders(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerOutputDTO> getCustomer(@PathVariable Long id){
        return ResponseEntity.ok(customerService.getCustomer(id));
    }

    @GetMapping
    public ResponseEntity<List<CustomerOutputDTO>> getAllCustomers(){
        return ResponseEntity.ok(customerService.getAllCustomers());
    }
}
