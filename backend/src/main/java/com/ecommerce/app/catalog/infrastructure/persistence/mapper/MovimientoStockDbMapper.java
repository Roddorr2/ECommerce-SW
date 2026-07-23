package com.ecommerce.app.catalog.infrastructure.persistence.mapper;

import com.ecommerce.app.catalog.domain.model.MovimientoStock;
import com.ecommerce.app.catalog.domain.model.Producto;
import com.ecommerce.app.catalog.infrastructure.persistence.entity.MovimientoStockEntity;
import org.springframework.stereotype.Component;

@Component
public class MovimientoStockDbMapper {

    private final ProductoDbMapper productoDbMapper;

    public MovimientoStockDbMapper(ProductoDbMapper productoDbMapper) {
        this.productoDbMapper = productoDbMapper;
    }

    public MovimientoStock toDomain(MovimientoStockEntity entity) {
        if (entity == null) return null;
        Producto productoDomain = productoDbMapper.toDomain(entity.getProductoEntity());
        return new MovimientoStock(
            entity.getId(),
            productoDomain,
            entity.getCantidadAnterior(),
            entity.getCantidadNueva(),
            entity.getTipoMovimiento(),
            entity.getTipoReferencia(),
            entity.getCodigoReferencia(),
            entity.getUsuario(),
            entity.getFechaMovimiento(),
            entity.getObservacion()
        );
    }

    public MovimientoStockEntity toEntity(MovimientoStock domain) {
        if (domain == null) return null;
        if (domain instanceof MovimientoStockEntity) {
            return (MovimientoStockEntity) domain;
        }
        MovimientoStockEntity entity = new MovimientoStockEntity(domain);
        if (domain.getProducto() != null) {
            entity.setProductoEntity(productoDbMapper.toEntity(domain.getProducto()));
        }
        return entity;
    }
}
