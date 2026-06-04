package com.ecommerce.app.purchases.application.mapper;

import com.ecommerce.app.purchases.application.dto.request.ActualizarTipoProveedorRequest;
import com.ecommerce.app.purchases.application.dto.request.CrearTipoProveedorRequest;
import com.ecommerce.app.purchases.application.dto.response.TipoProveedorResponse;
import com.ecommerce.app.purchases.domain.model.TipoProveedor;
import org.springframework.stereotype.Component;

@Component
public class TipoProveedorMapper {

    public TipoProveedor toEntity(CrearTipoProveedorRequest request) {
        if (request == null) {
            return null;
        }

        TipoProveedor tipoProveedor = new TipoProveedor();
        tipoProveedor.setNombre(request.nombre());
        return tipoProveedor;
    }

    public void updateEntity(TipoProveedor tipoProveedor, ActualizarTipoProveedorRequest request) {
        if (tipoProveedor == null || request == null) {
            return;
        }

        tipoProveedor.setNombre(request.nombre());
    }

    public TipoProveedorResponse toResponse(TipoProveedor tipoProveedor) {
        if (tipoProveedor == null) {
            return null;
        }

        return new TipoProveedorResponse(
                tipoProveedor.getId(),
                tipoProveedor.getNombre()
        );
    }
}
