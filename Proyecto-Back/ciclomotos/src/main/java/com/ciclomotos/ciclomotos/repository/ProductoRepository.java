package com.ciclomotos.ciclomotos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ciclomotos.ciclomotos.model.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    Optional<Producto> findByNombreIgnoreCase(String nombre);
}
