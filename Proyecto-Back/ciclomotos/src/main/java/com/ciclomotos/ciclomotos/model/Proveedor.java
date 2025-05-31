package com.ciclomotos.ciclomotos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "proveedor")
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "proveedor_seq")
    @SequenceGenerator(name = "proveedor_seq", sequenceName = "seq_proveedor", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "El nombre del proveedor es obligatorio")
    @Column(name = "nombre")
    private String nombre;

    @Email(message = "El email no es válido")
    @NotBlank(message = "El email es obligatorio")
    @Column(name = "email")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Column(name = "telefono")
    private String telefono;

    // Relación opcional con productos
    @OneToMany(mappedBy = "proveedor")
    @JsonIgnoreProperties({"proveedor"})
    private List<Producto> productos;

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    @Override
    public String toString() {
        return "Proveedor{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}
