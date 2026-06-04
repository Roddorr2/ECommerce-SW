package com.ecommerce.app.auth.domain.model;

import com.ecommerce.app.auth.domain.enums.EstadoCodigoVerificacionCodigo;
import com.ecommerce.app.shared.domain.model.Usuario;
import java.time.LocalDateTime;

public class CodigoVerificacion {

    private Integer id;
    private Usuario usuario;
    private String codigo;
    private EstadoCodigoVerificacionCodigo estado;
    private LocalDateTime fechaGeneracion;
    private LocalDateTime fechaExpiracion;
    private Integer intentosRealizados = 0;
    private Integer intentosMaximos = 3;
    private LocalDateTime fechaUso;

    public CodigoVerificacion() {
    }

    public CodigoVerificacion(Integer id, Usuario usuario, String codigo, EstadoCodigoVerificacionCodigo estado, LocalDateTime fechaGeneracion, LocalDateTime fechaExpiracion, Integer intentosRealizados, Integer intentosMaximos, LocalDateTime fechaUso) {
        this.id = id;
        this.usuario = usuario;
        this.codigo = codigo;
        this.estado = estado;
        this.fechaGeneracion = fechaGeneracion;
        this.fechaExpiracion = fechaExpiracion;
        this.intentosRealizados = intentosRealizados;
        this.intentosMaximos = intentosMaximos;
        this.fechaUso = fechaUso;
    }

    public boolean estaVigente() {
        return estado.isPendiente() && LocalDateTime.now().isBefore(fechaExpiracion);
    }

    public boolean puedeIntentar() {
        return estaVigente() && intentosRealizados < intentosMaximos;
    }

    public void registrarIntentoFallido() {
        if (!estado.isPendiente()) {
            throw new IllegalStateException("El código no está pendiente.");
        }
        this.intentosRealizados++;
    }

    public void validarPuedeMarcarComoUsado() {
        if (!estaVigente()) {
            throw new IllegalStateException("El código no está vigente.");
        }
    }

    public LocalDateTime getFechaUso() {
        return fechaUso;
    }

    public void setFechaUso(LocalDateTime fechaUso) {
        this.fechaUso = fechaUso;
    }

    public Integer getIntentosMaximos() {
        return intentosMaximos;
    }

    public void setIntentosMaximos(Integer intentosMaximos) {
        this.intentosMaximos = intentosMaximos;
    }

    public Integer getIntentosRealizados() {
        return intentosRealizados;
    }

    public void setIntentosRealizados(Integer intentosRealizados) {
        this.intentosRealizados = intentosRealizados;
    }

    public LocalDateTime getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDateTime fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDateTime fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public EstadoCodigoVerificacionCodigo getEstado() {
        return estado;
    }

    public void setEstado(EstadoCodigoVerificacionCodigo estado) {
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
