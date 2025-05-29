package com.ciclomotos.ciclomotos.repository;

import com.ciclomotos.ciclomotos.model.DetalleVenta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
    @Query("SELECT d FROM DetalleVenta d JOIN FETCH d.producto WHERE d.venta.id = :ventaId")
    List<DetalleVenta> findByVentaIdWithProducto(@Param("ventaId") Long ventaId);
}
