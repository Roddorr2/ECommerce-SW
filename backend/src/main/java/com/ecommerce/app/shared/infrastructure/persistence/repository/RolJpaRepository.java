package com.ecommerce.app.shared.infrastructure.persistence.repository;

import com.ecommerce.app.shared.infrastructure.persistence.entity.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolJpaRepository extends JpaRepository<RolEntity, Integer> {
    Optional<RolEntity> findByNombre(String nombre);
    List<RolEntity> findByNombreContainingIgnoreCase(String nombre);
}
