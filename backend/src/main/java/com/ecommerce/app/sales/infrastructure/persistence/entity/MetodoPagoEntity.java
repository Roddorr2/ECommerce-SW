package com.ecommerce.app.sales.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "metodo_pago", uniqueConstraints = {
        @UniqueConstraint(name = "uk_metodo_pago_nombre", columnNames = "nombre")
})
public class MetodoPagoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false, unique = true)
    private String nombre;

    @OneToMany(mappedBy = "metodoPago", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<OrdenEntity> ordenes = new ArrayList<>();

    public MetodoPagoEntity() {}

    public MetodoPagoEntity(Integer id, String nombre) {
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

    public List<OrdenEntity> getOrdenes() {
        return ordenes;
    }

    public void setOrdenes(List<OrdenEntity> ordenes) {
        this.ordenes = ordenes;
    }
}
