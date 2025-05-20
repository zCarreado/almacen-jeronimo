package gestorDeInventariosYVentas.example.service.impl;

import gestorDeInventariosYVentas.example.dto.input.OrderInputDTO;
import gestorDeInventariosYVentas.example.dto.output.CustomerOutputDTO;
import gestorDeInventariosYVentas.example.dto.output.OrderDetailsOutputDTO;
import gestorDeInventariosYVentas.example.dto.output.OrderOutputDTO;
import gestorDeInventariosYVentas.example.mapper.CustomerMapper;
import gestorDeInventariosYVentas.example.mapper.OrderDetailsMapper;
import gestorDeInventariosYVentas.example.mapper.OrderMapper;
import gestorDeInventariosYVentas.example.model.Customer;
import gestorDeInventariosYVentas.example.model.Order;
import gestorDeInventariosYVentas.example.model.OrderDetails;
import gestorDeInventariosYVentas.example.repository.CustomerRepository;
import gestorDeInventariosYVentas.example.repository.OrderDetailsRepository;
import gestorDeInventariosYVentas.example.repository.OrderRepository;
import gestorDeInventariosYVentas.example.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;
    private final OrderDetailsMapper orderDetailsMapper;

    public OrderServiceImpl(OrderMapper orderMapper, OrderRepository orderRepository,
                            OrderDetailsRepository orderDetailsRepository, CustomerMapper customerMapper,
                            CustomerRepository customerRepository, OrderDetailsMapper orderDetailsMapper){
        this.orderMapper = orderMapper;
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.customerMapper = customerMapper;
        this.customerRepository = customerRepository;
        this.orderDetailsMapper = orderDetailsMapper;
    }

    @Override
    @Transactional
    public OrderOutputDTO createOrder(OrderInputDTO orderInputDTO) {

        Order order = orderMapper.toEntity(orderInputDTO);

        Customer customer = customerRepository.findById(orderInputDTO.getCustomer().getId())
                .orElseThrow(() -> new EntityNotFoundException("Customer with ID " + orderInputDTO.getCustomer().getId() + " not found"));

        order.setCustomer(customer);

        customer.getOrders().add(order);

        orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    @Override
    public void calculateTotal(Long id) {
        Objects.requireNonNull(id,"ID cannot be null");

        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Order with ID " + id + " not found"));

        double sum =  order.getOrderDetailsList().stream()
                .mapToDouble(OrderDetails::getSubTotal)
                .sum();

        order.setTotal(sum * 1.19);

        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void addOrderDetail(Long idOrder,Long idOrderDetails) {

        Objects.requireNonNull(idOrder,"ID cannot be null");
        Objects.requireNonNull(idOrderDetails,"ID cannot be null");

        Order order = orderRepository.findById(idOrder)
                .orElseThrow(()-> new EntityNotFoundException("Order with ID " + idOrder + " not found"));

        OrderDetails orderDetails = orderDetailsRepository.findById(idOrderDetails)
                .orElseThrow(()->new EntityNotFoundException("Order Details with " + idOrderDetails + " not found"));

        if(!order.getOrderDetailsList().contains(orderDetails)){

            order.getOrderDetailsList().add(orderDetails);
            orderRepository.save(order);
        } else {
            throw  new IllegalStateException("Order Details is already associated with the Order");
        }
    }

    @Override
    @Transactional
    public void removeOrderDetails(Long idOrder,Long idOrderDetails) {

        Objects.requireNonNull(idOrder,"ID cannot be null");
        Objects.requireNonNull(idOrderDetails,"ID cannot be null");

        Order order = orderRepository.findById(idOrder)
                .orElseThrow(()-> new EntityNotFoundException("Order with ID " + idOrder + " not found"));

        OrderDetails orderDetails = orderDetailsRepository.findById(idOrderDetails)
                .orElseThrow(()-> new EntityNotFoundException("Order Details with " + idOrderDetails + " not found"));

        if (order.getOrderDetailsList().contains(orderDetails)){

            order.getOrderDetailsList().remove(orderDetails);
            orderRepository.save(order);
        }else{
            throw new IllegalArgumentException("The specified OrderDetails does not exist in the list of this order.");
        }
    }

    @Override
    public CustomerOutputDTO getCustomer(Long id) {
        Objects.requireNonNull(id,"ID cannot be null");

        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new  EntityNotFoundException("Order with ID " + id + " not found"));

        Customer customer = customerRepository.findById(order.getCustomer().getId())
                .orElseThrow(()-> new  EntityNotFoundException("Customer with ID " + id + " not found"));

        return customerMapper.toDto(customer);
    }

    @Override
    public OrderOutputDTO getOrder(Long id) {
        Objects.requireNonNull(id,"ID cannot be null");

        Order order = orderRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Order with ID " + id + " not found"));

        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderDetailsOutputDTO> getOrdersDetailsByOrder(Long id) {
        Objects.requireNonNull(id,"ID cannot be null");

        Order order = orderRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Order with ID " + id + " not found"));

        return order.getOrderDetailsList().stream()
                .map(orderDetailsMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderOutputDTO completeOrder(Long id) {
        Objects.requireNonNull(id,"ID cannot be null");

        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Order with ID " + id + " not found"));

        order.setStatus(Order.Status.COMPLETED);

        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderOutputDTO> GetAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }
}
