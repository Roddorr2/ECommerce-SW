package com.ecommerce.app.catalog.domain.model;

import com.ecommerce.app.catalog.domain.enums.TipoMovimientoCodigo;
import com.ecommerce.app.catalog.domain.enums.TipoReferenciaCodigo;
import com.ecommerce.app.shared.domain.model.Usuario;

import java.time.LocalDateTime;

public class MovimientoStock {
    private Integer id;
    private Producto producto;
    private int cantidadAnterior;
    private int cantidadNueva;
    private TipoMovimientoCodigo tipoMovimiento;
    private TipoReferenciaCodigo tipoReferencia;
    private String codigoReferencia;
    private Usuario usuario;
    private LocalDateTime fechaMovimiento;
    private String observacion;

    public MovimientoStock() {
    }

    public MovimientoStock(Integer id, Producto producto, int cantidadAnterior, int cantidadNueva, TipoMovimientoCodigo tipoMovimiento, TipoReferenciaCodigo tipoReferencia, String codigoReferencia, Usuario usuario, LocalDateTime fechaMovimiento, String observacion) {
        this.id = id;
        this.producto = producto;
        this.cantidadAnterior = cantidadAnterior;
        this.cantidadNueva = cantidadNueva;
        this.tipoMovimiento = tipoMovimiento;
        this.tipoReferencia = tipoReferencia;
        this.codigoReferencia = codigoReferencia;
        this.usuario = usuario;
        this.fechaMovimiento = fechaMovimiento;
        this.observacion = observacion;
    }

    public Integer getDiferencia() {
        return cantidadNueva - cantidadAnterior;
    }

    public boolean isEntrada() {
        return tipoMovimiento == TipoMovimientoCodigo.ENTRADA;
    }

    public boolean isSalida() {
        return tipoMovimiento == TipoMovimientoCodigo.SALIDA;
    }

    public void validarConsistencia() {
        int diferencia = getDiferencia();

        if (isEntrada() && diferencia <= 0) {
            throw new IllegalStateException("Un movimiento de ENTRADA debe tener diferencia positiva.");
        }

        if (isSalida() && diferencia >= 0) {
            throw new IllegalStateException("Un movimiento de SALIDA debe tener diferencia negativa.");
        }
    }

    public void validarDatosCompletos() {
        if (producto == null) {
            throw new IllegalStateException("El movimiento debe tener un producto asociado");
        }
        if (tipoMovimiento == null) {
            throw new IllegalStateException("El movimiento debe tener un tipo de movimiento");
        }
        if (tipoReferencia == null) {
            throw new IllegalStateException("El movimiento debe tener un tipo de referencia");
        }
        if (usuario == null) {
            throw new IllegalStateException("El movimiento debe tener un usuario responsable");
        }
        if (fechaMovimiento == null) {
            throw new IllegalStateException("El movimiento debe tener una fecha");
        }
    }

    public String getDescripcion() {
        String tipo = isEntrada() ? "Entrada" : "Salida";
        String referencia = tipoReferencia != null ? tipoReferencia.name() : "Sin referencia";
        String diferencia = Math.abs(getDiferencia()) + " unidades";

        return String.format("%s de %s - %s (%s → %s)", tipo, diferencia, referencia, cantidadAnterior, cantidadNueva);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidadAnterior() {
        return cantidadAnterior;
    }

    public void setCantidadAnterior(int cantidadAnterior) {
        this.cantidadAnterior = cantidadAnterior;
    }

    public int getCantidadNueva() {
        return cantidadNueva;
    }

    public void setCantidadNueva(int cantidadNueva) {
        this.cantidadNueva = cantidadNueva;
    }

    public TipoMovimientoCodigo getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(TipoMovimientoCodigo tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	public TipoReferenciaCodigo getTipoReferencia() {
		return tipoReferencia;
	}

	public void setTipoReferencia(TipoReferenciaCodigo tipoReferencia) {
		this.tipoReferencia = tipoReferencia;
	}

	public String getCodigoReferencia() {
        return codigoReferencia;
    }

    public void setCodigoReferencia(String codigoReferencia) {
        this.codigoReferencia = codigoReferencia;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(LocalDateTime fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
