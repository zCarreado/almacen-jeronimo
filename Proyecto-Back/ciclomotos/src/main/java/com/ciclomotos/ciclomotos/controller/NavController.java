package com.ciclomotos.ciclomotos.controller;

import java.security.Security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.ciclomotos.ciclomotos.SecurityConfig;

@Controller
public class NavController {
    SecurityConfig securityConfig;  

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "cliente-registro";
    }
}
