package com.ciclomotos.ciclomotos.repository;

import com.ciclomotos.ciclomotos.model.Venta;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    @Query("SELECT v FROM Venta v LEFT JOIN FETCH v.cliente LEFT JOIN FETCH v.detalles d LEFT JOIN FETCH d.producto WHERE v.id = :id")
    Venta findVentaWithClienteAndDetalles(@Param("id") Long id);

     @Query("SELECT v FROM Venta v WHERE v.fecha BETWEEN :fechaInicio AND :fechaFin")
    List<Venta> findVentasByFecha(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                 @Param("fechaFin") LocalDateTime fechaFin);
}
