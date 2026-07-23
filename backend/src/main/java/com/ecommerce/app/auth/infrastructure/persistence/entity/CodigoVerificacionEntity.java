package com.ecommerce.app.auth.infrastructure.persistence.entity;

import com.ecommerce.app.auth.domain.enums.EstadoCodigoVerificacionCodigo;
import com.ecommerce.app.shared.infrastructure.persistence.entity.UsuarioEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "codigo_verificacion")
public class CodigoVerificacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "usuario_id", foreignKey = @ForeignKey(name = "fk_codigo_verificacion_usuario"))
    private UsuarioEntity usuario;

    @NotNull
    @Column(nullable = false, length = 6)
    private String codigo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoCodigoVerificacionCodigo estado;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime fechaGeneracion;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime fechaExpiracion;

    @NotNull
    @Column(nullable = false)
    private Integer intentosRealizados = 0;

    @NotNull
    @Column(nullable = false)
    private Integer intentosMaximos = 3;

    @Column
    private LocalDateTime fechaUso;

    public CodigoVerificacionEntity() {
    }

    public CodigoVerificacionEntity(Integer id, UsuarioEntity usuario, String codigo, EstadoCodigoVerificacionCodigo estado, LocalDateTime fechaGeneracion, LocalDateTime fechaExpiracion, Integer intentosRealizados, Integer intentosMaximos, LocalDateTime fechaUso) {
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public EstadoCodigoVerificacionCodigo getEstado() {
        return estado;
    }

    public void setEstado(EstadoCodigoVerificacionCodigo estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDateTime fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public LocalDateTime getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDateTime fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public Integer getIntentosRealizados() {
        return intentosRealizados;
    }

    public void setIntentosRealizados(Integer intentosRealizados) {
        this.intentosRealizados = intentosRealizados;
    }

    public Integer getIntentosMaximos() {
        return intentosMaximos;
    }

    public void setIntentosMaximos(Integer intentosMaximos) {
        this.intentosMaximos = intentosMaximos;
    }

    public LocalDateTime getFechaUso() {
        return fechaUso;
    }

    public void setFechaUso(LocalDateTime fechaUso) {
        this.fechaUso = fechaUso;
    }
}
