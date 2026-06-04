package com.ecommerce.app.purchases.infrastructure.persistence.repository;

import com.ecommerce.app.purchases.infrastructure.persistence.entity.CompraDetalleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompraDetalleJpaRepository extends JpaRepository<CompraDetalleEntity, Integer> {
}
