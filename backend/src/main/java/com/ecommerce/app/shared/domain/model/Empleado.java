package com.ecommerce.app.shared.domain.model;

public class Empleado {

    private Integer id;
    private Usuario usuario;
    private Area area;
    private Cargo cargo;

    public Empleado() {}

    public Empleado(Integer id, Usuario usuario, Area area, Cargo cargo) {
        this.id = id;
        this.usuario = usuario;
        this.area = area;
        this.cargo = cargo;
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

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }
}