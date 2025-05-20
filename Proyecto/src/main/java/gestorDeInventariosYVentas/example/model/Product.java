package gestorDeInventariosYVentas.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "products")
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Long stock;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "creation date")
    private LocalDateTime creationDate;

    public Product(LocalDateTime creationDate, Category category, Long stock, Double price, String description, String name) {
        this.creationDate = creationDate;
        this.category = category;
        this.stock = stock;
        this.price = price;
        this.description = description;
        this.name = name;
    }
}
