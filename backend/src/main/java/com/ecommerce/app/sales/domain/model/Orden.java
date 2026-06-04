package com.ecommerce.app.sales.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.ecommerce.app.sales.domain.enums.EstadoOrdenCodigo;

public class Orden {

    private Integer id;
    private LocalDateTime fechaOrden = LocalDateTime.now();
    private String direccionEnvio;
    private EstadoOrdenCodigo estadoOrden;
    private Cliente cliente;
    private MetodoPago metodoPago;
    private List<OrdenDetalle> detalles = new ArrayList<>();

    public Orden() {}

    public Orden(Integer id, LocalDateTime fechaOrden, String direccionEnvio, EstadoOrdenCodigo estadoOrden, Cliente cliente, MetodoPago metodoPago) {
        this.id = id;
        this.fechaOrden = fechaOrden;
        this.direccionEnvio = direccionEnvio;
        this.estadoOrden = estadoOrden;
        this.cliente = cliente;
        this.metodoPago = metodoPago;
    }

    public BigDecimal calcularTotal() {
        if (detalles == null || detalles.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return detalles.stream()
                .map(OrdenDetalle::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void validarParaConfirmacion() {
        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalStateException("La orden debe tener al menos un producto.");
        }
        if (cliente == null) {
            throw new IllegalStateException("La orden debe tener un cliente asignado.");
        }
        if (metodoPago == null) {
            throw new IllegalStateException("La orden debe tener un método de pago.");
        }
        if (direccionEnvio == null || direccionEnvio.isBlank()) {
            throw new IllegalStateException("La orden debe tener una dirección de envío.");
        }
    }

    public void agregarDetalle(OrdenDetalle detalle) {
        if (detalles == null) {
            detalles = new ArrayList<>();
        }
        detalle.setOrden(this);
        detalles.add(detalle);
    }

    public void transicionarA(EstadoOrdenCodigo nuevoEstado) {
        if (!this.estadoOrden.permiteModificacion()) {
            throw new IllegalStateException("No se puede modificar una orden finalizada.");
        }
        if (nuevoEstado.isCancelled() && this.estadoOrden.isPagado()) {
            throw new IllegalStateException("Requiere autorización para cancelar orden pagada.");
        }
        this.estadoOrden = nuevoEstado;
    }

    public boolean puedeCancelarse() {
        return estadoOrden == EstadoOrdenCodigo.PENDIENTE;
    }

    public boolean puedeModificarse() {
        return estadoOrden.permiteModificacion();
    }

    public boolean isPagado() {
        return this.estadoOrden.isPagado();
    }

    public void establecerDireccionEnvio(String direccion) {
        this.direccionEnvio = (direccion != null && !direccion.isBlank())
                ? direccion
                : this.cliente.getDireccion();
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public List<OrdenDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<OrdenDetalle> detalles) {
        this.detalles = detalles;
    }
}