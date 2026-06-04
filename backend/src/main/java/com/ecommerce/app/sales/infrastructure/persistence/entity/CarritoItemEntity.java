package com.ecommerce.app.sales.infrastructure.persistence.entity;

import com.ecommerce.app.catalog.infrastructure.persistence.entity.ProductoEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "carrito_item")
public class CarritoItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "carrito_id", nullable = false, foreignKey = @ForeignKey(name = "fk_carrito_item_carrito"))
    private CarritoEntity carrito;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false, foreignKey = @ForeignKey(name = "fk_carrito_item_producto"))
    private ProductoEntity producto;

    @Column(nullable = false)
    private int cantidad;

    @Column(precision = 7, scale = 2, nullable = false)
    private BigDecimal precioUnitario;

    @Column(nullable = false)
    private LocalDateTime fechaAgregado;

    public CarritoItemEntity() {}

    public CarritoItemEntity(Integer id, CarritoEntity carrito, ProductoEntity producto, int cantidad, BigDecimal precioUnitario, LocalDateTime fechaAgregado) {
        this.id = id;
        this.carrito = carrito;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.fechaAgregado = fechaAgregado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CarritoEntity getCarrito() {
        return carrito;
    }

    public void setCarrito(CarritoEntity carrito) {
        this.carrito = carrito;
    }

    public ProductoEntity getProducto() {
        return producto;
    }

    public void setProducto(ProductoEntity producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public LocalDateTime getFechaAgregado() {
        return fechaAgregado;
    }

    public void setFechaAgregado(LocalDateTime fechaAgregado) {
        this.fechaAgregado = fechaAgregado;
    }
}
