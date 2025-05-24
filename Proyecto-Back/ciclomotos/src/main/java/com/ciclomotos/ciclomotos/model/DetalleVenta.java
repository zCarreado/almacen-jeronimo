package com.ciclomotos.ciclomotos.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "detalle_venta_seq")
    @SequenceGenerator(name = "detalle_venta_seq", sequenceName = "SEQ_DETALLE_VENTA", allocationSize = 1)
    private Long id;

    @ManyToOne
    private Venta venta;

    @ManyToOne
    private Producto producto;

    private Integer cantidad;
    @Column(name = "precio_unitario", columnDefinition = "NUMBER(10,2)")
    private BigDecimal precioUnitario;
    @Column(name = "subtotal", columnDefinition = "NUMBER(10,2)")
    private BigDecimal subtotal;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Venta getVenta() { return venta; }
    public void setVenta(Venta venta) { this.venta = venta; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    @Override
    public String toString() {
        return "DetalleVenta{" +
                "id=" + id +
                ", producto=" + (producto != null ? producto.getId() : null) +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                ", subtotal=" + subtotal +
                '}';
    }
}
