package com.ecommerce.app.shared.infrastructure.persistence.repository;

import com.ecommerce.app.shared.infrastructure.persistence.entity.EmpleadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpleadoJpaRepository extends JpaRepository<EmpleadoEntity, Integer> {
    Optional<EmpleadoEntity> findByUsuarioId(Integer usuarioId);
}
