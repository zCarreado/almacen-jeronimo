package gestorDeInventariosYVentas.example.mapper;

import gestorDeInventariosYVentas.example.dto.input.CustomerInputDTO;
import gestorDeInventariosYVentas.example.dto.output.CustomerOutputDTO;
import gestorDeInventariosYVentas.example.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomerOutputDTO toDto (Customer customer){

        return new CustomerOutputDTO(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getAddress(),
                customer.getPhoneNumber()
        );
    }

    public Customer toEntity (CustomerInputDTO customerInputDTO){

        return new Customer(
                customerInputDTO.getPhoneNumber(),
                customerInputDTO.getAddress(),
                customerInputDTO.getEmail(),
                customerInputDTO.getName()
        );
    }
}
