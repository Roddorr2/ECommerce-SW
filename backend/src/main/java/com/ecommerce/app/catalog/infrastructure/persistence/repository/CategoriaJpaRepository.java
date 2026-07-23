package com.ecommerce.app.catalog.infrastructure.persistence.repository;

import com.ecommerce.app.catalog.infrastructure.persistence.entity.CategoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaJpaRepository extends JpaRepository<CategoriaEntity, Integer> {
    Optional<CategoriaEntity> findByNombre(String nombre);
    List<CategoriaEntity> findByNombreContainingIgnoreCase(String nombre);
}
