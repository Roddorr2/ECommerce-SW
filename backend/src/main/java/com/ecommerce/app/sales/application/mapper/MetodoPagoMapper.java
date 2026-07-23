package com.ecommerce.app.sales.application.mapper;

import com.ecommerce.app.sales.application.dto.request.ActualizarMetodoPagoRequest;
import com.ecommerce.app.sales.application.dto.request.CrearMetodoPagoRequest;
import com.ecommerce.app.sales.application.dto.response.MetodoPagoResponse;
import com.ecommerce.app.sales.domain.model.MetodoPago;
import org.springframework.stereotype.Component;

@Component
public class MetodoPagoMapper {

    public MetodoPago toEntity(CrearMetodoPagoRequest request) {
        if (request == null) {
            return null;
        }

        MetodoPago metodoPago = new MetodoPago();
        metodoPago.setNombre(request.nombre());
        return metodoPago;
    }

    public void updateEntity(MetodoPago metodoPago, ActualizarMetodoPagoRequest request) {
        if (metodoPago == null || request == null) {
            return;
        }

        metodoPago.setNombre(request.nombre());
    }

    public MetodoPagoResponse toResponse(MetodoPago metodoPago) {
        if (metodoPago == null) {
            return null;
        }

        return new MetodoPagoResponse(
                metodoPago.getId(),
                metodoPago.getNombre()
        );
    }
}
