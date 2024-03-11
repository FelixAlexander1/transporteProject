package com.example.transporte.model;

import java.time.LocalDateTime;

public class Ruta {
    private int id;
    private Pedido pedido;
    private Conductor conductor;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    // Constructor
    public Ruta(int id, Pedido pedido, Conductor conductor, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        this.id = id;
        this.pedido = pedido;
        this.conductor = conductor;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Conductor getConductor() {
        return conductor;
    }

    public void setConductor(Conductor conductor) {
        this.conductor = conductor;
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

    @Override
    public String toString() {
        return
                "id=" + id +
                ", pedido=" + pedido +
                ", conductor=" + conductor +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin;
    }
}
