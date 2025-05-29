package com.ciclomotos.ciclomotos.controller.dto;

import java.util.List;

public class VentaConDetallesRequest {
    private VentaSimple venta;
    private List<DetalleVentaRequest> detalles;

    public VentaSimple getVenta() {
        return venta;
    }

    public void setVenta(VentaSimple venta) {
        this.venta = venta;
    }

    public List<DetalleVentaRequest> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVentaRequest> detalles) {
        this.detalles = detalles;
    }

    public static class VentaSimple {
        private Long id; // para actualización o null para nuevo
        private String fecha;
        private ClienteSimple cliente;

        // getters y setters

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getFecha() {
            return fecha;
        }

        public void setFecha(String fecha) {
            this.fecha = fecha;
        }

        public ClienteSimple getCliente() {
            return cliente;
        }

        public void setCliente(ClienteSimple cliente) {
            this.cliente = cliente;
        }
    }

    public static class ClienteSimple {
        private Long id;
        // más campos si quieres

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    public static class DetalleVentaRequest {
        private Integer cantidad;

        // Solo uno de estos dos debe ser enviado, no ambos
        private ProductoRef producto;
        private ServicioRef servicio;

        public Integer getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }

        public ProductoRef getProducto() {
            return producto;
        }

        public void setProducto(ProductoRef producto) {
            this.producto = producto;
        }

        public ServicioRef getServicio() {
            return servicio;
        }

        public void setServicio(ServicioRef servicio) {
            this.servicio = servicio;
        }
    }

    public static class ProductoRef {
        private Long id;
        public Long getId() {
            return id;
        }
        public void setId(Long id) {
            this.id = id;
        }
    }

    public static class ServicioRef {
        private Long id;
        public Long getId() {
            return id;
        }
        public void setId(Long id) {
            this.id = id;
        }
    }
}
