package com.ciclomotos.ciclomotos.controller;

import com.ciclomotos.ciclomotos.model.Servicio;
import com.ciclomotos.ciclomotos.service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/servicios")
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

    @PostMapping("/crearServicio")
    public Servicio crearServicio(@RequestBody Servicio servicio) {
        return servicioService.crearServicio(servicio);
    }

    @GetMapping("/obtenerServicios")
    public List<Servicio> obtenerServicios() {
        return servicioService.obtenerServicios();
    }

    @GetMapping("/obtenerServicio/{id}")
    public Optional<Servicio> obtenerServicioPorId(@PathVariable Long id) {
        return servicioService.obtenerServicioPorId(id);
    }

    @PutMapping("/actualizarServicio/{id}")
    public Servicio actualizarServicio(@PathVariable Long id, @RequestBody Servicio servicio) {
        return servicioService.actualizarServicio(id, servicio);
    }

    @DeleteMapping("/eliminarServicio/{id}")
    public void eliminarServicio(@PathVariable Long id) {
        servicioService.eliminarServicio(id);
    }
}
