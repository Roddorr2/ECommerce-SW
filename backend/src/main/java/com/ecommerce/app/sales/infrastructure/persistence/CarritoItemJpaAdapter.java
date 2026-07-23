package com.ecommerce.app.sales.infrastructure.persistence;

import com.ecommerce.app.catalog.domain.model.Producto;
import com.ecommerce.app.catalog.infrastructure.persistence.entity.ProductoEntity;
import com.ecommerce.app.catalog.infrastructure.persistence.mapper.ProductoDbMapper;
import com.ecommerce.app.sales.domain.model.Carrito;
import com.ecommerce.app.sales.domain.model.CarritoItem;
import com.ecommerce.app.sales.domain.port.CarritoItemPort;
import com.ecommerce.app.sales.infrastructure.persistence.entity.CarritoEntity;
import com.ecommerce.app.sales.infrastructure.persistence.entity.CarritoItemEntity;
import com.ecommerce.app.sales.infrastructure.persistence.mapper.SalesDbMapper;
import com.ecommerce.app.sales.infrastructure.persistence.repository.CarritoItemJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CarritoItemJpaAdapter implements CarritoItemPort {
    private final CarritoItemJpaRepository carritoItemJpaRepository;
    private final SalesDbMapper salesDbMapper;
    private final ProductoDbMapper productoDbMapper;

    public CarritoItemJpaAdapter(CarritoItemJpaRepository carritoItemJpaRepository, SalesDbMapper salesDbMapper, ProductoDbMapper productoDbMapper) {
        this.carritoItemJpaRepository = carritoItemJpaRepository;
        this.salesDbMapper = salesDbMapper;
        this.productoDbMapper = productoDbMapper;
    }

    @Override
    public Optional<CarritoItem> buscarPorId(Integer id) {
        return carritoItemJpaRepository.findById(id)
                .map(salesDbMapper::toDomain);
    }

    @Override
    public Optional<CarritoItem> buscarPorCarritoYProducto(Carrito carrito, Producto producto) {
        CarritoEntity carritoEntity = salesDbMapper.toEntity(carrito);
        ProductoEntity productoEntity = productoDbMapper.toEntity(producto);
        return carritoItemJpaRepository.findByCarritoAndProducto(carritoEntity, productoEntity)
                .map(salesDbMapper::toDomain);
    }

    @Override
    public CarritoItem guardar(CarritoItem item) {
        CarritoItemEntity entity = salesDbMapper.toEntity(item);
        CarritoItemEntity guardado = carritoItemJpaRepository.save(entity);
        return salesDbMapper.toDomain(guardado);
    }

    @Override
    public void eliminar(CarritoItem item) {
        CarritoItemEntity entity = salesDbMapper.toEntity(item);
        carritoItemJpaRepository.delete(entity);
    }

    @Override
    public void eliminarTodos(List<CarritoItem> items) {
        List<CarritoItemEntity> entities = items.stream()
                .map(salesDbMapper::toEntity)
                .collect(Collectors.toList());
        carritoItemJpaRepository.deleteAll(entities);
    }
}
