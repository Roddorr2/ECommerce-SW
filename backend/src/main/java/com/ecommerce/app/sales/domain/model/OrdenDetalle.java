package com.ecommerce.app.sales.domain.model;

import com.ecommerce.app.catalog.domain.model.Producto;
import java.math.BigDecimal;

public class OrdenDetalle {

    private Integer id;
    private int cantidad;
    private BigDecimal precioUnitario;
    private Orden orden;
    private Producto producto;

    public OrdenDetalle() {}

    public OrdenDetalle(Integer id, int cantidad, BigDecimal precioUnitario, Orden orden, Producto producto) {
        this.id = id;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.orden = orden;
        this.producto = producto;
    }

    public BigDecimal calcularSubtotal() {
        if (precioUnitario == null)
            return BigDecimal.ZERO;
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Orden getOrden() {
        return orden;
    }

    public void setOrden(Orden orden) {
        this.orden = orden;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}