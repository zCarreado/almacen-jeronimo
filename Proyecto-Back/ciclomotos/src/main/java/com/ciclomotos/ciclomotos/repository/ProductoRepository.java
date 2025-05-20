package com.ciclomotos.ciclomotos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ciclomotos.ciclomotos.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
