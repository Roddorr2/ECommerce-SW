package com.ecommerce.app.sales.infrastructure.persistence.mapper;

import com.ecommerce.app.catalog.infrastructure.persistence.mapper.ProductoDbMapper;
import com.ecommerce.app.sales.domain.model.*;
import com.ecommerce.app.sales.infrastructure.persistence.entity.*;
import com.ecommerce.app.shared.infrastructure.persistence.mapper.SharedDbMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SalesDbMapper {

    private final ProductoDbMapper productoDbMapper;
    private final SharedDbMapper sharedDbMapper;

    public SalesDbMapper(ProductoDbMapper productoDbMapper, SharedDbMapper sharedDbMapper) {
        this.productoDbMapper = productoDbMapper;
        this.sharedDbMapper = sharedDbMapper;
    }

    // --- CLIENTE ---
    public Cliente toDomain(ClienteEntity entity) {
        if (entity == null) return null;
        return new Cliente(
            entity.getId(),
            sharedDbMapper.toDomain(entity.getUsuario()),
            entity.getTelefono(),
            entity.getDireccion()
        );
    }

    public ClienteEntity toEntity(Cliente domain) {
        if (domain == null) return null;
        return new ClienteEntity(
            domain.getId(),
            sharedDbMapper.toEntity(domain.getUsuario()),
            domain.getTelefono(),
            domain.getDireccion()
        );
    }

    // --- METODO PAGO ---
    public MetodoPago toDomain(MetodoPagoEntity entity) {
        if (entity == null) return null;
        return new MetodoPago(
            entity.getId(),
            entity.getNombre()
        );
    }

    public MetodoPagoEntity toEntity(MetodoPago domain) {
        if (domain == null) return null;
        return new MetodoPagoEntity(
            domain.getId(),
            domain.getNombre()
        );
    }

    // --- CARRITO ---
    public Carrito toDomain(CarritoEntity entity) {
        if (entity == null) return null;
        Carrito domain = new Carrito(
            entity.getId(),
            toDomain(entity.getCliente()),
            entity.getFechaCreacion(),
            entity.getFechaActualizacion(),
            entity.getEstadoCarrito()
        );
        if (entity.getItems() != null) {
            domain.setItems(entity.getItems().stream()
                .map(itemEntity -> toDomain(itemEntity, domain))
                .collect(Collectors.toList()));
        }
        return domain;
    }

    public CarritoEntity toEntity(Carrito domain) {
        if (domain == null) return null;
        CarritoEntity entity = new CarritoEntity(
            domain.getId(),
            toEntity(domain.getCliente()),
            domain.getFechaCreacion(),
            domain.getFechaActualizacion(),
            domain.getEstadoCarrito()
        );
        if (domain.getItems() != null) {
            List<CarritoItemEntity> items = new ArrayList<>();
            for (CarritoItem item : domain.getItems()) {
                CarritoItemEntity itemEntity = new CarritoItemEntity(
                    item.getId(),
                    entity,
                    productoDbMapper.toEntity(item.getProducto()),
                    item.getCantidad(),
                    item.getPrecioUnitario(),
                    item.getFechaAgregado()
                );
                items.add(itemEntity);
            }
            entity.setItems(items);
        }
        return entity;
    }

    // --- CARRITO ITEM ---
    public CarritoItem toDomain(CarritoItemEntity entity, Carrito carritoDomain) {
        if (entity == null) return null;
        return new CarritoItem(
            entity.getId(),
            carritoDomain,
            productoDbMapper.toDomain(entity.getProducto()),
            entity.getCantidad(),
            entity.getPrecioUnitario(),
            entity.getFechaAgregado()
        );
    }

    public CarritoItem toDomain(CarritoItemEntity entity) {
        if (entity == null) return null;
        CarritoItem domain = new CarritoItem(
            entity.getId(),
            null,
            productoDbMapper.toDomain(entity.getProducto()),
            entity.getCantidad(),
            entity.getPrecioUnitario(),
            entity.getFechaAgregado()
        );
        if (entity.getCarrito() != null) {
            Carrito carrito = new Carrito(
                entity.getCarrito().getId(),
                toDomain(entity.getCarrito().getCliente()),
                entity.getCarrito().getFechaCreacion(),
                entity.getCarrito().getFechaActualizacion(),
                entity.getCarrito().getEstadoCarrito()
            );
            domain.setCarrito(carrito);
        }
        return domain;
    }

    public CarritoItemEntity toEntity(CarritoItem domain) {
        if (domain == null) return null;
        CarritoEntity carritoEntity = null;
        if (domain.getCarrito() != null) {
            carritoEntity = new CarritoEntity(
                domain.getCarrito().getId(),
                toEntity(domain.getCarrito().getCliente()),
                domain.getCarrito().getFechaCreacion(),
                domain.getCarrito().getFechaActualizacion(),
                domain.getCarrito().getEstadoCarrito()
            );
        }
        return new CarritoItemEntity(
            domain.getId(),
            carritoEntity,
            productoDbMapper.toEntity(domain.getProducto()),
            domain.getCantidad(),
            domain.getPrecioUnitario(),
            domain.getFechaAgregado()
        );
    }

    // --- ORDEN ---
    public Orden toDomain(OrdenEntity entity) {
        if (entity == null) return null;
        Orden domain = new Orden(
            entity.getId(),
            entity.getFechaOrden(),
            entity.getDireccionEnvio(),
            entity.getEstadoOrden(),
            toDomain(entity.getCliente()),
            toDomain(entity.getMetodoPago())
        );
        if (entity.getDetalles() != null) {
            domain.setDetalles(entity.getDetalles().stream()
                .map(detalleEntity -> toDomain(detalleEntity, domain))
                .collect(Collectors.toList()));
        }
        return domain;
    }

    public OrdenEntity toEntity(Orden domain) {
        if (domain == null) return null;
        OrdenEntity entity = new OrdenEntity(
            domain.getId(),
            domain.getFechaOrden(),
            domain.getDireccionEnvio(),
            domain.getEstadoOrden(),
            toEntity(domain.getCliente()),
            toEntity(domain.getMetodoPago())
        );
        if (domain.getDetalles() != null) {
            List<OrdenDetalleEntity> detalles = new ArrayList<>();
            for (OrdenDetalle det : domain.getDetalles()) {
                OrdenDetalleEntity detEntity = new OrdenDetalleEntity(
                    det.getId(),
                    det.getCantidad(),
                    det.getPrecioUnitario(),
                    entity,
                    productoDbMapper.toEntity(det.getProducto())
                );
                detalles.add(detEntity);
            }
            entity.setDetalles(detalles);
        }
        return entity;
    }

    // --- ORDEN DETALLE ---
    public OrdenDetalle toDomain(OrdenDetalleEntity entity, Orden ordenDomain) {
        if (entity == null) return null;
        return new OrdenDetalle(
            entity.getId(),
            entity.getCantidad(),
            entity.getPrecioUnitario(),
            ordenDomain,
            productoDbMapper.toDomain(entity.getProducto())
        );
    }

    public OrdenDetalle toDomain(OrdenDetalleEntity entity) {
        if (entity == null) return null;
        Orden ordenDomain = null;
        if (entity.getOrden() != null) {
            ordenDomain = new Orden(
                entity.getOrden().getId(),
                entity.getOrden().getFechaOrden(),
                entity.getOrden().getDireccionEnvio(),
                entity.getOrden().getEstadoOrden(),
                toDomain(entity.getOrden().getCliente()),
                toDomain(entity.getOrden().getMetodoPago())
            );
        }
        return new OrdenDetalle(
            entity.getId(),
            entity.getCantidad(),
            entity.getPrecioUnitario(),
            ordenDomain,
            productoDbMapper.toDomain(entity.getProducto())
        );
    }

    public OrdenDetalleEntity toEntity(OrdenDetalle domain) {
        if (domain == null) return null;
        OrdenEntity ordenEntity = null;
        if (domain.getOrden() != null) {
            ordenEntity = new OrdenEntity(
                domain.getOrden().getId(),
                domain.getOrden().getFechaOrden(),
                domain.getOrden().getDireccionEnvio(),
                domain.getOrden().getEstadoOrden(),
                toEntity(domain.getOrden().getCliente()),
                toEntity(domain.getOrden().getMetodoPago())
            );
        }
        return new OrdenDetalleEntity(
            domain.getId(),
            domain.getCantidad(),
            domain.getPrecioUnitario(),
            ordenEntity,
            productoDbMapper.toEntity(domain.getProducto())
        );
    }
}
