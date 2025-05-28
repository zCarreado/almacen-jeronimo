package com.ciclomotos.ciclomotos.model;

import jakarta.persistence.*;
import java.util.List;
import jakarta.persistence.OneToMany;

@Entity
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String contacto;
    private String telefono;
    private String correo;
    private String direccion;

    @OneToMany(mappedBy = "proveedor")
    private List<Producto> productos;

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

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String email) {
        this.correo = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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
                ", contacto='" + contacto + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + correo + '\'' +
                ", direccion='" + direccion + '\'' +
                ", productos=" + productos +
                '}';
    }
}
