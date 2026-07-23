package com.ecommerce.app.auth.infrastructure.persistence.mapper;

import com.ecommerce.app.auth.domain.model.CodigoVerificacion;
import com.ecommerce.app.auth.infrastructure.persistence.entity.CodigoVerificacionEntity;
import com.ecommerce.app.shared.infrastructure.persistence.mapper.SharedDbMapper;
import org.springframework.stereotype.Component;

@Component
public class AuthDbMapper {

    private final SharedDbMapper sharedDbMapper;

    public AuthDbMapper(SharedDbMapper sharedDbMapper) {
        this.sharedDbMapper = sharedDbMapper;
    }

    public CodigoVerificacion toDomain(CodigoVerificacionEntity entity) {
        if (entity == null) return null;
        return new CodigoVerificacion(
            entity.getId(),
            sharedDbMapper.toDomain(entity.getUsuario()),
            entity.getCodigo(),
            entity.getEstado(),
            entity.getFechaGeneracion(),
            entity.getFechaExpiracion(),
            entity.getIntentosRealizados(),
            entity.getIntentosMaximos(),
            entity.getFechaUso()
        );
    }

    public CodigoVerificacionEntity toEntity(CodigoVerificacion domain) {
        if (domain == null) return null;
        return new CodigoVerificacionEntity(
            domain.getId(),
            sharedDbMapper.toEntity(domain.getUsuario()),
            domain.getCodigo(),
            domain.getEstado(),
            domain.getFechaGeneracion(),
            domain.getFechaExpiracion(),
            domain.getIntentosRealizados(),
            domain.getIntentosMaximos(),
            domain.getFechaUso()
        );
    }
}
