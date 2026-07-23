package com.ecommerce.app.purchases.infrastructure.persistence.entity;

import com.ecommerce.app.purchases.domain.model.TipoProveedor;
import jakarta.persistence.*;

@Entity
@Table(name = "tipo_proveedor")
public class TipoProveedorEntity extends TipoProveedor {

    public TipoProveedorEntity() {
        super();
    }

    public TipoProveedorEntity(Integer id, String nombre) {
        super(id, nombre);
    }

    public TipoProveedorEntity(TipoProveedor tipoProveedor) {
        super(tipoProveedor.getId(), tipoProveedor.getNombre());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Integer getId() {
        return super.getId();
    }

    @Column(nullable = false, unique = true)
    @Override
    public String getNombre() {
        return super.getNombre();
    }
}
