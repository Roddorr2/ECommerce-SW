package com.ecommerce.app.purchases.infrastructure.persistence.mapper;

import com.ecommerce.app.purchases.domain.model.Proveedor;
import com.ecommerce.app.purchases.domain.model.TipoProveedor;
import com.ecommerce.app.purchases.infrastructure.persistence.entity.ProveedorEntity;
import com.ecommerce.app.purchases.infrastructure.persistence.entity.TipoProveedorEntity;
import org.springframework.stereotype.Component;

@Component
public class ProveedorDbMapper {

    public TipoProveedor toDomain(TipoProveedorEntity entity) {
        if (entity == null) return null;
        return new TipoProveedor(entity.getId(), entity.getNombre());
    }

    public TipoProveedorEntity toEntity(TipoProveedor domain) {
        if (domain == null) return null;
        if (domain instanceof TipoProveedorEntity) {
            return (TipoProveedorEntity) domain;
        }
        return new TipoProveedorEntity(domain.getId(), domain.getNombre());
    }

    public Proveedor toDomain(ProveedorEntity entity) {
        if (entity == null) return null;
        TipoProveedor tipoProveedorDomain = toDomain(entity.getTipoProveedorEntity());
        return new Proveedor(
            entity.getId(),
            entity.getNombre(),
            entity.getTelefono(),
            entity.getCorreo(),
            entity.getDireccion(),
            tipoProveedorDomain
        );
    }

    public ProveedorEntity toEntity(Proveedor domain) {
        if (domain == null) return null;
        if (domain instanceof ProveedorEntity) {
            return (ProveedorEntity) domain;
        }
        ProveedorEntity entity = new ProveedorEntity(domain);
        if (domain.getTipoProveedor() != null) {
            entity.setTipoProveedorEntity(toEntity(domain.getTipoProveedor()));
        }
        return entity;
    }
}
