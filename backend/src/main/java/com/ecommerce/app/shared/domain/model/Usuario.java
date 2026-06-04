package com.ecommerce.app.shared.domain.model;

public class Usuario {

    private Integer id;
    private String nombre;
    private String correo;
    private String contrasena;
    private boolean activo = true;
    private Rol rol;
    private Boolean dobleFactorActivo = false;

    public Usuario() {}

    public Usuario(Integer id, String nombre, String correo, String contrasena, boolean activo, Rol rol, Boolean dobleFactorActivo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.activo = activo;
        this.rol = rol;
        this.dobleFactorActivo = dobleFactorActivo;
    }

    public boolean requiere2FA() {
        return Boolean.TRUE.equals(dobleFactorActivo);
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Boolean getDobleFactorActivo() {
        return dobleFactorActivo;
    }

    public void setDobleFactorActivo(Boolean dobleFactorActivo) {
        this.dobleFactorActivo = dobleFactorActivo;
    }
}