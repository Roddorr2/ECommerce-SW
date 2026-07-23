package com.ecommerce.app.purchases.infrastructure.persistence.mapper;

import com.ecommerce.app.catalog.infrastructure.persistence.mapper.ProductoDbMapper;
import com.ecommerce.app.purchases.domain.model.Compra;
import com.ecommerce.app.purchases.domain.model.CompraDetalle;
import com.ecommerce.app.purchases.infrastructure.persistence.entity.CompraDetalleEntity;
import com.ecommerce.app.purchases.infrastructure.persistence.entity.CompraEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompraDbMapper {

    private final ProveedorDbMapper proveedorDbMapper;
    private final ProductoDbMapper productoDbMapper;

    public CompraDbMapper(ProveedorDbMapper proveedorDbMapper, ProductoDbMapper productoDbMapper) {
        this.proveedorDbMapper = proveedorDbMapper;
        this.productoDbMapper = productoDbMapper;
    }

    public Compra toDomain(CompraEntity entity) {
        if (entity == null) return null;
        Compra domain = new Compra(
            entity.getId(),
            entity.getFechaCompra(),
            entity.getEstadoCompra(),
            proveedorDbMapper.toDomain(entity.getProveedorEntity()),
            entity.getEmpleado()
        );
        if (entity.getDetalles() != null) {
            List<CompraDetalle> detallesDomain = entity.getDetalles().stream()
                .map(d -> toDomain((CompraDetalleEntity) d, domain))
                .collect(Collectors.toList());
            domain.setDetalles(detallesDomain);
        }
        return domain;
    }

    public CompraEntity toEntity(Compra domain) {
        if (domain == null) return null;
        if (domain instanceof CompraEntity) {
            return (CompraEntity) domain;
        }
        CompraEntity entity = new CompraEntity(domain);
        if (domain.getProveedor() != null) {
            entity.setProveedorEntity(proveedorDbMapper.toEntity(domain.getProveedor()));
        }
        entity.setEmpleado(domain.getEmpleado());
        if (domain.getDetalles() != null) {
            List<CompraDetalle> detallesEntity = domain.getDetalles().stream()
                .map(d -> {
                    CompraDetalleEntity de = toEntity(d);
                    de.setCompraEntity(entity);
                    return de;
                })
                .collect(Collectors.toList());
            entity.setDetalles(detallesEntity);
        }
        return entity;
    }

    public CompraDetalle toDomain(CompraDetalleEntity entity, Compra compraDomain) {
        if (entity == null) return null;
        return new CompraDetalle(
            entity.getId(),
            entity.getCantidad(),
            entity.getPrecioUnitario(),
            productoDbMapper.toDomain(entity.getProductoEntity()),
            compraDomain
        );
    }

    public CompraDetalleEntity toEntity(CompraDetalle domain) {
        if (domain == null) return null;
        if (domain instanceof CompraDetalleEntity) {
            return (CompraDetalleEntity) domain;
        }
        CompraDetalleEntity entity = new CompraDetalleEntity(domain);
        if (domain.getProducto() != null) {
            entity.setProductoEntity(productoDbMapper.toEntity(domain.getProducto()));
        }
        if (domain.getCompra() != null) {
            if (domain.getCompra() instanceof CompraEntity) {
                entity.setCompraEntity((CompraEntity) domain.getCompra());
            }
        }
        return entity;
    }
}
