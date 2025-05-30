package com.ciclomotos.ciclomotos.service;

import com.ciclomotos.ciclomotos.controller.dto.ReporteVentaDTO;
import com.ciclomotos.ciclomotos.repository.DetalleVentaRepository;
import com.ciclomotos.ciclomotos.repository.VentaRepository;
import com.ciclomotos.ciclomotos.model.Venta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Transactional
public class ReporteService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    // ===== Reportes automáticos (actuales) =====
    public ReporteVentaDTO generarReporteHoy() {
        return generarReportePorDia(LocalDate.now());
    }

    public ReporteVentaDTO generarReporteSemanaActual() {
        LocalDate lunes = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        return generarReportePorSemana(lunes);
    }

    public ReporteVentaDTO generarReporteMesActual() {
        LocalDate hoy = LocalDate.now();
        return generarReportePorMes(hoy.getYear(), hoy.getMonthValue());
    }

    public ReporteVentaDTO generarReporteAnioActual() {
        return generarReportePorAnio(LocalDate.now().getYear());
    }

    // ===== Reportes históricos (por parámetros) =====
    public ReporteVentaDTO generarReportePorDia(LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.atTime(23, 59, 59);
        return generarReporte(inicio, fin);
    }

    public ReporteVentaDTO generarReportePorSemana(LocalDate fechaInicioSemana) {
        LocalDateTime inicio = fechaInicioSemana.atStartOfDay();
        LocalDateTime fin = fechaInicioSemana.plusDays(6).atTime(23, 59, 59);
        return generarReporte(inicio, fin);
    }

    public ReporteVentaDTO generarReportePorMes(int anio, int mes) {
        LocalDateTime inicio = LocalDateTime.of(anio, mes, 1, 0, 0);
        LocalDateTime fin = inicio.plusMonths(1).minusDays(1)
                .withHour(23).withMinute(59).withSecond(59);
        return generarReporte(inicio, fin);
    }

    public ReporteVentaDTO generarReportePorAnio(int anio) {
        LocalDateTime inicio = LocalDateTime.of(anio, 1, 1, 0, 0);
        LocalDateTime fin = LocalDateTime.of(anio, 12, 31, 23, 59, 59);
        return generarReporte(inicio, fin);
    }

    private ReporteVentaDTO generarReporte(LocalDateTime inicio, LocalDateTime fin) {
        List<Venta> ventas = ventaRepository.findVentasByFecha(inicio, fin);
        int totalVentas = ventas.size();
        BigDecimal totalGanancias = detalleVentaRepository.sumSubtotalByFecha(inicio, fin);

        long dias = ChronoUnit.DAYS.between(inicio.toLocalDate(), fin.toLocalDate()) + 1;
        BigDecimal promedioVentasDiarias = BigDecimal.valueOf(totalVentas)
                .divide(BigDecimal.valueOf(dias), 2, RoundingMode.HALF_UP);

        BigDecimal promedioGananciasDiarias = BigDecimal.ZERO;
        if (dias > 0 && totalGanancias != null) {
            promedioGananciasDiarias = totalGanancias.divide(BigDecimal.valueOf(dias), 2, RoundingMode.HALF_UP);
        }

        // Usar la nueva consulta para ventas por categoría
        List<Object[]> statsCategorias = detalleVentaRepository.getVentasPorCategoria(inicio, fin);
        Map<String, ReporteVentaDTO.CategoriaStats> estadisticas = new LinkedHashMap<>();

        for (Object[] stat : statsCategorias) {
            String categoria = (String) stat[0];
            Long cantidadVentas = ((Number) stat[1]).longValue(); // ahora es cantidad de ventas únicas por categoría
            BigDecimal gananciasNetas = (BigDecimal) stat[2];

            BigDecimal porcentaje = totalGanancias != null && totalGanancias.compareTo(BigDecimal.ZERO) > 0
                    ? gananciasNetas.divide(totalGanancias, 4, RoundingMode.HALF_UP)
                            .multiply(new BigDecimal(100))
                    : BigDecimal.ZERO;

            estadisticas.put(categoria, new ReporteVentaDTO.CategoriaStats(
                    cantidadVentas,
                    gananciasNetas,
                    porcentaje.setScale(2, RoundingMode.HALF_UP)
            ));
        }

        ReporteVentaDTO reporte = new ReporteVentaDTO();
        reporte.setFechaInicio(inicio.toLocalDate());
        reporte.setFechaFin(fin.toLocalDate());
        reporte.setTotalVentas(totalVentas);
        reporte.setTotalGanancias(totalGanancias);
        reporte.setPromedioVentasDiarias(promedioVentasDiarias);
        reporte.setPromedioGananciasDiarias(promedioGananciasDiarias);
        reporte.setEstadisticasPorCategoria(estadisticas);

        return reporte;
    }
}