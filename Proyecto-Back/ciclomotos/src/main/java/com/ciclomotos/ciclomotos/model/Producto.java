package com.ciclomotos.ciclomotos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;

@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "producto_seq")
    @SequenceGenerator(name = "producto_seq", sequenceName = "SEQ_PRODUCTO", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "precio", precision = 10, scale = 2)
    private java.math.BigDecimal precio;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "Stock_minimo")
    private Integer stockMinimo;

    @ManyToOne
    @JoinColumn(name = "categoria")
    @JsonIgnoreProperties({"productos"})
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    @JsonIgnoreProperties({"productos"})
    private Proveedor proveedor;

    // Getters y Setters
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

    public java.math.BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(java.math.BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", categoria=" + (categoria != null ? categoria.getId() : null) +
                ", precio=" + precio +
                ", cantidad=" + cantidad +
                ", stockMinimo=" + stockMinimo +
                ", proveedor=" + proveedor +
                '}';
    }
}
