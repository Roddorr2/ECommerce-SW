package com.ecommerce.app.shared.domain.port;

import com.ecommerce.app.shared.domain.model.EstadoTicket;
import com.ecommerce.app.shared.domain.model.TicketSoporte;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TicketSoporteRepositoryPort {
    TicketSoporte guardar(TicketSoporte ticketSoporte);
    List<TicketSoporte> guardarTodos(List<TicketSoporte> tickets);
    Optional<TicketSoporte> buscarPorId(Long id);
    Optional<TicketSoporte> buscarPorCodigo(String codigoTicket);
    List<TicketSoporte> listarTodos();
    List<TicketSoporte> buscarPorEstado(EstadoTicket estado);
    List<TicketSoporte> buscarInactivosPendientesAntesDe(EstadoTicket estado, LocalDateTime fechaLimite);
}
