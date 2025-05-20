package com.ciclomotos.ciclomotos.service;

import com.ciclomotos.ciclomotos.model.Proveedor;
import com.ciclomotos.ciclomotos.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    public Proveedor crearProveedor(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    public List<Proveedor> obtenerProveedores() {
        return proveedorRepository.findAll();
    }

    public Optional<Proveedor> obtenerProveedorPorId(Long id) {
        return proveedorRepository.findById(id);
    }

    public Proveedor actualizarProveedor(Long id, Proveedor proveedor) {
        if (proveedorRepository.existsById(id)) {
            proveedor.setId(id);
            return proveedorRepository.save(proveedor);
        }
        return null;
    }

    public void eliminarProveedor(Long id) {
        proveedorRepository.deleteById(id);
    }
}
