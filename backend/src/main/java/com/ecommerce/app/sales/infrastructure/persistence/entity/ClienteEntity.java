package com.ecommerce.app.sales.infrastructure.persistence.entity;

import com.ecommerce.app.shared.infrastructure.persistence.entity.UsuarioEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cliente")
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_cliente_usuario"))
    private UsuarioEntity usuario;

    @Column(length = 20, nullable = false)
    private String telefono;

    @Column(nullable = false, length = 200)
    private String direccion;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdenEntity> ordenes = new ArrayList<>();

    public ClienteEntity() {}

    public ClienteEntity(Integer id, UsuarioEntity usuario, String telefono, String direccion) {
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

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
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

    public List<OrdenEntity> getOrdenes() {
        return ordenes;
    }

    public void setOrdenes(List<OrdenEntity> ordenes) {
        this.ordenes = ordenes;
    }
}
