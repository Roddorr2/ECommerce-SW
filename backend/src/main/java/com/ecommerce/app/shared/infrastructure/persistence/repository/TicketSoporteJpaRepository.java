package com.ecommerce.app.shared.infrastructure.persistence.repository;

import com.ecommerce.app.shared.domain.model.EstadoTicket;
import com.ecommerce.app.shared.infrastructure.persistence.entity.TicketSoporteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketSoporteJpaRepository extends JpaRepository<TicketSoporteEntity, Long> {
    Optional<TicketSoporteEntity> findByCodigoTicket(String codigoTicket);
    List<TicketSoporteEntity> findByEstado(EstadoTicket estado);

    @Query("SELECT t FROM TicketSoporteEntity t WHERE t.estado = :estado AND t.fechaUltimaActualizacion <= :fechaLimite")
    List<TicketSoporteEntity> findInactivosPorEstadoYFechaLimite(
            @Param("estado") EstadoTicket estado,
            @Param("fechaLimite") LocalDateTime fechaLimite
    );
}
