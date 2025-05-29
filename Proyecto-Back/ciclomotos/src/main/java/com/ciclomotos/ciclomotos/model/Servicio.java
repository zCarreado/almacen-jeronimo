package com.ciclomotos.ciclomotos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "servicio")
public class Servicio {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "servicio_seq")
    @SequenceGenerator(name = "servicio_seq", sequenceName = "seq_servicio", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre", length = 100)
    private String nombre;
    
    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "costo", precision = 10, scale = 2)
    private java.math.BigDecimal costo;

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




    public java.math.BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(java.math.BigDecimal costo) {
        this.costo = costo;
    }



    @Override
    public String toString() {
        return "Servicio{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                ", costo=" + costo +
                '}';
    }
}
