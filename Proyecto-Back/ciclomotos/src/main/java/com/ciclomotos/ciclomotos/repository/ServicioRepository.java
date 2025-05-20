package com.ciclomotos.ciclomotos.repository;

import com.ciclomotos.ciclomotos.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {
}
