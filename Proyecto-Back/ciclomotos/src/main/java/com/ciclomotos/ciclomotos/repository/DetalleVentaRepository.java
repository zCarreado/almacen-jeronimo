package com.ciclomotos.ciclomotos.repository;

import com.ciclomotos.ciclomotos.model.DetalleVenta;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
    @Query("SELECT d FROM DetalleVenta d JOIN FETCH d.producto WHERE d.venta.id = :ventaId")
    List<DetalleVenta> findByVentaIdWithProducto(@Param("ventaId") Long ventaId);

    @Query("SELECT p.categoria.nombre, SUM(d.cantidad) " +
            "FROM DetalleVenta d JOIN d.producto p " +
            "WHERE d.venta.fecha BETWEEN :fechaInicio AND :fechaFin " +
            "GROUP BY p.categoria.nombre")
    List<Object[]> countVentasByCategoria(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT p.categoria.nombre, " +
            "SUM(d.cantidad) as ventas, " +
            "SUM(d.subtotal) as ganancias " +
            "FROM DetalleVenta d JOIN d.producto p " +
            "WHERE d.venta.fecha BETWEEN :fechaInicio AND :fechaFin " +
            "GROUP BY p.categoria.nombre")
    List<Object[]> getEstadisticasByCategoria(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT COALESCE(SUM(d.subtotal), 0) " +
            "FROM DetalleVenta d " +
            "WHERE d.venta.fecha BETWEEN :fechaInicio AND :fechaFin")
    BigDecimal sumSubtotalByFecha(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT p.categoria.nombre, COUNT(DISTINCT d.venta.id), SUM(d.subtotal) " +
           "FROM DetalleVenta d JOIN d.producto p " +
           "WHERE d.venta.fecha BETWEEN :fechaInicio AND :fechaFin " +
           "GROUP BY p.categoria.nombre")
    List<Object[]> getVentasPorCategoria(
            @Param("fechaInicio") java.time.LocalDateTime fechaInicio,
            @Param("fechaFin") java.time.LocalDateTime fechaFin);

    @Query("SELECT p.categoria.nombre, COUNT(d), SUM(d.subtotal) " +
            "FROM DetalleVenta d JOIN d.producto p " +
            "WHERE d.venta.fecha BETWEEN :inicio AND :fin " +
            "GROUP BY p.categoria.nombre")
    List<Object[]> getDetallesAgrupadosPorCategoria(@Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);

}
