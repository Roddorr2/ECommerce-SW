package com.ecommerce.app.sales.application.mapper;

import com.ecommerce.app.catalog.application.mapper.ProductoMapper;
import com.ecommerce.app.sales.application.dto.response.EstadoOrdenResponse;
import com.ecommerce.app.sales.application.dto.response.OrdenDetalleResponse;
import com.ecommerce.app.sales.application.dto.response.OrdenResponse;
import com.ecommerce.app.sales.application.dto.response.OrdenResumenResponse;
import com.ecommerce.app.sales.domain.model.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrdenMapper {

    private final ClienteMapper clienteMapper;
    private final MetodoPagoMapper metodoPagoMapper;
    private final ProductoMapper productoMapper;

    public OrdenMapper(ClienteMapper clienteMapper, MetodoPagoMapper metodoPagoMapper, ProductoMapper productoMapper) {
        this.clienteMapper = clienteMapper;
        this.metodoPagoMapper = metodoPagoMapper;
        this.productoMapper = productoMapper;
    }

    public OrdenResponse toResponse(Orden orden) {
        if (orden == null) {
            return null;
        }

        return new OrdenResponse(
                orden.getId(),
                orden.getFechaOrden(),
                orden.getDireccionEnvio(),
                orden.calcularTotal(),
                new EstadoOrdenResponse(orden.getEstadoOrden().name()),
                clienteMapper.toBasicoResponse(orden.getCliente()),
                metodoPagoMapper.toResponse(orden.getMetodoPago()),
                orden.getDetalles().stream()
                        .map(this::toDetalleResponse)
                        .collect(Collectors.toList())
        );
    }

    public OrdenDetalleResponse toDetalleResponse(OrdenDetalle detalle) {
        if (detalle == null) {
            return null;
        }

        return new OrdenDetalleResponse(
                detalle.getId(),
                productoMapper.toBasicoResponse(detalle.getProducto()),
                detalle.getCantidad(),
                detalle.getPrecioUnitario(),
                detalle.calcularSubtotal()
        );
    }

    public OrdenResumenResponse toResumenResponse(Orden orden) {
        if (orden == null) return null;

        return new OrdenResumenResponse(
                orden.getId(),
                orden.getFechaOrden(),
                orden.getEstadoOrden().name(),
                orden.calcularTotal(),
                orden.getDetalles().size(),
                clienteMapper.toBasicoResponse(orden.getCliente())
        );
    }
}
