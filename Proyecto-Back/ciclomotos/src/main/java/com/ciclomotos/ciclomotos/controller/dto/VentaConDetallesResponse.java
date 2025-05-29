package com.ciclomotos.ciclomotos.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class VentaConDetallesResponse {
    private Long id;
    private LocalDateTime fecha;
    private BigDecimal subtotal;  // <-- nuevo campo
    private BigDecimal iva;       // <-- nuevo campo
    private BigDecimal total;
    private ClienteSimple cliente;
    private List<DetalleVentaSimple> detalles;

    public static class ClienteSimple {
        private Long id;
        private String nombre;

        // getters y setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
    }

    public static class DetalleVentaSimple {
        private Long idProducto;
        private String nombreProducto;
        private BigDecimal precioUnitario;
        private Integer cantidad;
        private BigDecimal subtotal;

        // getters y setters
        public Long getIdProducto() { return idProducto; }
        public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }
        public String getNombreProducto() { return nombreProducto; }
        public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
        public BigDecimal getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
        public BigDecimal getSubtotal() { return subtotal; }
        public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    }

    // getters y setters para la clase principal
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getIva() { return iva; }
    public void setIva(BigDecimal iva) { this.iva = iva; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public ClienteSimple getCliente() { return cliente; }
    public void setCliente(ClienteSimple cliente) { this.cliente = cliente; }
    public List<DetalleVentaSimple> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVentaSimple> detalles) { this.detalles = detalles; }
}
