package com.ciclomotos.ciclomotos.repository;

import com.ciclomotos.ciclomotos.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
}
