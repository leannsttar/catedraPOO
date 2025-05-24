package com.example.catedrapoo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Asignacion {
    private int id;
    private String titulo;
    private String area;
    private double costoPorHora;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private int cantidadHoras;
    private double incrementoExtra;
    private double costoBase;
    private double totalAsignacion;
    private Integer idEmpleado;
    private String descripcion;
    private List<SubTarea> subTareas;
    private Integer idCotizacion;

    public Asignacion() {
        this.subTareas = new ArrayList<>();
    }

    public Asignacion(int id, String titulo, String area, double costoPorHora, LocalDateTime fechaInicio, LocalDateTime fechaFin, int cantidadHoras, double incrementoExtra, double costoBase, double totalAsignacion) {
        this.id = id;
        this.titulo = titulo;
        this.area = area;
        this.costoPorHora = costoPorHora;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cantidadHoras = cantidadHoras;
        this.incrementoExtra = incrementoExtra;
        this.costoBase = costoBase;
        this.totalAsignacion = totalAsignacion;
        this.subTareas = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public double getCostoPorHora() {
        return costoPorHora;
    }

    public void setCostoPorHora(double costoPorHora) {
        this.costoPorHora = costoPorHora;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getCantidadHoras() {
        return cantidadHoras;
    }

    public void setCantidadHoras(int cantidadHoras) {
        this.cantidadHoras = cantidadHoras;
    }

    public double getIncrementoExtra() {
        return incrementoExtra;
    }

    public void setIncrementoExtra(double incrementoExtra) {
        this.incrementoExtra = incrementoExtra;
    }

    public double getCostoBase() {
        return costoBase;
    }

    public void setCostoBase(double costoBase) {
        this.costoBase = costoBase;
    }

    public double getTotalAsignacion() {
        return totalAsignacion;
    }

    public void setTotalAsignacion(double totalAsignacion) {
        this.totalAsignacion = totalAsignacion;
    }

    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<SubTarea> getSubTareas() {
        return subTareas;
    }

    public void setSubTareas(List<SubTarea> subTareas) {
        this.subTareas = subTareas;
    }

    public Integer getIdCotizacion() {
        return idCotizacion;
    }

    public void setIdCotizacion(Integer idCotizacion) {
        this.idCotizacion = idCotizacion;
    }

    public void calcularCostoAsignacion() {
        this.totalAsignacion = (costoPorHora * cantidadHoras + costoBase) * (1 + incrementoExtra);
    }
}
