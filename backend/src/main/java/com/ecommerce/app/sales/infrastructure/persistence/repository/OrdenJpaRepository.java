package com.ecommerce.app.sales.infrastructure.persistence.repository;

import com.ecommerce.app.sales.domain.enums.EstadoOrdenCodigo;
import com.ecommerce.app.sales.infrastructure.persistence.entity.ClienteEntity;
import com.ecommerce.app.sales.infrastructure.persistence.entity.OrdenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenJpaRepository extends JpaRepository<OrdenEntity, Integer> {
    List<OrdenEntity> findByClienteOrderByFechaOrdenDesc(ClienteEntity cliente);
    List<OrdenEntity> findByEstadoOrdenOrderByFechaOrdenDesc(EstadoOrdenCodigo estadoOrden);
}
