package com.example.catedrapoo.model;

public class CostoAdicional {
    private int id;
    private int idCotizacion;
    private String descripcion;
    private double monto;

    public CostoAdicional() {}

    public CostoAdicional(int id, int idCotizacion, String descripcion, double monto) {
        this.id = id;
        this.idCotizacion = idCotizacion;
        this.descripcion = descripcion;
        this.monto = monto;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdCotizacion() { return idCotizacion; }
    public void setIdCotizacion(int idCotizacion) { this.idCotizacion = idCotizacion; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
}
