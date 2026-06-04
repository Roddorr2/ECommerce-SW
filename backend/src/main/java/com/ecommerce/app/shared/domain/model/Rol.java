package com.ecommerce.app.shared.domain.model;

import java.util.ArrayList;
import java.util.List;

public class Rol {

    private Integer id;
    private String nombre;
    private List<Usuario> usuarios = new ArrayList<>();

    public Rol() {}

    public Rol(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}