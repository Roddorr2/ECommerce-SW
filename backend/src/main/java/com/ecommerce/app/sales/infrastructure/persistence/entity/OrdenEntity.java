package com.ecommerce.app.sales.infrastructure.persistence.entity;

import com.ecommerce.app.sales.domain.enums.EstadoOrdenCodigo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orden")
public class OrdenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime fechaOrden;

    @Column(nullable = false)
    private String direccionEnvio;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoOrdenCodigo estadoOrden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false, foreignKey = @ForeignKey(name = "fk_orden_cliente"))
    private ClienteEntity cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metodo_pago_id", nullable = false, foreignKey = @ForeignKey(name = "fk_orden_metodo_pago"))
    private MetodoPagoEntity metodoPago;

    @OneToMany(mappedBy = "orden", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<OrdenDetalleEntity> detalles = new ArrayList<>();

    public OrdenEntity() {}

    public OrdenEntity(Integer id, LocalDateTime fechaOrden, String direccionEnvio, EstadoOrdenCodigo estadoOrden, ClienteEntity cliente, MetodoPagoEntity metodoPago) {
        this.id = id;
        this.fechaOrden = fechaOrden;
        this.direccionEnvio = direccionEnvio;
        this.estadoOrden = estadoOrden;
        this.cliente = cliente;
        this.metodoPago = metodoPago;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getFechaOrden() {
        return fechaOrden;
    }

    public void setFechaOrden(LocalDateTime fechaOrden) {
        this.fechaOrden = fechaOrden;
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    public EstadoOrdenCodigo getEstadoOrden() {
        return estadoOrden;
    }

    public void setEstadoOrden(EstadoOrdenCodigo estadoOrden) {
        this.estadoOrden = estadoOrden;
    }

    public ClienteEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClienteEntity cliente) {
        this.cliente = cliente;
    }

    public MetodoPagoEntity getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPagoEntity metodoPago) {
        this.metodoPago = metodoPago;
    }

    public List<OrdenDetalleEntity> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<OrdenDetalleEntity> detalles) {
        this.detalles = detalles;
    }
}
