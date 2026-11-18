package com.pgl.appia;


public class Gasto {
    private String descripcion;
    private double monto;
    private String categoria;
    private String fecha;

    public Gasto(String descripcion, double monto, String categoria, String fecha) {
        this.descripcion = descripcion;
        this.monto = monto;
        this.categoria = categoria;
        this.fecha = fecha;
    }

    public String getDescripcion() { return descripcion; }
    public double getMonto() { return monto; }
    public String getCategoria() { return categoria; }
    public String getFecha() { return fecha; }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setMonto(double monto) { this.monto = monto; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}