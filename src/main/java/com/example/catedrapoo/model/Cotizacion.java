package com.example.catedrapoo.model;

import java.time.LocalDateTime;
import java.util.List;

public class Cotizacion {
    private int id;
    private String estado;
    private LocalDateTime fechaFin;
    private LocalDateTime fechaInicio;
    private int cantidadHorasTotales;
    private double costoAsignaciones;
    private double costosAdicionales;
    private double totalCotizacion;
    private Integer idCliente;
    private List<CostoAdicional> costosAdicionalesList;
    private Integer idEmpleadoElabora;

    public Cotizacion() {
    }

    public Cotizacion(int id, String estado, LocalDateTime fechaFin, LocalDateTime fechaInicio, int cantidadHorasTotales, double costoAsignaciones, double costosAdicionales, double totalCotizacion) {
        this.id = id;
        this.estado = estado;
        this.fechaFin = fechaFin;
        this.fechaInicio = fechaInicio;
        this.cantidadHorasTotales = cantidadHorasTotales;
        this.costoAsignaciones = costoAsignaciones;
        this.costosAdicionales = costosAdicionales;
        this.totalCotizacion = totalCotizacion;
    }

    public Cotizacion(int id, String estado, LocalDateTime fechaFin, LocalDateTime fechaInicio, int cantidadHorasTotales, double costoAsignaciones, double costosAdicionales, double totalCotizacion, Integer idCliente) {
        this.id = id;
        this.estado = estado;
        this.fechaFin = fechaFin;
        this.fechaInicio = fechaInicio;
        this.cantidadHorasTotales = cantidadHorasTotales;
        this.costoAsignaciones = costoAsignaciones;
        this.costosAdicionales = costosAdicionales;
        this.totalCotizacion = totalCotizacion;
        this.idCliente = idCliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public int getCantidadHorasTotales() {
        return cantidadHorasTotales;
    }

    public void setCantidadHorasTotales(int cantidadHorasTotales) {
        this.cantidadHorasTotales = cantidadHorasTotales;
    }

    public double getCostoAsignaciones() {
        return costoAsignaciones;
    }

    public void setCostoAsignaciones(double costoAsignaciones) {
        this.costoAsignaciones = costoAsignaciones;
    }

    public double getCostosAdicionales() {
        return costosAdicionales;
    }

    public void setCostosAdicionales(double costosAdicionales) {
        this.costosAdicionales = costosAdicionales;
    }

    public double getTotalCotizacion() {
        return totalCotizacion;
    }

    public void setTotalCotizacion(double totalCotizacion) {
        this.totalCotizacion = totalCotizacion;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public List<CostoAdicional> getCostosAdicionalesList() {
        return costosAdicionalesList;
    }

    public void setCostosAdicionalesList(List<CostoAdicional> lista) {
        this.costosAdicionalesList = lista;
    }

    public Integer getIdEmpleadoElabora() {
        return idEmpleadoElabora;
    }

    public void setIdEmpleadoElabora(Integer idEmpleadoElabora) {
        this.idEmpleadoElabora = idEmpleadoElabora;
    }

    public void calcularCostoTotal() {
        this.totalCotizacion = this.costoAsignaciones + this.costosAdicionales;
    }
}
