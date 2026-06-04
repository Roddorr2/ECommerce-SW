package com.ecommerce.app.purchases.domain.model;

import com.ecommerce.app.purchases.domain.enums.EstadoCompraCodigo;
import com.ecommerce.app.shared.domain.model.Empleado;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Compra {

    private Integer id;
    private LocalDate fechaCompra = LocalDate.now();
    private EstadoCompraCodigo estadoCompra;
    private Proveedor proveedor;
    private Empleado empleado;
    private List<CompraDetalle> detalles = new ArrayList<>();

    public Compra() {}

    public Compra(Integer id, LocalDate fechaCompra, EstadoCompraCodigo estadoCompra, Proveedor proveedor, Empleado empleado) {
        this.id = id;
        this.fechaCompra = fechaCompra;
        this.estadoCompra = estadoCompra;
        this.proveedor = proveedor;
        this.empleado = empleado;
    }

    public BigDecimal calcularTotal() {
        if (detalles == null || detalles.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return detalles.stream()
                .map(CompraDetalle::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void validarParaConfirmacion() {
        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalStateException("La compra debe tener al menos un producto");
        }
        if (proveedor == null) {
            throw new IllegalStateException("La compra debe tener un proveedor asignado.");
        }
        if (empleado == null) {
            throw new IllegalStateException("La compra debe tener un empleado responsable");
        }
    }

    public void agregarDetalle(CompraDetalle detalle) {
        if (detalles == null) {
            detalles = new ArrayList<>();
        }
        detalle.setCompra(this);
        detalles.add(detalle);
    }

    public void transicionarA(EstadoCompraCodigo nuevoEstado) {
        if (!this.estadoCompra.permiteModificacion()) {
            throw new IllegalStateException("No se puede modificar una compra finalizada.");
        }

        if (nuevoEstado.isCancelada() && this.estadoCompra.isRecibida()) {
            throw new IllegalStateException("No se puede cancelar una compra que ya fue recibida.");
        }
        this.estadoCompra = nuevoEstado;
    }

    public boolean puedeRecibirse() {
        return estadoCompra.puedeRecibirse();
    }

    public boolean puedeCancelarse() {
        return estadoCompra.puedeCancelarse();
    }

    public boolean puedeModificarse() {
        return estadoCompra.permiteModificacion();
    }

    public boolean isRecibida() {
        return estadoCompra.isRecibida();
    }

    public boolean isPendiente() {
        return estadoCompra.isPendiente();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public EstadoCompraCodigo getEstadoCompra() {
        return estadoCompra;
    }

    public void setEstadoCompra(EstadoCompraCodigo estadoCompra) {
        this.estadoCompra = estadoCompra;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public List<CompraDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<CompraDetalle> detalles) {
        this.detalles = detalles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Compra)) return false;
        Compra compra = (Compra) o;
        return id != null && id.equals(compra.getId());
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}