package com.ecommerce.app.shared.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 100)
    private String correo;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 255)
    private String contrasena;

    @Column(nullable = false)
    private boolean activo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_id", nullable = false, foreignKey = @ForeignKey(name = "fk_usuario_rol"))
    private RolEntity rol;

    @Column(name = "doble_factor_activo", nullable = false)
    private Boolean dobleFactorActivo = false;

    public UsuarioEntity() {}

    public UsuarioEntity(Integer id, String correo, String nombre, String contrasena, boolean activo, RolEntity rol, Boolean dobleFactorActivo) {
        this.id = id;
        this.correo = correo;
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.activo = activo;
        this.rol = rol;
        this.dobleFactorActivo = dobleFactorActivo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public RolEntity getRol() {
        return rol;
    }

    public void setRol(RolEntity rol) {
        this.rol = rol;
    }

    public Boolean getDobleFactorActivo() {
        return dobleFactorActivo;
    }

    public void setDobleFactorActivo(Boolean dobleFactorActivo) {
        this.dobleFactorActivo = dobleFactorActivo;
    }
}
