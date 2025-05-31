package com.ciclomotos.ciclomotos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "venta")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "venta_seq")
    @SequenceGenerator(name = "venta_seq", sequenceName = "seq_venta", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "La fecha es obligatoria")
    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @NotNull(message = "El subtotal es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El subtotal no puede ser negativo")
    @Column(name = "subtotal", precision = 10, scale = 2)
    private BigDecimal subtotal;

    @NotNull(message = "El IVA es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El IVA no puede ser negativo")
    @Column(name = "iva", precision = 10, scale = 2)
    private BigDecimal iva;

    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El total no puede ser negativo")
    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total;

    @NotNull(message = "El cliente es obligatorio")
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonIgnoreProperties({"ventas"})
    private Cliente cliente;

    @NotNull(message = "Los detalles de la venta son obligatorios")
    @Size(min = 1, message = "Debe haber al menos un detalle de venta")
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("venta")
    private java.util.List<DetalleVenta> detalles;

    // Getters y setters

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

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
                ", subtotal=" + subtotal +
                ", iva=" + iva +
                ", total=" + total +
                ", cliente=" + (cliente != null ? cliente.getId() : null) +
                '}';
    }
}
