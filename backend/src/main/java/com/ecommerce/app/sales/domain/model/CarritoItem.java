package com.ecommerce.app.sales.domain.model;

import com.ecommerce.app.catalog.domain.model.Producto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CarritoItem {

    private Integer id;
    private Carrito carrito;
    private Producto producto;
    private int cantidad;
    private BigDecimal precioUnitario;
    private LocalDateTime fechaAgregado;

    public CarritoItem() {}

    public CarritoItem(Integer id, Carrito carrito, Producto producto, int cantidad, BigDecimal precioUnitario, LocalDateTime fechaAgregado) {
        this.id = id;
        this.carrito = carrito;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.fechaAgregado = fechaAgregado;
    }

    public BigDecimal calcularSubtotal() {
        if (precioUnitario == null) {
            return BigDecimal.ZERO;
        }
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    public void validarCantidad(int nuevaCantidad) {
        if (nuevaCantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
        }
    }

    public void incrementarCantidad(int incremento) {
        validarCantidad(this.cantidad + incremento);
        this.cantidad += incremento;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
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
