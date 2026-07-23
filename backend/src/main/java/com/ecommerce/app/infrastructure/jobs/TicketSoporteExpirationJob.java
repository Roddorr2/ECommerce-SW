package com.ecommerce.app.infrastructure.jobs;

import com.ecommerce.app.shared.domain.model.EstadoTicket;
import com.ecommerce.app.shared.domain.model.TicketSoporte;
import com.ecommerce.app.shared.domain.port.TicketSoporteRepositoryPort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class TicketSoporteExpirationJob {

    private static final Logger log = LoggerFactory.getLogger(TicketSoporteExpirationJob.class);

    private final TicketSoporteRepositoryPort ticketRepositoryPort;

    public TicketSoporteExpirationJob(TicketSoporteRepositoryPort ticketRepositoryPort) {
        this.ticketRepositoryPort = ticketRepositoryPort;
    }

    /**
     * Revisa diariamente a las 02:00 AM los tickets en estado PENDIENTE que no hayan
     * registrado actividad o atención durante más de 7 días y los marca automáticamente como EXPIRADO.
     */
    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void expirarTicketsInactivos() {
        LocalDateTime limiteInactividad = LocalDateTime.now().minusDays(7);

        List<TicketSoporte> inactivos = ticketRepositoryPort
                .buscarInactivosPendientesAntesDe(EstadoTicket.PENDIENTE, limiteInactividad);

        if (inactivos.isEmpty()) {
            return;
        }

        inactivos.forEach(ticket -> {
            ticket.setEstado(EstadoTicket.EXPIRADO);
            ticket.setFechaUltimaActualizacion(LocalDateTime.now());
        });

        ticketRepositoryPort.guardarTodos(inactivos);

        log.info("CronJob Soporte: Se han marcado {} tickets/reclamos inactivos como EXPIRADO.", inactivos.size());
    }
}
