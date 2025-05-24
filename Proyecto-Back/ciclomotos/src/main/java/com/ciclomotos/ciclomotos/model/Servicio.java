package com.ciclomotos.ciclomotos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Servicio {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "servicio_seq")
    @SequenceGenerator(name = "servicio_seq", sequenceName = "SEQ_SERVICIO", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre", length = 100)
    private String nombre;
    
    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "fecha_ingreso")
    private LocalDateTime fechaIngreso;

    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;

    @Column(name = "estado", length = 50)
    private String estado;

    @Column(name = "costo", precision = 10, scale = 2)
    private java.math.BigDecimal costo;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
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

    public java.math.BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(java.math.BigDecimal costo) {
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
