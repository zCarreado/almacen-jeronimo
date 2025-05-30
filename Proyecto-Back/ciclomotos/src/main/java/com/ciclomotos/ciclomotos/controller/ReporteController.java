package com.ciclomotos.ciclomotos.controller;

import com.ciclomotos.ciclomotos.controller.dto.ReporteVentaDTO;
import com.ciclomotos.ciclomotos.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    // Reportes automáticos
    @GetMapping("/hoy")
    public ReporteVentaDTO getReporteHoy() {
        return reporteService.generarReporteHoy();
    }

    @GetMapping("/semana-actual")
    public ReporteVentaDTO getReporteSemanaActual() {
        return reporteService.generarReporteSemanaActual();
    }

    @GetMapping("/mes-actual")
    public ReporteVentaDTO getReporteMesActual() {
        return reporteService.generarReporteMesActual();
    }

    @GetMapping("/anio-actual")
    public ReporteVentaDTO getReporteAnioActual() {
        return reporteService.generarReporteAnioActual();
    }

    // Reportes históricos
    @GetMapping("/por-dia")
    public ResponseEntity<ReporteVentaDTO> getReportePorDia(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        if (fecha.isAfter(LocalDate.now())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(reporteService.generarReportePorDia(fecha));
    }

    @GetMapping("/por-semana")
    public ResponseEntity<ReporteVentaDTO> getReportePorSemana(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicioSemana) {
        if (fechaInicioSemana.isAfter(LocalDate.now())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(reporteService.generarReportePorSemana(fechaInicioSemana));
    }

    @GetMapping("/por-mes")
    public ResponseEntity<ReporteVentaDTO> getReportePorMes(
            @RequestParam @Min(2000) int anio,
            @RequestParam @Min(1) @Max(12) int mes) {
        if (anio > LocalDate.now().getYear()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(reporteService.generarReportePorMes(anio, mes));
    }

    @GetMapping("/por-anio")
    public ResponseEntity<ReporteVentaDTO> getReportePorAnio(
            @RequestParam @Min(2000) int anio) {
        if (anio > LocalDate.now().getYear()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(reporteService.generarReportePorAnio(anio));
    }
}