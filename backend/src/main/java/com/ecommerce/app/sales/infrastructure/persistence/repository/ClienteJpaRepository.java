package com.ecommerce.app.sales.infrastructure.persistence.repository;

import com.ecommerce.app.sales.infrastructure.persistence.entity.ClienteEntity;
import com.ecommerce.app.shared.infrastructure.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteJpaRepository extends JpaRepository<ClienteEntity, Integer> {
    Optional<ClienteEntity> findByUsuario(UsuarioEntity usuario);
    Optional<ClienteEntity> findByUsuarioCorreo(String correoorreo);
}
