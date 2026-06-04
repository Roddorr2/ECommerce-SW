package com.ecommerce.app.purchases.infrastructure.persistence.entity;

import com.ecommerce.app.purchases.domain.model.Proveedor;
import com.ecommerce.app.purchases.domain.model.TipoProveedor;
import jakarta.persistence.*;

@Entity
@Table(name = "proveedor")
public class ProveedorEntity extends Proveedor {

    public ProveedorEntity() {
        super();
    }

    public ProveedorEntity(Proveedor proveedor) {
        super(proveedor.getId(), proveedor.getNombre(), proveedor.getTelefono(),
              proveedor.getCorreo(), proveedor.getDireccion(), proveedor.getTipoProveedor());
    }

    public ProveedorEntity(Integer id, String nombre, String telefono, String correo, String direccion, TipoProveedor tipoProveedor) {
        super(id, nombre, telefono, correo, direccion, tipoProveedor);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Integer getId() {
        return super.getId();
    }

    @Column(nullable = false)
    @Override
    public String getNombre() {
        return super.getNombre();
    }

    @Column(nullable = false)
    @Override
    public String getTelefono() {
        return super.getTelefono();
    }

    @Column(nullable = false, unique = true)
    @Override
    public String getCorreo() {
        return super.getCorreo();
    }

    @Column(nullable = false)
    @Override
    public String getDireccion() {
        return super.getDireccion();
    }

    @Transient
    @Override
    public TipoProveedor getTipoProveedor() {
        return super.getTipoProveedor();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_proveedor_id", nullable = false, foreignKey = @ForeignKey(name = "fk_proveedor_tipo_proveedor"))
    public TipoProveedorEntity getTipoProveedorEntity() {
        if (super.getTipoProveedor() == null) {
            return null;
        }
        if (super.getTipoProveedor() instanceof TipoProveedorEntity) {
            return (TipoProveedorEntity) super.getTipoProveedor();
        }
        return new TipoProveedorEntity(super.getTipoProveedor());
    }

    public void setTipoProveedorEntity(TipoProveedorEntity tipoProveedorEntity) {
        super.setTipoProveedor(tipoProveedorEntity);
    }
}
