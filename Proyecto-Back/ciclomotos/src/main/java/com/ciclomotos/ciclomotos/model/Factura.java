package com.ciclomotos.ciclomotos.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "factura")
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "factura_seq")
    @SequenceGenerator(name = "factura_seq", sequenceName = "seq_factura", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "estado_pago")
    private String estadoPago;

    @Column(name = "numero")
    private String numero;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
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

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    @Override
    public String toString() {
        return "Factura{" +
                "id=" + id +
                ", numero='" + numero + '\'' +
                ", fecha=" + fecha +
                ", total=" + total +
                ", estadoPago='" + estadoPago + '\'' +
                ", venta=" + (venta != null ? venta.getId() : null) +
                '}';
    }
}
