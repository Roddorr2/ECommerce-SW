package com.ecommerce.app.catalog.infrastructure.persistence.entity;

import com.ecommerce.app.catalog.domain.enums.TipoMovimientoCodigo;
import com.ecommerce.app.catalog.domain.enums.TipoReferenciaCodigo;
import com.ecommerce.app.catalog.domain.model.MovimientoStock;
import com.ecommerce.app.catalog.domain.model.Producto;
import com.ecommerce.app.shared.domain.model.Usuario;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimiento_stock")
public class MovimientoStockEntity extends MovimientoStock {

    public MovimientoStockEntity() {
        super();
    }

    public MovimientoStockEntity(MovimientoStock movimiento) {
        super(movimiento.getId(), movimiento.getProducto(), movimiento.getCantidadAnterior(),
              movimiento.getCantidadNueva(), movimiento.getTipoMovimiento(), movimiento.getTipoReferencia(),
              movimiento.getCodigoReferencia(), movimiento.getUsuario(), movimiento.getFechaMovimiento(),
              movimiento.getObservacion());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Integer getId() {
        return super.getId();
    }

    @Transient
    @Override
    public Producto getProducto() {
        return super.getProducto();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false, foreignKey = @ForeignKey(name = "fk_movimiento_stock_producto"))
    public ProductoEntity getProductoEntity() {
        if (super.getProducto() == null) {
            return null;
        }
        if (super.getProducto() instanceof ProductoEntity) {
            return (ProductoEntity) super.getProducto();
        }
        return new ProductoEntity(super.getProducto());
    }

    public void setProductoEntity(ProductoEntity productoEntity) {
        super.setProducto(productoEntity);
    }

    @Column(name = "cantidad_anterior", nullable = false)
    @Override
    public int getCantidadAnterior() {
        return super.getCantidadAnterior();
    }

    @Column(name = "cantidad_nueva", nullable = false)
    @Override
    public int getCantidadNueva() {
        return super.getCantidadNueva();
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", nullable = false)
    @Override
    public TipoMovimientoCodigo getTipoMovimiento() {
        return super.getTipoMovimiento();
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_referencia", nullable = false)
    @Override
    public TipoReferenciaCodigo getTipoReferencia() {
        return super.getTipoReferencia();
    }

    @Column(name = "codigo_referencia")
    @Override
    public String getCodigoReferencia() {
        return super.getCodigoReferencia();
    }

    @Transient
    @Override
    public Usuario getUsuario() {
        return super.getUsuario();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, foreignKey = @ForeignKey(name = "fk_movimiento_stock_usuario"))
    public com.ecommerce.app.shared.infrastructure.persistence.entity.UsuarioEntity getUsuarioEntity() {
        if (super.getUsuario() == null) {
            return null;
        }
        Usuario u = super.getUsuario();
        com.ecommerce.app.shared.infrastructure.persistence.entity.UsuarioEntity entity = new com.ecommerce.app.shared.infrastructure.persistence.entity.UsuarioEntity();
        entity.setId(u.getId());
        entity.setNombre(u.getNombre());
        entity.setCorreo(u.getCorreo());
        entity.setContrasena(u.getContrasena());
        entity.setActivo(u.isActivo());
        entity.setDobleFactorActivo(u.getDobleFactorActivo());
        if (u.getRol() != null) {
            entity.setRol(new com.ecommerce.app.shared.infrastructure.persistence.entity.RolEntity(u.getRol().getId(), u.getRol().getNombre()));
        }
        return entity;
    }

    public void setUsuarioEntity(com.ecommerce.app.shared.infrastructure.persistence.entity.UsuarioEntity usuarioEntity) {
        if (usuarioEntity == null) {
            super.setUsuario(null);
        } else {
            com.ecommerce.app.shared.domain.model.Rol rol = null;
            if (usuarioEntity.getRol() != null) {
                rol = new com.ecommerce.app.shared.domain.model.Rol(usuarioEntity.getRol().getId(), usuarioEntity.getRol().getNombre());
            }
            Usuario u = new Usuario(
                usuarioEntity.getId(),
                usuarioEntity.getNombre(),
                usuarioEntity.getCorreo(),
                usuarioEntity.getContrasena(),
                usuarioEntity.isActivo(),
                rol,
                usuarioEntity.getDobleFactorActivo()
            );
            super.setUsuario(u);
        }
    }

    @Column(name = "fecha_movimiento", nullable = false)
    @Override
    public LocalDateTime getFechaMovimiento() {
        return super.getFechaMovimiento();
    }

    @Column(columnDefinition = "TEXT")
    @Override
    public String getObservacion() {
        return super.getObservacion();
    }
}
