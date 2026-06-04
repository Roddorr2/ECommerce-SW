package com.ecommerce.app.auth.infrastructure.persistence.repository;

import com.ecommerce.app.auth.domain.enums.EstadoCodigoVerificacionCodigo;
import com.ecommerce.app.auth.infrastructure.persistence.entity.CodigoVerificacionEntity;
import com.ecommerce.app.shared.infrastructure.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CodigoVerificacionJpaRepository extends JpaRepository<CodigoVerificacionEntity, Integer> {
    Optional<CodigoVerificacionEntity> findByUsuarioAndCodigoAndEstado(UsuarioEntity usuario, String codigo, EstadoCodigoVerificacionCodigo estado);
    
    @Query("""
    SELECT COUNT(c) FROM CodigoVerificacionEntity c
    WHERE c.usuario = :usuario AND c.fechaGeneracion > :desde 
    """)
    long countByUsuarioAndFechaGeneracionAfter(@Param("usuario") UsuarioEntity usuario, @Param("desde") LocalDateTime desde);
    
    List<CodigoVerificacionEntity> findByUsuarioAndEstado(UsuarioEntity usuario, EstadoCodigoVerificacionCodigo estado);
    
    @Query("""
    SELECT c FROM CodigoVerificacionEntity c
    WHERE c.estado = :estado AND c.fechaExpiracion < :fecha
    """)
    List<CodigoVerificacionEntity> findCodigosExpirados(@Param("estado") EstadoCodigoVerificacionCodigo estado, @Param("fecha") LocalDateTime fecha);
}
