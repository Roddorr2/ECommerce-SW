package com.ecommerce.app.sales.application.mapper;

import com.ecommerce.app.catalog.application.mapper.ProductoMapper;
import com.ecommerce.app.sales.application.dto.response.CarritoItemResponse;
import com.ecommerce.app.sales.application.dto.response.CarritoResponse;
import com.ecommerce.app.sales.application.dto.response.EstadoCarritoResponse;
import com.ecommerce.app.sales.domain.model.Carrito;
import com.ecommerce.app.sales.domain.model.CarritoItem;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CarritoMapper {

    private final ClienteMapper clienteMapper;
    private final ProductoMapper productoMapper;

    public CarritoMapper(ClienteMapper clienteMapper, ProductoMapper productoMapper) {
        this.clienteMapper = clienteMapper;
        this.productoMapper = productoMapper;
    }

    public CarritoResponse toResponse(Carrito carrito) {
        if (carrito == null) {
            return null;
        }

        return new CarritoResponse(
                carrito.getId(),
                clienteMapper.toBasicoResponse(carrito.getCliente()),
                carrito.getItems().stream()
                        .map(this::toItemResponse)
                        .collect(Collectors.toList()),
                carrito.calcularTotal(),
                carrito.getCantidadTotalItems(),
                carrito.getFechaCreacion(),
                carrito.getFechaActualizacion(),
                new EstadoCarritoResponse(carrito.getEstadoCarrito().name())
        );
    }

    public CarritoItemResponse toItemResponse(CarritoItem item) {
        if (item == null) {
            return null;
        }

        return new CarritoItemResponse(
                item.getId(),
                productoMapper.toBasicoResponse(item.getProducto()),
                item.getCantidad(),
                item.getPrecioUnitario(),
                item.calcularSubtotal(),
                item.getFechaAgregado()
        );
    }
}
