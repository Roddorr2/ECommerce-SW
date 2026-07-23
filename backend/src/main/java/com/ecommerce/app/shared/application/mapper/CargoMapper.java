package com.ecommerce.app.shared.application.mapper;

import com.ecommerce.app.shared.application.dto.request.ActualizarCargoRequest;
import org.springframework.stereotype.Component;

import com.ecommerce.app.shared.application.dto.request.CrearCargoRequest;
import com.ecommerce.app.shared.application.dto.response.CargoResponse;
import com.ecommerce.app.shared.domain.model.Cargo;

@Component
public class CargoMapper {
	public Cargo toEntity(CrearCargoRequest request) {
        if (request == null) {
            return null;
        }

        Cargo Cargo = new Cargo();
        Cargo.setNombre(request.nombre());
        return Cargo;
    }

    public void updateEntity(Cargo cargo, ActualizarCargoRequest request) {
        if (cargo == null || request == null) {
            return;
        }

        cargo.setNombre(request.nombre());
    }

    public CargoResponse toResponse(Cargo cargo) {
        if (cargo == null) {
            return null;
        }

        return new CargoResponse(
        		cargo.getId(),
        		cargo.getNombre()
        );
    }
}
