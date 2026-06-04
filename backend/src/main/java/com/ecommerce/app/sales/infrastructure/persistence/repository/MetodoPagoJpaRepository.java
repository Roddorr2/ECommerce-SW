package com.ecommerce.app.sales.infrastructure.persistence.repository;

import com.ecommerce.app.sales.infrastructure.persistence.entity.MetodoPagoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MetodoPagoJpaRepository extends JpaRepository<MetodoPagoEntity, Integer> {
    Optional<MetodoPagoEntity> findByNombre(String nombre);
    List<MetodoPagoEntity> findByNombreContainingIgnoreCase(String nombre);
}
