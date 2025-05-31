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

    /**
     * GET /api/reportes/hoy
     * Reporte de ventas del día actual.
     * Respuesta (JSON de salida):
     * {
     *   "fechaInicio": "2025-05-30",
     *   "fechaFin": "2025-05-30",
     *   "totalVentas": 3,
     *   "totalGanancias": 150000.00,
     *   "promedioVentasDiarias": 3.00,
     *   "promedioGananciasDiarias": 150000.00,
     *   "estadisticasPorCategoria": {
     *     "Bicicletas": {
     *       "cantidadVentas": 2,
     *       "ganancias": 100000.00,
     *       "porcentajeDelTotal": 66.67
     *     },
     *     "Motos": {
     *       "cantidadVentas": 1,
     *       "ganancias": 50000.00,
     *       "porcentajeDelTotal": 33.33
     *     }
     *   }
     * }
     */
    @GetMapping("/hoy")
    public ReporteVentaDTO getReporteHoy() {
        return reporteService.generarReporteHoy();
    }

    /**
     * GET /api/reportes/semana-actual
     * Reporte de ventas de la semana actual.
     * Respuesta: igual formato que el reporte diario, pero con el rango de la semana.
     */
    @GetMapping("/semana-actual")
    public ReporteVentaDTO getReporteSemanaActual() {
        return reporteService.generarReporteSemanaActual();
    }

    /**
     * GET /api/reportes/mes-actual
     * Reporte de ventas del mes actual.
     * Respuesta: igual formato que el reporte diario, pero con el rango del mes.
     */
    @GetMapping("/mes-actual")
    public ReporteVentaDTO getReporteMesActual() {
        return reporteService.generarReporteMesActual();
    }

    /**
     * GET /api/reportes/anio-actual
     * Reporte de ventas del año actual.
     * Respuesta: igual formato que el reporte diario, pero con el rango del año.
     */
    @GetMapping("/anio-actual")
    public ReporteVentaDTO getReporteAnioActual() {
        return reporteService.generarReporteAnioActual();
    }

    /**
     * GET /api/reportes/por-dia?fecha=YYYY-MM-DD
     * Reporte de ventas para un día específico.
     * Ejemplo de uso:
     *   /api/reportes/por-dia?fecha=2025-05-30
     * Respuesta (JSON de salida):
     * {
     *   "fechaInicio": "2025-05-30",
     *   "fechaFin": "2025-05-30",
     *   "totalVentas": 3,
     *   "totalGanancias": 150000.00,
     *   "promedioVentasDiarias": 3.00,
     *   "promedioGananciasDiarias": 150000.00,
     *   "estadisticasPorCategoria": {
     *     "Bicicletas": {
     *       "cantidadVentas": 2,
     *       "ganancias": 100000.00,
     *       "porcentajeDelTotal": 66.67
     *     },
     *     "Motos": {
     *       "cantidadVentas": 1,
     *       "ganancias": 50000.00,
     *       "porcentajeDelTotal": 33.33
     *     }
     *   }
     * }
     */
    @GetMapping("/por-dia")
    public ResponseEntity<ReporteVentaDTO> getReportePorDia(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        if (fecha.isAfter(LocalDate.now())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(reporteService.generarReportePorDia(fecha));
    }

    /**
     * GET /api/reportes/por-semana?fechaInicioSemana=YYYY-MM-DD
     * Reporte de ventas para una semana específica (fecha de inicio de semana).
     * Ejemplo de uso:
     *   /api/reportes/por-semana?fechaInicioSemana=2025-05-26
     * Respuesta: igual formato que el reporte diario, pero con el rango de la semana.
     */
    @GetMapping("/por-semana")
    public ResponseEntity<ReporteVentaDTO> getReportePorSemana(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicioSemana) {
        if (fechaInicioSemana.isAfter(LocalDate.now())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(reporteService.generarReportePorSemana(fechaInicioSemana));
    }

    /**
     * GET /api/reportes/por-mes?anio=YYYY&mes=MM
     * Reporte de ventas para un mes específico.
     * Ejemplo de uso:
     *   /api/reportes/por-mes?anio=2025&mes=5
     * Respuesta: igual formato que el reporte diario, pero con el rango del mes.
     */
    @GetMapping("/por-mes")
    public ResponseEntity<ReporteVentaDTO> getReportePorMes(
            @RequestParam @Min(2000) int anio,
            @RequestParam @Min(1) @Max(12) int mes) {
        if (anio > LocalDate.now().getYear()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(reporteService.generarReportePorMes(anio, mes));
    }

    /**
     * GET /api/reportes/por-anio?anio=YYYY
     * Reporte de ventas para un año específico.
     * Ejemplo de uso:
     *   /api/reportes/por-anio?anio=2025
     * Respuesta: igual formato que el reporte diario, pero con el rango del año.
     */
    @GetMapping("/por-anio")
    public ResponseEntity<ReporteVentaDTO> getReportePorAnio(
            @RequestParam @Min(2000) int anio) {
        if (anio > LocalDate.now().getYear()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(reporteService.generarReportePorAnio(anio));
    }
}