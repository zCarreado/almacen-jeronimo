package gestorDeInventariosYVentas.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userName;
    private String password;

    @Column(name = "is_enabled")
    private boolean isEnabled;

    @Column(name = "account_no_expired")
    private boolean accountNoExpired;

    @Column(name = "account_no_locked")
    private boolean accountNoLocked;

    @Column(name = "credencial_no_expired")
    private boolean credencialNoExpired;

    @ElementCollection(fetch = FetchType.EAGER) // Para permitir mapeo de colecciones simples
    @Enumerated(EnumType.STRING) // Guardar el nombre del enum como texto
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id")) // Tabla para roles
    @Column(name = "role") // Columna que almacena los roles
    private Set<rolesEnum> roles = new HashSet<>();

    public enum rolesEnum{
        ADMIN,
        CASHIER,
        SALES_MANAGER
    }
}


