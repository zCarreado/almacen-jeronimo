package com.ciclomotos.ciclomotos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavController {

    @GetMapping("/")
    public String mostrarInicio() {
        return "login";
    }
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "cliente-registro";
    }
}
