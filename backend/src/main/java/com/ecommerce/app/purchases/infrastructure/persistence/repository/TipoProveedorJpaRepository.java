package com.ecommerce.app.purchases.infrastructure.persistence.repository;

import com.ecommerce.app.purchases.infrastructure.persistence.entity.TipoProveedorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoProveedorJpaRepository extends JpaRepository<TipoProveedorEntity, Integer> {
    Optional<TipoProveedorEntity> findByNombre(String nombre);
    List<TipoProveedorEntity> findByNombreContainingIgnoreCase(String nombre);
}
