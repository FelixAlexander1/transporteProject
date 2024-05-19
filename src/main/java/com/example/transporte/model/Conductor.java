package com.example.transporte.model;

public class Conductor extends Usuario {
    private int id;
    private Usuario usuario;
    private String licenciaConducir;
    private Vehiculo vehiculo;
    private boolean disponible;

    // Constructor


    public Conductor(int id, String nombre, String email, String contraseña, TipoUsuario tipo, int id1, Usuario usuario, String licenciaConducir, Vehiculo vehiculo, boolean disponible) {
        super(id, nombre, email, contraseña, tipo);
        this.id = id1;
        this.usuario = usuario;
        this.licenciaConducir = licenciaConducir;
        this.vehiculo = vehiculo;
        this.disponible = disponible;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getLicenciaConducir() {
        return licenciaConducir;
    }

    public void setLicenciaConducir(String licenciaConducir) {
        this.licenciaConducir = licenciaConducir;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return "Conductor{" +
                "id=" + id +
                ", usuario=" + usuario +
                ", licenciaConducir='" + licenciaConducir + '\'' +
                ", vehiculo=" + vehiculo +
                ", disponible=" + disponible +
                '}';
    }
}
