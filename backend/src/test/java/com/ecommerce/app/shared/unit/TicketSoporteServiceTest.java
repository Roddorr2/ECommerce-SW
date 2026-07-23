package com.ecommerce.app.shared.unit;

import com.ecommerce.app.shared.application.dto.request.ActualizarEstadoTicketRequest;
import com.ecommerce.app.shared.application.dto.request.CrearTicketRequest;
import com.ecommerce.app.shared.application.dto.response.TicketSoporteResponse;
import com.ecommerce.app.shared.application.service.TicketSoporteService;
import com.ecommerce.app.shared.domain.model.EstadoTicket;
import com.ecommerce.app.shared.domain.model.TicketSoporte;
import com.ecommerce.app.shared.domain.model.TipoSolicitud;
import com.ecommerce.app.shared.domain.port.TicketSoporteRepositoryPort;
import com.ecommerce.app.shared.infrastructure.persistence.mapper.TicketSoporteMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TicketSoporteService Test")
public class TicketSoporteServiceTest {

    @Mock
    private TicketSoporteRepositoryPort repositoryPort;

    @Mock
    private TicketSoporteMapper mapper;

    @InjectMocks
    private TicketSoporteService service;

    private TicketSoporte ticket;
    private TicketSoporteResponse response;

    @BeforeEach
    void setUp() {
        ticket = new TicketSoporte(1L, "TICK-2026-1001", "Juan Pérez", "juan@example.com",
                TipoSolicitud.GARANTIA, "Falla GPU", "La pantalla parpadea", EstadoTicket.PENDIENTE,
                LocalDateTime.now(), LocalDateTime.now());

        response = new TicketSoporteResponse(1L, "TICK-2026-1001", "Juan Pérez", "juan@example.com",
                TipoSolicitud.GARANTIA, "Falla GPU", "La pantalla parpadea", EstadoTicket.PENDIENTE,
                LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    @DisplayName("dado request válido, cuando se crea ticket, entonces guarda y retorna respuesta con código")
    void dadoRequestValido_cuandoCrearTicket_entoncesRetornaResponse() {
        CrearTicketRequest request = new CrearTicketRequest("Juan Pérez", "juan@example.com", "garantia", "Falla GPU", "La pantalla parpadea");

        when(repositoryPort.guardar(any(TicketSoporte.class))).thenReturn(ticket);
        when(mapper.toResponse(any(TicketSoporte.class))).thenReturn(response);

        TicketSoporteResponse resultado = service.crearTicket(request);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getCodigoTicket()).isEqualTo("TICK-2026-1001");
        verify(repositoryPort).guardar(any(TicketSoporte.class));
    }

    @Test
    @DisplayName("dado código existente, cuando se busca por código, entonces retorna el ticket")
    void dadoCodigoExistente_cuandoBuscarPorCodigo_entoncesRetornaTicket() {
        when(repositoryPort.buscarPorCodigo("TICK-2026-1001")).thenReturn(Optional.of(ticket));
        when(mapper.toResponse(ticket)).thenReturn(response);

        TicketSoporteResponse resultado = service.obtenerPorCodigo("TICK-2026-1001");

        assertThat(resultado).isNotNull();
        assertThat(resultado.getCodigoTicket()).isEqualTo("TICK-2026-1001");
    }

    @Test
    @DisplayName("dado código inexistente, cuando se busca por código, entonces lanza RuntimeException")
    void dadoCodigoInexistente_cuandoBuscarPorCodigo_entoncesLanzaExcepcion() {
        when(repositoryPort.buscarPorCodigo("INEXISTENTE")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.obtenerPorCodigo("INEXISTENTE"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ticket de soporte no encontrado");
    }

    @Test
    @DisplayName("dado ticket existente, cuando se actualiza estado, entonces guarda nuevo estado")
    void dadoTicketExistente_cuandoActualizarEstado_entoncesGuardaNuevoEstado() {
        ActualizarEstadoTicketRequest request = new ActualizarEstadoTicketRequest(EstadoTicket.RESUELTO);

        when(repositoryPort.buscarPorId(1L)).thenReturn(Optional.of(ticket));
        when(repositoryPort.guardar(ticket)).thenReturn(ticket);
        when(mapper.toResponse(ticket)).thenReturn(response);

        TicketSoporteResponse resultado = service.actualizarEstado(1L, request);

        assertThat(resultado).isNotNull();
        assertThat(ticket.getEstado()).isEqualTo(EstadoTicket.RESUELTO);
        verify(repositoryPort).guardar(ticket);
    }
}
