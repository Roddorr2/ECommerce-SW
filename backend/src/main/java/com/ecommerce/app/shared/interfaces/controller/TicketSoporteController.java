package com.ecommerce.app.shared.interfaces.controller;

import com.ecommerce.app.shared.application.dto.request.ActualizarEstadoTicketRequest;
import com.ecommerce.app.shared.application.dto.request.CrearTicketRequest;
import com.ecommerce.app.shared.application.dto.response.TicketSoporteResponse;
import com.ecommerce.app.shared.application.service.TicketSoporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/soporte")
@Tag(name = "Soporte Técnico", description = "Operaciones de gestión y atención de tickets/reclamos de soporte técnico")
public class TicketSoporteController {

    private final TicketSoporteService ticketSoporteService;

    public TicketSoporteController(TicketSoporteService ticketSoporteService) {
        this.ticketSoporteService = ticketSoporteService;
    }

    @PostMapping
    @Operation(summary = "Crear ticket de soporte", description = "Permite registrar una nueva solicitud o reclamo técnico desde la web")
    public ResponseEntity<TicketSoporteResponse> crearTicket(@RequestBody CrearTicketRequest request) {
        TicketSoporteResponse nuevoTicket = ticketSoporteService.crearTicket(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTicket);
    }

    @GetMapping
    @Operation(summary = "Listar todos los tickets", description = "Devuelve la lista completa de solicitudes de soporte registradas")
    public ResponseEntity<List<TicketSoporteResponse>> listarTodos() {
        List<TicketSoporteResponse> tickets = ticketSoporteService.listarTodos();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{codigoTicket}")
    @Operation(summary = "Obtener ticket por código", description = "Consulta la información detallada de un ticket mediante su código único (ej. TICK-2026-1234)")
    public ResponseEntity<TicketSoporteResponse> obtenerPorCodigo(@PathVariable String codigoTicket) {
        TicketSoporteResponse ticket = ticketSoporteService.obtenerPorCodigo(codigoTicket);
        return ResponseEntity.ok(ticket);
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado de ticket", description = "Permite cambiar el estado de un ticket (PENDIENTE, EN_PROCESO, RESUELTO, EXPIRADO, CERRADO)")
    public ResponseEntity<TicketSoporteResponse> actualizarEstado(
            @PathVariable Long id,
            @RequestBody ActualizarEstadoTicketRequest request) {
        TicketSoporteResponse actualizado = ticketSoporteService.actualizarEstado(id, request);
        return ResponseEntity.ok(actualizado);
    }
}
