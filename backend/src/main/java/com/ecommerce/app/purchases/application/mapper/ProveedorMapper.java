package com.ecommerce.app.purchases.application.mapper;

import com.ecommerce.app.purchases.application.dto.request.ActualizarProveedorRequest;
import org.springframework.stereotype.Component;

import com.ecommerce.app.purchases.application.dto.request.CrearProveedorRequest;
import com.ecommerce.app.purchases.application.dto.response.ProveedorBasicoResponse;
import com.ecommerce.app.purchases.application.dto.response.ProveedorResponse;
import com.ecommerce.app.purchases.domain.model.Proveedor;
import com.ecommerce.app.purchases.domain.model.TipoProveedor;

@Component
public class ProveedorMapper {

    public Proveedor toEntity(CrearProveedorRequest request, TipoProveedor tipoProveedor) {
        if (request == null) {
            return null;
        }

        Proveedor proveedor = new Proveedor();
        proveedor.setNombre(request.nombre());
        proveedor.setCorreo(request.correo());
        proveedor.setTelefono(request.telefono());
        proveedor.setDireccion(request.direccion());
        proveedor.setTipoProveedor(tipoProveedor);
        return proveedor;
    }

    public void updateEntity(Proveedor proveedor, ActualizarProveedorRequest request, TipoProveedor tipoProveedor) {
        if (proveedor == null || request == null) {
            return;
        }

        proveedor.setNombre(request.nombre());
        proveedor.setCorreo(request.correo());
        proveedor.setTelefono(request.telefono());
        proveedor.setDireccion(request.direccion());
        proveedor.setTipoProveedor(tipoProveedor);
    }

    public ProveedorBasicoResponse toBasicoResponse(Proveedor proveedor) {
        if (proveedor == null) {
            return null;
        }

        return new ProveedorBasicoResponse(
                proveedor.getId(),
                proveedor.getNombre(),
                proveedor.getTelefono(),
                proveedor.getCorreo()
        );
    }

    public ProveedorResponse toResponse(Proveedor proveedor) {
        if (proveedor == null) {
            return null;
        }

        return new ProveedorResponse(
                proveedor.getId(),
                proveedor.getNombre(),
                proveedor.getTelefono(),
                proveedor.getCorreo(),
                proveedor.getDireccion(),
                proveedor.getTipoProveedor().getNombre()
        );
    }
}
