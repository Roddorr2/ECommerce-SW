package com.ecommerce.app.shared.application.dto.request;

import com.ecommerce.app.shared.domain.model.EstadoTicket;

public class ActualizarEstadoTicketRequest {
    private EstadoTicket estado;

    public ActualizarEstadoTicketRequest() {
    }

    public ActualizarEstadoTicketRequest(EstadoTicket estado) {
        this.estado = estado;
    }

    public EstadoTicket getEstado() {
        return estado;
    }

    public void setEstado(EstadoTicket estado) {
        this.estado = estado;
    }
}
