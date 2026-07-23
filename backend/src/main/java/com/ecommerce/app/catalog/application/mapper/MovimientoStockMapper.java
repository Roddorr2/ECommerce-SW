package com.ecommerce.app.catalog.application.mapper;

import com.ecommerce.app.catalog.application.dto.response.MovimientoStockResponse;
import com.ecommerce.app.catalog.domain.model.MovimientoStock;
import org.springframework.stereotype.Component;

@Component
public class MovimientoStockMapper {

    public MovimientoStockResponse toResponse(MovimientoStock movimiento) {
        if (movimiento == null) {
            return null;
        }

        return new MovimientoStockResponse(
                movimiento.getId(),
                movimiento.getProducto().getNombre(),
                movimiento.getProducto().getSku(),
                movimiento.getCantidadAnterior(),
                movimiento.getCantidadNueva(),
                movimiento.getDiferencia(),
                movimiento.getTipoMovimiento().name(),
                movimiento.getTipoReferencia().name(),
                movimiento.getCodigoReferencia(),
                movimiento.getUsuario().getNombre(),
                movimiento.getFechaMovimiento(),
                movimiento.getObservacion()
        );
    }
}
