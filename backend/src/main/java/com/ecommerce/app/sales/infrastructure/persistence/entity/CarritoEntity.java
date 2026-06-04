package com.ecommerce.app.sales.infrastructure.persistence.entity;

import com.ecommerce.app.sales.domain.enums.EstadoCarritoCodigo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carrito", uniqueConstraints = {
        @UniqueConstraint(name = "uk_carrito_cliente", columnNames = "cliente_id"),
})
public class CarritoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "cliente_id", nullable = false, foreignKey = @ForeignKey(name = "fk_cliente_carrito"))
    private ClienteEntity cliente;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoCarritoCodigo estadoCarrito;

    @OneToMany(mappedBy = "carrito", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<CarritoItemEntity> items = new ArrayList<>();

    public CarritoEntity() {}

    public CarritoEntity(Integer id, ClienteEntity cliente, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion, EstadoCarritoCodigo estadoCarrito) {
        this.id = id;
        this.cliente = cliente;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
        this.estadoCarrito = estadoCarrito;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ClienteEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClienteEntity cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public EstadoCarritoCodigo getEstadoCarrito() {
        return estadoCarrito;
    }

    public void setEstadoCarrito(EstadoCarritoCodigo estadoCarrito) {
        this.estadoCarrito = estadoCarrito;
    }

    public List<CarritoItemEntity> getItems() {
        return items;
    }

    public void setItems(List<CarritoItemEntity> items) {
        this.items = items;
    }
}
