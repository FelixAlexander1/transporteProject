package com.example.transporte.model;

import java.time.LocalDateTime;

public class Pedido {
    private String nombre;
    private int id;
    private Cliente cliente;
    private String origen;
    private String destino;
    private LocalDateTime fechaPedido;
    private EstadoPedido estado;

    public Pedido() {
    }

    public Pedido(String nombre) {
        this.nombre = nombre;
    }

    public Pedido(String nombre, int id, EstadoPedido estado) {
        this.nombre = nombre;
        this.id = id;
        this.estado = estado;
    }

    public Pedido(String nombre, int id, Cliente cliente, String origen, String destino, LocalDateTime fechaPedido, EstadoPedido estado) {
        this.nombre = nombre;
        this.id = id;
        this.cliente = cliente;
        this.origen = origen;
        this.destino = destino;
        this.fechaPedido = fechaPedido;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "nombre='" + nombre + '\'' +
                ", id=" + id +
                ", cliente=" + cliente +
                ", origen='" + origen + '\'' +
                ", destino='" + destino + '\'' +
                ", fechaPedido=" + fechaPedido +
                ", estado=" + estado +
                '}';
    }


}
