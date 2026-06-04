package com.ecommerce.app.purchases.application.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CrearCompraRequest(
		@NotNull(message = "El proveedor es obligatorio.")
		Integer proveedorId,
		
		@NotNull(message = "El empleado responsable es obligatorio.")
		Integer empleadoId,
		
		@NotEmpty(message = "La compra debe tener al menos un producto.")
		@Valid
		List<CompraItemRequest> items
) {}
