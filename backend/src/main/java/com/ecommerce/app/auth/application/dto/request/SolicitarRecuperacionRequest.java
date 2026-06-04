package com.ecommerce.app.auth.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SolicitarRecuperacionRequest(
        @NotBlank(message = "El correo es requerido.")
        @Email(message = "El correo no es válido.")
        String correo
) {}
