package com.example.transporte.model;

public class Vehiculo {
    private int id;
    private String marca;
    private String modelo;
    private int año;
    private int capacidad;
    private String matricula;

    // Constructor
    public Vehiculo(int id, String marca, String modelo, int año, int capacidad, String matricula) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.año = año;
        this.capacidad = capacidad;
        this.matricula = matricula;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAño() {
        return año;
    }

    public void setAño(int año) {
        this.año = año;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    @Override
    public String toString() {
        return "Vehiculo{" +
                "id=" + id +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", año=" + año +
                ", capacidad=" + capacidad +
                ", matricula='" + matricula + '\'' +
                '}';
    }
}
