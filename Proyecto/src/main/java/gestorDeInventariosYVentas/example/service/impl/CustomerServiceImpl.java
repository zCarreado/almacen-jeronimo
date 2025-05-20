package gestorDeInventariosYVentas.example.service.impl;

import gestorDeInventariosYVentas.example.dto.input.CustomerInputDTO;
import gestorDeInventariosYVentas.example.dto.output.CustomerOutputDTO;
import gestorDeInventariosYVentas.example.dto.output.OrderOutputDTO;
import gestorDeInventariosYVentas.example.mapper.CustomerMapper;
import gestorDeInventariosYVentas.example.mapper.OrderMapper;
import gestorDeInventariosYVentas.example.model.Customer;
import gestorDeInventariosYVentas.example.repository.CustomerRepository;
import gestorDeInventariosYVentas.example.service.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final OrderMapper orderMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository,CustomerMapper customerMapper, OrderMapper orderMapper){
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.orderMapper = orderMapper;
    }

    @Override
    public CustomerOutputDTO createCustomer(CustomerInputDTO customerInputDTO) {

        Customer customer = customerMapper.toEntity(customerInputDTO);

        customerRepository.save(customer);

        return customerMapper.toDto(customer);
    }

    @Override
    public String getFullName(Long id) {
        Objects.requireNonNull(id,"Customer ID cannot be null.");

        Customer customer = customerRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Customer with ID " + id + " not found"));

        return "name: " + customer.getName();
    }

    @Override
    public List<OrderOutputDTO> getOrders(Long id) {
        Objects.requireNonNull(id,"Customer ID cannot be null.");

        Customer customer = customerRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Customer with ID " + id + " not found"));

        return customer.getOrders().stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerOutputDTO getCustomer(Long id) {
        Objects.requireNonNull(id,"Customer ID cannot be null.");

        Customer customer = customerRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Customer with ID " + id + " not found"));

        return customerMapper.toDto(customer);
    }

    @Override
    public List<CustomerOutputDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toDto)
                .collect(Collectors.toList());
    }
}
