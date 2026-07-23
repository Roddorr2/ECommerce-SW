package com.ecommerce.app.shared.infrastructure.persistence.repository;

import com.ecommerce.app.shared.infrastructure.persistence.entity.UsuarioEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, Integer> {
    @Query("SELECT u FROM UsuarioEntity u JOIN FETCH u.rol WHERE u.correo = :correo")
    Optional<UsuarioEntity> findByCorreo(@Param("correo") String correo);
}
