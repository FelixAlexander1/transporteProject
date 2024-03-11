package com.example.transporte.model;

import javafx.scene.control.Label;

public class Cliente extends Usuario {
    private int id;
    private Usuario usuario;
    private String direccion;
    private String telefono;

    // Constructor


    public Cliente(int id, String nombre, String email, String contraseña, TipoUsuario tipo, int id1, Usuario usuario, String direccion, String telefono) {
        super(id, nombre, email, contraseña, tipo);
        this.id = id1;
        this.usuario = usuario;
        this.direccion = direccion;
        this.telefono = telefono;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + super.getId() +
                ", usuario=" + super.getNombre() +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}
