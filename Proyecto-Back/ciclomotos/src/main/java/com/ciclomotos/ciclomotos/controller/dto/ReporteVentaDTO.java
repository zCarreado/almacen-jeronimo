package com.ciclomotos.ciclomotos.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReporteVentaDTO {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int totalVentas;
    private BigDecimal totalGanancias;
    private BigDecimal promedioVentasDiarias;
    private BigDecimal promedioGananciasDiarias;
    private Map<String, CategoriaStats> estadisticasPorCategoria = new LinkedHashMap<>();

    // Clase interna para estadísticas por categoría
    public static class CategoriaStats {
        private Long cantidadVentas;
        private BigDecimal ganancias;
        private BigDecimal porcentajeDelTotal;

        // Constructor
        public CategoriaStats(Long cantidadVentas, BigDecimal ganancias, BigDecimal porcentajeDelTotal) {
            this.cantidadVentas = cantidadVentas;
            this.ganancias = ganancias;
            this.porcentajeDelTotal = porcentajeDelTotal;
        }

        // Getters
        public Long getCantidadVentas() { return cantidadVentas; }
        public BigDecimal getGanancias() { return ganancias; }
        public BigDecimal getPorcentajeDelTotal() { return porcentajeDelTotal; }
    }

    // Getters y Setters
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    
    public int getTotalVentas() { return totalVentas; }
    public void setTotalVentas(int totalVentas) { this.totalVentas = totalVentas; }
    
    public BigDecimal getTotalGanancias() { return totalGanancias; }
    public void setTotalGanancias(BigDecimal totalGanancias) { this.totalGanancias = totalGanancias; }
    
    public BigDecimal getPromedioVentasDiarias() { return promedioVentasDiarias; }
    public void setPromedioVentasDiarias(BigDecimal promedioVentasDiarias) { this.promedioVentasDiarias = promedioVentasDiarias; }
    
    public BigDecimal getPromedioGananciasDiarias() { return promedioGananciasDiarias; }
    public void setPromedioGananciasDiarias(BigDecimal promedioGananciasDiarias) { this.promedioGananciasDiarias = promedioGananciasDiarias; }
    
    public Map<String, CategoriaStats> getEstadisticasPorCategoria() { return estadisticasPorCategoria; }
    public void setEstadisticasPorCategoria(Map<String, CategoriaStats> estadisticasPorCategoria) { this.estadisticasPorCategoria = estadisticasPorCategoria; }
}