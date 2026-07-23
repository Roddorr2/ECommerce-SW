package com.ecommerce.app.shared.application.mapper;

import com.ecommerce.app.shared.application.dto.request.ActualizarRolRequest;
import com.ecommerce.app.shared.application.dto.request.CrearRolRequest;
import com.ecommerce.app.shared.application.dto.response.RolResponse;
import com.ecommerce.app.shared.domain.model.Rol;
import org.springframework.stereotype.Component;

@Component
public class RolMapper {

    public Rol toEntity(CrearRolRequest request) {
        if (request == null) {
            return null;
        }

        Rol rol = new Rol();
        rol.setNombre(request.nombre());
        return rol;
    }

    public void updateEntity(Rol rol, ActualizarRolRequest request) {
        if (rol == null || request == null) {
            return;
        }

        rol.setNombre(request.nombre());
    }

    public RolResponse toResponse(Rol rol) {
        if (rol == null) {
            return null;
        }

        return new RolResponse(
                rol.getId(),
                rol.getNombre()
        );
    }
}
