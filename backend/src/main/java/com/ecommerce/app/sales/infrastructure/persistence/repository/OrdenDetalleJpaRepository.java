package com.ecommerce.app.sales.infrastructure.persistence.repository;

import com.ecommerce.app.sales.infrastructure.persistence.entity.OrdenDetalleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenDetalleJpaRepository extends JpaRepository<OrdenDetalleEntity, Integer> {
}
