package com.ecommerce.app.shared.application.service;

import com.ecommerce.app.shared.application.dto.request.ActualizarEstadoTicketRequest;
import com.ecommerce.app.shared.application.dto.request.CrearTicketRequest;
import com.ecommerce.app.shared.application.dto.response.TicketSoporteResponse;
import com.ecommerce.app.shared.domain.model.EstadoTicket;
import com.ecommerce.app.shared.domain.model.TicketSoporte;
import com.ecommerce.app.shared.domain.model.TipoSolicitud;
import com.ecommerce.app.shared.domain.port.TicketSoporteRepositoryPort;
import com.ecommerce.app.shared.infrastructure.persistence.mapper.TicketSoporteMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class TicketSoporteService {

    private final TicketSoporteRepositoryPort repositoryPort;
    private final TicketSoporteMapper mapper;
    private final Random random = new Random();

    public TicketSoporteService(TicketSoporteRepositoryPort repositoryPort, TicketSoporteMapper mapper) {
        this.repositoryPort = repositoryPort;
        this.mapper = mapper;
    }

    @Transactional
    public TicketSoporteResponse crearTicket(CrearTicketRequest request) {
        String codigoTicket = generarCodigoTicket();

        TicketSoporte ticket = new TicketSoporte();
        ticket.setCodigoTicket(codigoTicket);
        ticket.setNombreCliente(request.getNombre());
        ticket.setEmailCliente(request.getEmail());
        ticket.setTipoSolicitud(TipoSolicitud.fromCodigo(request.getTipoCaso()));
        ticket.setAsunto(request.getAsunto());
        ticket.setDescripcion(request.getMensaje());
        ticket.setEstado(EstadoTicket.PENDIENTE);
        ticket.setFechaCreacion(LocalDateTime.now());
        ticket.setFechaUltimaActualizacion(LocalDateTime.now());

        TicketSoporte guardado = repositoryPort.guardar(ticket);
        return mapper.toResponse(guardado);
    }

    @Transactional(readOnly = true)
    public List<TicketSoporteResponse> listarTodos() {
        return repositoryPort.listarTodos().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TicketSoporteResponse obtenerPorCodigo(String codigoTicket) {
        TicketSoporte ticket = repositoryPort.buscarPorCodigo(codigoTicket)
                .orElseThrow(() -> new RuntimeException("Ticket de soporte no encontrado con el código: " + codigoTicket));
        return mapper.toResponse(ticket);
    }

    @Transactional
    public TicketSoporteResponse actualizarEstado(Long id, ActualizarEstadoTicketRequest request) {
        TicketSoporte ticket = repositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Ticket de soporte no encontrado con el ID: " + id));

        ticket.setEstado(request.getEstado());
        ticket.setFechaUltimaActualizacion(LocalDateTime.now());

        TicketSoporte actualizado = repositoryPort.guardar(ticket);
        return mapper.toResponse(actualizado);
    }

    private String generarCodigoTicket() {
        int numeroAleatorio = 1000 + random.nextInt(9000);
        return "TICK-" + LocalDateTime.now().getYear() + "-" + numeroAleatorio;
    }
}
