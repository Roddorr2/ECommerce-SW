package com.ecommerce.app.sales.infrastructure.persistence.repository;

import com.ecommerce.app.catalog.infrastructure.persistence.entity.ProductoEntity;
import com.ecommerce.app.sales.infrastructure.persistence.entity.CarritoEntity;
import com.ecommerce.app.sales.infrastructure.persistence.entity.CarritoItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoItemJpaRepository extends JpaRepository<CarritoItemEntity, Integer> {
    Optional<CarritoItemEntity> findByCarritoAndProducto(CarritoEntity carrito, ProductoEntity producto);
}
