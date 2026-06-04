package com.ecommerce.app.catalog.infrastructure.persistence;

import com.ecommerce.app.catalog.domain.model.Producto;
import com.ecommerce.app.catalog.domain.port.ProductoPort;
import com.ecommerce.app.catalog.infrastructure.persistence.entity.ProductoEntity;
import com.ecommerce.app.catalog.infrastructure.persistence.mapper.ProductoDbMapper;
import com.ecommerce.app.catalog.infrastructure.persistence.repository.ProductoJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductoJpaAdapter implements ProductoPort {

    private final ProductoJpaRepository productoJpaRepository;
    private final ProductoDbMapper productoDbMapper;

    public ProductoJpaAdapter(ProductoJpaRepository productoJpaRepository, ProductoDbMapper productoDbMapper) {
        this.productoJpaRepository = productoJpaRepository;
        this.productoDbMapper = productoDbMapper;
    }

    @Override
    public Optional<Producto> buscarPorId(Integer id) {
        return productoJpaRepository.findById(id)
                .map(productoDbMapper::toDomain);
    }

    @Override
    public Optional<Producto> buscarPorSku(String sku) {
        return productoJpaRepository.findBySku(sku)
                .map(productoDbMapper::toDomain);
    }

    @Override
    public List<Producto> buscarProductosDisponibles(String nombre) {
        return productoJpaRepository.buscarProductosDisponibles(nombre)
                .stream()
                .map(productoDbMapper::toDomain)
                .toList();
    }

    @Override
    public Producto guardar(Producto producto) {
        ProductoEntity entity = productoDbMapper.toEntity(producto);
        ProductoEntity entityGuardado = productoJpaRepository.save(entity);
        return productoDbMapper.toDomain(entityGuardado);
    }

    @Override
    public void eliminar(Producto producto) {
        ProductoEntity entity = productoDbMapper.toEntity(producto);
        productoJpaRepository.delete(entity);
    }

    @Override
    public List<Producto> listarTodos() {
        return productoJpaRepository.findAll()
                .stream()
                .map(productoDbMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existeEnCompras(Integer productoId) {
        return productoJpaRepository.existeEnCompras(productoId);
    }

    @Override
    public boolean existeEnOrdenes(Integer productoId) {
        return productoJpaRepository.existeEnOrdenes(productoId);
    }

    @Override
    public boolean existeProductosConCategoria(Integer categoriaId) {
        return productoJpaRepository.existeProductosConCategoria(categoriaId);
    }
}
