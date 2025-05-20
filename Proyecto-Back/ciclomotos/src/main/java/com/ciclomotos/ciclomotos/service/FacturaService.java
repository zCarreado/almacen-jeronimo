package com.ciclomotos.ciclomotos.service;

import com.ciclomotos.ciclomotos.model.Factura;
import com.ciclomotos.ciclomotos.repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    public Factura crearFactura(Factura factura) {
        return facturaRepository.save(factura);
    }

    public List<Factura> obtenerFacturas() {
        return facturaRepository.findAll();
    }

    public Optional<Factura> obtenerFacturaPorId(Long id) {
        return facturaRepository.findById(id);
    }

    public Factura actualizarFactura(Long id, Factura factura) {
        if (facturaRepository.existsById(id)) {
            factura.setId(id);
            return facturaRepository.save(factura);
        }
        return null;
    }

    public void eliminarFactura(Long id) {
        facturaRepository.deleteById(id);
    }
}
