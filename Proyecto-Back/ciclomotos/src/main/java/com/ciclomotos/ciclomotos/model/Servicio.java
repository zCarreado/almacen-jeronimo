package com.ciclomotos.ciclomotos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Servicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;
    private LocalDateTime fechaIngreso;
    private LocalDateTime fechaEntrega;
    private String estado;
    private Double costo;

    @ManyToOne
    private Cliente cliente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDateTime getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDateTime fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return "Servicio{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                ", fechaIngreso=" + fechaIngreso +
                ", fechaEntrega=" + fechaEntrega +
                ", estado='" + estado + '\'' +
                ", costo=" + costo +
                ", cliente=" + (cliente != null ? cliente.getId() : null) +
                '}';
    }
}
