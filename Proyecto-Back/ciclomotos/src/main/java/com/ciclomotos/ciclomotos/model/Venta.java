package com.ciclomotos.ciclomotos.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "venta_seq")
    @SequenceGenerator(name = "venta_seq", sequenceName = "SEQ_VENTA", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total;
    
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonIgnoreProperties({"ventas"})
    private Cliente cliente;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties("venta")
    private java.util.List<DetalleVenta> detalles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public java.util.List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(java.util.List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }

    @Override
    public String toString() {
        return "Venta{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", total=" + total +
                ", cliente=" + (cliente != null ? cliente.getId() : null) +
                '}';
    }

}
