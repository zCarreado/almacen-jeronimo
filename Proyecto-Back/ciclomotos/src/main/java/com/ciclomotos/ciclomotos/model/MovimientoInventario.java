package com.ciclomotos.ciclomotos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class MovimientoInventario {
    public enum TipoMovimiento {
        ENTRADA, SALIDA, AJUSTE, DEVOLUCION
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Producto producto;

    private LocalDateTime fecha;
    @Enumerated(EnumType.STRING)
    private TipoMovimiento tipoMovimiento;
    private Integer cantidad;
    private String observaciones;

    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }
    public Producto getProducto() { 
        return producto; 
    }
    public void setProducto(Producto producto) { 
        this.producto = producto; 
    }
    public LocalDateTime getFecha() { 
        return fecha; 
    }
    public void setFecha(LocalDateTime fecha) { 
        this.fecha = fecha;
     }
    public TipoMovimiento getTipoMovimiento() { 
        return tipoMovimiento; 
    }
    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) { 
        this.tipoMovimiento = tipoMovimiento; 
    }
    public Integer getCantidad() { 
        return cantidad; 
    }
    public void setCantidad(Integer cantidad) { 
        this.cantidad = cantidad; 
    }
    public String getObservaciones() { 
        return observaciones; 
    }
    public void setObservaciones(String observaciones) { 
        this.observaciones = observaciones; 
    }

    @Override
    public String toString() {
        return "MovimientoInventario{" +
                "id=" + id +
                ", producto=" + (producto != null ? producto.getId() : null) +
                ", fecha=" + fecha +
                ", tipoMovimiento=" + tipoMovimiento +
                ", cantidad=" + cantidad +
                ", observaciones='" + observaciones + '\'' +
                '}';
    }
}
