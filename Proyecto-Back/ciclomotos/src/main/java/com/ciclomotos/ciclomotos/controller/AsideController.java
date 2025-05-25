package com.ciclomotos.ciclomotos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AsideController {
    @GetMapping("/venta")
    public String mostrarVenta() {
        return "venta";
    }
    @GetMapping("/clientes")
    public String mostrarClientes() {
        return "registro-cliente";
    }
    @GetMapping("/inventario")
    public String mostrarInventario() {
        return "inventario";
    }
    @GetMapping("/reportes")
    public String mostrarReportes() {
        return "reportes";
    }

    @GetMapping("/usuarios")
    public String mostrarUsuiario() {
        return "registro-empleado";
    }
    @GetMapping("/servicios")
    public String mostrarServicios() {
        return "servicios";
    }
}
