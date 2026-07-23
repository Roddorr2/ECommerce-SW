package com.ecommerce.app.purchases.application.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CompraItemRequest(
		@NotNull(message = "El productes obligatorio.")
		Integer productoId,
		
		@NotNull(message = "La cantidad es obligatoria.")
		@Min(value = 1, message = "La cantidad debe ser al menos 1.")
		Integer cantidad,
		
		@NotNull(message = "El precio unitario es obligatorio.")
		@DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0.")
		@Digits(integer = 7, fraction = 2, message = "El precio debe tener máximo 7 dígitos enteros y 2 decimales.")
		BigDecimal precioUnitario
) {}
