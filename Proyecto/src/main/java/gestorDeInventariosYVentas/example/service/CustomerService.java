package gestorDeInventariosYVentas.example.service;

import gestorDeInventariosYVentas.example.dto.input.CustomerInputDTO;
import gestorDeInventariosYVentas.example.dto.output.CustomerOutputDTO;
import gestorDeInventariosYVentas.example.dto.output.OrderOutputDTO;

import java.util.List;

public interface CustomerService {

    CustomerOutputDTO createCustomer(CustomerInputDTO customerInputDTO);

    String getFullName(Long id);

    List<OrderOutputDTO> getOrders(Long id);

    CustomerOutputDTO getCustomer(Long id);

    List<CustomerOutputDTO> getAllCustomers();

}
