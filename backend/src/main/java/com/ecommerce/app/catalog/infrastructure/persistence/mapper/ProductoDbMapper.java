package com.ecommerce.app.catalog.infrastructure.persistence.mapper;

import com.ecommerce.app.catalog.domain.model.Categoria;
import com.ecommerce.app.catalog.domain.model.Producto;
import com.ecommerce.app.catalog.infrastructure.persistence.entity.CategoriaEntity;
import com.ecommerce.app.catalog.infrastructure.persistence.entity.ProductoEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductoDbMapper {

    public Categoria toDomain(CategoriaEntity entity) {
        if (entity == null) return null;
        return new Categoria(entity.getId(), entity.getNombre());
    }

    public CategoriaEntity toEntity(Categoria domain) {
        if (domain == null) return null;
        if (domain instanceof CategoriaEntity) {
            return (CategoriaEntity) domain;
        }
        return new CategoriaEntity(domain.getId(), domain.getNombre());
    }

    public Producto toDomain(ProductoEntity entity) {
        if (entity == null) return null;
        Categoria categoriaDomain = toDomain(entity.getCategoriaEntity());
        return new Producto(
            entity.getId(),
            entity.getNombre(),
            entity.getSku(),
            entity.getDescripcion(),
            entity.getPrecio(),
            entity.getStock(),
            entity.getImagenNombre(),
            entity.isActivo(),
            categoriaDomain
        );
    }

    public ProductoEntity toEntity(Producto domain) {
        if (domain == null) return null;
        if (domain instanceof ProductoEntity) {
            return (ProductoEntity) domain;
        }
        ProductoEntity entity = new ProductoEntity(domain);
        if (domain.getCategoria() != null) {
            entity.setCategoriaEntity(toEntity(domain.getCategoria()));
        }
        return entity;
    }
}
