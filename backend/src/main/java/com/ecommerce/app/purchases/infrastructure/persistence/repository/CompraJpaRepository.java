package com.ecommerce.app.purchases.infrastructure.persistence.repository;

import com.ecommerce.app.purchases.domain.enums.EstadoCompraCodigo;
import com.ecommerce.app.purchases.infrastructure.persistence.entity.CompraEntity;
import com.ecommerce.app.purchases.infrastructure.persistence.entity.ProveedorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompraJpaRepository extends JpaRepository<CompraEntity, Integer> {
    List<CompraEntity> findByProveedorEntityOrderByFechaCompraDesc(ProveedorEntity proveedorEntity);
    List<CompraEntity> findByEstadoCompraOrderByFechaCompraDesc(EstadoCompraCodigo estadoCompra);
}
