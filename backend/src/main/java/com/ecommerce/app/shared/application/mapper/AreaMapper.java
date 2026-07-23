package com.ecommerce.app.shared.application.mapper;

import com.ecommerce.app.shared.application.dto.request.ActualizarAreaRequest;
import com.ecommerce.app.shared.application.dto.request.CrearAreaRequest;
import com.ecommerce.app.shared.application.dto.response.AreaResponse;
import com.ecommerce.app.shared.domain.model.Area;
import org.springframework.stereotype.Component;

@Component
public class AreaMapper {
	public Area toEntity(CrearAreaRequest request) {
		if (request == null) {
			return null;
		}

		Area Area = new Area();
		Area.setNombre(request.nombre());
		return Area;
	}

	public void updateEntity(Area Area, ActualizarAreaRequest request) {
		if (Area == null || request == null) {
			return;
		}

		Area.setNombre(request.nombre());
	}

	public AreaResponse toResponse(Area area) {
		if (area == null) {
			return null;
		}

		return new AreaResponse(
				area.getId(),
				area.getNombre()
		);
	}
}
