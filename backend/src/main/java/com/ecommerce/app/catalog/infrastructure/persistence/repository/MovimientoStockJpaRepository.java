package com.ecommerce.app.catalog.infrastructure.persistence.repository;

import com.ecommerce.app.catalog.infrastructure.persistence.entity.MovimientoStockEntity;
import com.ecommerce.app.catalog.infrastructure.persistence.entity.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MovimientoStockJpaRepository extends JpaRepository<MovimientoStockEntity, Integer> {
    List<MovimientoStockEntity> findByProductoEntityOrderByFechaMovimientoDesc(ProductoEntity producto);
}
