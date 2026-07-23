package com.ecommerce.app.shared.infrastructure.persistence.mapper;

import com.ecommerce.app.shared.application.dto.response.TicketSoporteResponse;
import com.ecommerce.app.shared.domain.model.TicketSoporte;
import com.ecommerce.app.shared.infrastructure.persistence.entity.TicketSoporteEntity;
import org.springframework.stereotype.Component;

@Component
public class TicketSoporteMapper {

    public TicketSoporte toDomain(TicketSoporteEntity entity) {
        if (entity == null) return null;
        return new TicketSoporte(
                entity.getId(),
                entity.getCodigoTicket(),
                entity.getNombreCliente(),
                entity.getEmailCliente(),
                entity.getTipoSolicitud(),
                entity.getAsunto(),
                entity.getDescripcion(),
                entity.getEstado(),
                entity.getFechaCreacion(),
                entity.getFechaUltimaActualizacion()
        );
    }

    public TicketSoporteEntity toEntity(TicketSoporte domain) {
        if (domain == null) return null;
        TicketSoporteEntity entity = new TicketSoporteEntity();
        entity.setId(domain.getId());
        entity.setCodigoTicket(domain.getCodigoTicket());
        entity.setNombreCliente(domain.getNombreCliente());
        entity.setEmailCliente(domain.getEmailCliente());
        entity.setTipoSolicitud(domain.getTipoSolicitud());
        entity.setAsunto(domain.getAsunto());
        entity.setDescripcion(domain.getDescripcion());
        entity.setEstado(domain.getEstado());
        entity.setFechaCreacion(domain.getFechaCreacion());
        entity.setFechaUltimaActualizacion(domain.getFechaUltimaActualizacion());
        return entity;
    }

    public TicketSoporteResponse toResponse(TicketSoporte domain) {
        if (domain == null) return null;
        return new TicketSoporteResponse(
                domain.getId(),
                domain.getCodigoTicket(),
                domain.getNombreCliente(),
                domain.getEmailCliente(),
                domain.getTipoSolicitud(),
                domain.getAsunto(),
                domain.getDescripcion(),
                domain.getEstado(),
                domain.getFechaCreacion(),
                domain.getFechaUltimaActualizacion()
        );
    }
}
