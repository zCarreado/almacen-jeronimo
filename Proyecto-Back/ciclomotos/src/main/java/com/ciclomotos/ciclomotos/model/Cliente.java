package com.ciclomotos.ciclomotos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "cliente")
public class Cliente {
    @Id
    @NotNull(message = "El ID es obligatorio")
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(name = "nombre")
    private String nombre;

    @Email(message = "El email no es válido")
    @NotBlank(message = "El email es obligatorio")
    @Column(name = "email")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Column(name = "telefono")
    private String telefono;

    @NotBlank(message = "La dirección es obligatoria")
    @Column(name = "direccion")
    private String direccion;

    @OneToMany(mappedBy = "cliente")
    @JsonIgnoreProperties({"cliente"})
    private List<Venta> ventas;

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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<Venta> getVentas() {
        return ventas;
    }

    public void setVentas(List<Venta> ventas) {
        this.ventas = ventas;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", direccion='" + direccion + '\'' +
                ", ventas=" + ventas +
                '}';
    }
}
