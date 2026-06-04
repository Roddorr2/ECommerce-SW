package com.ecommerce.app.sales.infrastructure.persistence.repository;

import com.ecommerce.app.sales.domain.enums.EstadoCarritoCodigo;
import com.ecommerce.app.sales.infrastructure.persistence.entity.CarritoEntity;
import com.ecommerce.app.sales.infrastructure.persistence.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarritoJpaRepository extends JpaRepository<CarritoEntity, Integer> {
    Optional<CarritoEntity> findByClienteAndEstadoCarrito(ClienteEntity cliente, EstadoCarritoCodigo estadoCarrito);
    List<CarritoEntity> findByEstadoCarritoAndFechaActualizacionBefore(EstadoCarritoCodigo estadoCarritoCodigo, LocalDateTime fecha);
}
