package com.ecommerce.app.purchases.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CrearTipoProveedorRequest(
		@NotBlank(message = "El nombre es requerido")
		@Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
		String nombre
) {}
