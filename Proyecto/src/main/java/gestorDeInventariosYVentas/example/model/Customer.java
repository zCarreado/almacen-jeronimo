package gestorDeInventariosYVentas.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "customers")
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    private String address;

    @Column(name = "phone number")
    private String phoneNumber;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders;

    public Customer(String phoneNumber, String address, String email, String name) {
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
        this.name = name;
    }
}
