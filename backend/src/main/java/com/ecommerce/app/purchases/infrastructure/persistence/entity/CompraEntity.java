package com.ecommerce.app.purchases.infrastructure.persistence.entity;

import com.ecommerce.app.purchases.domain.enums.EstadoCompraCodigo;
import com.ecommerce.app.purchases.domain.model.Compra;
import com.ecommerce.app.purchases.domain.model.CompraDetalle;
import com.ecommerce.app.purchases.domain.model.Proveedor;
import com.ecommerce.app.shared.domain.model.Empleado;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "compra")
public class CompraEntity extends Compra {

    public CompraEntity() {
        super();
    }

    public CompraEntity(Compra compra) {
        super(compra.getId(), compra.getFechaCompra(), compra.getEstadoCompra(),
              compra.getProveedor(), compra.getEmpleado());
        if (compra.getDetalles() != null) {
            List<CompraDetalle> mappedDetalles = new ArrayList<>();
            for (CompraDetalle d : compra.getDetalles()) {
                CompraDetalleEntity de = (d instanceof CompraDetalleEntity) ?
                        (CompraDetalleEntity) d : new CompraDetalleEntity(d);
                de.setCompraEntity(this);
                mappedDetalles.add(de);
            }
            this.setDetalles(mappedDetalles);
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Integer getId() {
        return super.getId();
    }

    @Column(name = "fecha_compra", nullable = false)
    @Override
    public LocalDate getFechaCompra() {
        return super.getFechaCompra();
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_compra", nullable = false, length = 20)
    @Override
    public EstadoCompraCodigo getEstadoCompra() {
        return super.getEstadoCompra();
    }

    @Transient
    @Override
    public Proveedor getProveedor() {
        return super.getProveedor();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", nullable = false, foreignKey = @ForeignKey(name = "fk_compra_proveedor"))
    public ProveedorEntity getProveedorEntity() {
        if (super.getProveedor() == null) {
            return null;
        }
        if (super.getProveedor() instanceof ProveedorEntity) {
            return (ProveedorEntity) super.getProveedor();
        }
        return new ProveedorEntity(super.getProveedor());
    }

    public void setProveedorEntity(ProveedorEntity proveedorEntity) {
        super.setProveedor(proveedorEntity);
    }

    @Transient
    @Override
    public Empleado getEmpleado() {
        return super.getEmpleado();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", nullable = false, foreignKey = @ForeignKey(name = "fk_compra_empleado"))
    public com.ecommerce.app.shared.infrastructure.persistence.entity.EmpleadoEntity getEmpleadoEntity() {
        if (super.getEmpleado() == null) {
            return null;
        }
        Empleado e = super.getEmpleado();
        com.ecommerce.app.shared.infrastructure.persistence.entity.EmpleadoEntity entity = new com.ecommerce.app.shared.infrastructure.persistence.entity.EmpleadoEntity();
        entity.setId(e.getId());
        if (e.getUsuario() != null) {
            com.ecommerce.app.shared.infrastructure.persistence.entity.UsuarioEntity ue = new com.ecommerce.app.shared.infrastructure.persistence.entity.UsuarioEntity();
            ue.setId(e.getUsuario().getId());
            ue.setNombre(e.getUsuario().getNombre());
            ue.setCorreo(e.getUsuario().getCorreo());
            ue.setContrasena(e.getUsuario().getContrasena());
            ue.setActivo(e.getUsuario().isActivo());
            ue.setDobleFactorActivo(e.getUsuario().getDobleFactorActivo());
            if (e.getUsuario().getRol() != null) {
                ue.setRol(new com.ecommerce.app.shared.infrastructure.persistence.entity.RolEntity(e.getUsuario().getRol().getId(), e.getUsuario().getRol().getNombre()));
            }
            entity.setUsuario(ue);
        }
        if (e.getArea() != null) {
            entity.setArea(new com.ecommerce.app.shared.infrastructure.persistence.entity.AreaEntity(e.getArea().getId(), e.getArea().getNombre()));
        }
        if (e.getCargo() != null) {
            entity.setCargo(new com.ecommerce.app.shared.infrastructure.persistence.entity.CargoEntity(e.getCargo().getId(), e.getCargo().getNombre()));
        }
        return entity;
    }

    public void setEmpleadoEntity(com.ecommerce.app.shared.infrastructure.persistence.entity.EmpleadoEntity empleadoEntity) {
        if (empleadoEntity == null) {
            super.setEmpleado(null);
        } else {
            com.ecommerce.app.shared.domain.model.Usuario usuario = null;
            if (empleadoEntity.getUsuario() != null) {
                com.ecommerce.app.shared.domain.model.Rol rol = null;
                if (empleadoEntity.getUsuario().getRol() != null) {
                    rol = new com.ecommerce.app.shared.domain.model.Rol(empleadoEntity.getUsuario().getRol().getId(), empleadoEntity.getUsuario().getRol().getNombre());
                }
                usuario = new com.ecommerce.app.shared.domain.model.Usuario(
                    empleadoEntity.getUsuario().getId(),
                    empleadoEntity.getUsuario().getNombre(),
                    empleadoEntity.getUsuario().getCorreo(),
                    empleadoEntity.getUsuario().getContrasena(),
                    empleadoEntity.getUsuario().isActivo(),
                    rol,
                    empleadoEntity.getUsuario().getDobleFactorActivo()
                );
            }
            com.ecommerce.app.shared.domain.model.Area area = null;
            if (empleadoEntity.getArea() != null) {
                area = new com.ecommerce.app.shared.domain.model.Area(empleadoEntity.getArea().getId(), empleadoEntity.getArea().getNombre());
            }
            com.ecommerce.app.shared.domain.model.Cargo cargo = null;
            if (empleadoEntity.getCargo() != null) {
                cargo = new com.ecommerce.app.shared.domain.model.Cargo(empleadoEntity.getCargo().getId(), empleadoEntity.getCargo().getNombre());
            }
            Empleado e = new Empleado(
                empleadoEntity.getId(),
                usuario,
                area,
                cargo
            );
            super.setEmpleado(e);
        }
    }

    @OneToMany(mappedBy = "compraEntity", cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = CompraDetalleEntity.class)
    @Override
    public List<CompraDetalle> getDetalles() {
        return super.getDetalles();
    }
}
