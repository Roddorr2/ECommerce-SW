package com.ecommerce.app.purchases.application.mapper;

import java.util.stream.Collectors;

import com.ecommerce.app.purchases.application.dto.response.CompraResumenResponse;
import org.springframework.stereotype.Component;

import com.ecommerce.app.catalog.application.mapper.ProductoMapper;
import com.ecommerce.app.purchases.application.dto.response.CompraDetalleResponse;
import com.ecommerce.app.purchases.application.dto.response.CompraResponse;
import com.ecommerce.app.purchases.application.dto.response.EstadoCompraResponse;
import com.ecommerce.app.purchases.domain.model.Compra;
import com.ecommerce.app.purchases.domain.model.CompraDetalle;
import com.ecommerce.app.shared.application.mapper.EmpleadoMapper;

@Component
public class CompraMapper {
	
	private final ProveedorMapper proveedorMapper;
	private final EmpleadoMapper empleadoMapper;
	private final ProductoMapper productoMapper;
	
	public CompraMapper(ProveedorMapper proveedorMapper, EmpleadoMapper empleadoMapper, ProductoMapper productoMapper) {
		this.proveedorMapper = proveedorMapper;
		this.empleadoMapper = empleadoMapper;
		this.productoMapper = productoMapper;
	}
	
	public CompraResponse toResponse(Compra compra) {
		if (compra == null) {
			return null;
		}
		
		return new CompraResponse(
				compra.getId(),
				compra.getFechaCompra(),
				compra.calcularTotal(),
				new EstadoCompraResponse(compra.getEstadoCompra().name()),
				proveedorMapper.toBasicoResponse(compra.getProveedor()),
				empleadoMapper.toBasicoResponse(compra.getEmpleado()),
				compra.getDetalles().stream()
					.map(this::toDetalleResponse)
					.collect(Collectors.toList())
		);
	}
	
	public CompraDetalleResponse toDetalleResponse(CompraDetalle detalle) {
		if (detalle == null) {
			return null;
		}
		
		return new CompraDetalleResponse(
				detalle.getId(),
				productoMapper.toBasicoResponse(detalle.getProducto()),
				detalle.getCantidad(),
				detalle.getPrecioUnitario(),
				detalle.calcularSubtotal()
		);
	}

	public CompraResumenResponse toResumenResponse(Compra compra) {
		if (compra == null) {
			return null;
		}

		return new CompraResumenResponse(
				compra.getId(),
				compra.getFechaCompra(),
				compra.getEstadoCompra().name(),
				compra.calcularTotal(),
				compra.getDetalles().size(),
				proveedorMapper.toBasicoResponse(compra.getProveedor()),
				empleadoMapper.toBasicoResponse(compra.getEmpleado())
		);
	}
}
