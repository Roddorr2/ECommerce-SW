package com.ecommerce.app.auth.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRequest (
		@NotBlank(message = "El correo es obligatorio")
		@Email(message = "Formato de correo inválido")
		String correo,

		@NotBlank(message = "La contraseña es obligatoria")
		String contrasena
) {}
