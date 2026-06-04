package com.ecommerce.app.catalog.infrastructure.persistence;

import com.ecommerce.app.catalog.domain.model.MovimientoStock;
import com.ecommerce.app.catalog.domain.model.Producto;
import com.ecommerce.app.catalog.domain.port.MovimientoStockPort;
import com.ecommerce.app.catalog.infrastructure.persistence.entity.MovimientoStockEntity;
import com.ecommerce.app.catalog.infrastructure.persistence.entity.ProductoEntity;
import com.ecommerce.app.catalog.infrastructure.persistence.mapper.MovimientoStockDbMapper;
import com.ecommerce.app.catalog.infrastructure.persistence.mapper.ProductoDbMapper;
import com.ecommerce.app.catalog.infrastructure.persistence.repository.MovimientoStockJpaRepository;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class MovimientoStockJpaAdapter implements MovimientoStockPort {

    private final MovimientoStockJpaRepository movimientoStockJpaRepository;
    private final MovimientoStockDbMapper movimientoStockDbMapper;
    private final ProductoDbMapper productoDbMapper;

    public MovimientoStockJpaAdapter(MovimientoStockJpaRepository movimientoStockJpaRepository, 
                                     MovimientoStockDbMapper movimientoStockDbMapper,
                                     ProductoDbMapper productoDbMapper) {
        this.movimientoStockJpaRepository = movimientoStockJpaRepository;
        this.movimientoStockDbMapper = movimientoStockDbMapper;
        this.productoDbMapper = productoDbMapper;
    }

    @Override
    public List<MovimientoStock> buscarPorProductoOrdenado(Producto producto) {
        ProductoEntity entity = productoDbMapper.toEntity(producto);
        return movimientoStockJpaRepository.findByProductoEntityOrderByFechaMovimientoDesc(entity)
                .stream()
                .map(movimientoStockDbMapper::toDomain)
                .toList();
    }

    @Override
    public MovimientoStock guardar(MovimientoStock movimiento) {
        MovimientoStockEntity entity = movimientoStockDbMapper.toEntity(movimiento);
        MovimientoStockEntity entityGuardado = movimientoStockJpaRepository.save(entity);
        return movimientoStockDbMapper.toDomain(entityGuardado);
    }
}
