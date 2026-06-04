package com.ecommerce.app.sales.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.ecommerce.app.sales.domain.enums.EstadoCarritoCodigo;

public class Carrito {

    private Integer id;
    private Cliente cliente;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private EstadoCarritoCodigo estadoCarrito;
    private List<CarritoItem> items = new ArrayList<>();

    public Carrito() {}

    public Carrito(Integer id, Cliente cliente, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion, EstadoCarritoCodigo estadoCarrito) {
        this.id = id;
        this.cliente = cliente;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
        this.estadoCarrito = estadoCarrito;
    }

    public BigDecimal calcularTotal() {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return items.stream()
                .map(CarritoItem::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getCantidadTotalItems() {
        if (items == null || items.isEmpty()) {
            return 0;
        }
        return items.stream()
                .mapToInt(CarritoItem::getCantidad)
                .sum();
    }

    public boolean estaVacio() {
        return items == null || items.isEmpty();
    }

    public boolean puedeModificarse() {
        return estadoCarrito != null && estadoCarrito.permiteModificacion();
    }

    public boolean puedeConvertirse() {
        return !estaVacio() &&
                estadoCarrito != null &&
                estadoCarrito.permiteConversion();
    }

    public void validarParaConversion() {
        if (estaVacio()) {
            throw new IllegalStateException("El carrito está vacío, no puede convertirse en orden");
        }
        if (!estadoCarrito.permiteConversion()) {
            throw new IllegalStateException("El carrito no permite conversión en este estado: " + estadoCarrito.name());
        }
    }

    public void actualizarFecha() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
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

    public List<CarritoItem> getItems() {
        return items;
    }

    public void setItems(List<CarritoItem> items) {
        this.items = items;
    }
}