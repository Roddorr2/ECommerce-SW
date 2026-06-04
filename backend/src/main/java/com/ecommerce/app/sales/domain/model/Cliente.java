package com.ecommerce.app.sales.domain.model;

import com.ecommerce.app.shared.domain.model.Usuario;
import java.util.ArrayList;
import java.util.List;

public class Cliente {

    private Integer id;
    private Usuario usuario;
    private String telefono;
    private String direccion;
    private List<Orden> ordenes = new ArrayList<>();

    public Cliente() {}

    public Cliente(Integer id, Usuario usuario, String telefono, String direccion) {
        this.id = id;
        this.usuario = usuario;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<Orden> getOrdenes() {
        return ordenes;
    }

    public void setOrdenes(List<Orden> ordenes) {
        this.ordenes = ordenes;
    }
}