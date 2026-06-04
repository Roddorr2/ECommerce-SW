package com.ecommerce.app.shared.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CrearCargoRequest(
		@NotBlank(message = "El nombre es obligatorio")
		@Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
		String nombre
) {}
