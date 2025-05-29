package com.ciclomotos.ciclomotos.controller;

import com.ciclomotos.ciclomotos.model.TipoUsuario;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

    @RestController
    @RequestMapping("/api/tipoUsuario")
    public class TipoController {

        @GetMapping
        public TipoUsuario[] obtenerTipoUsuario() {
            return TipoUsuario.values();
        }
    }

