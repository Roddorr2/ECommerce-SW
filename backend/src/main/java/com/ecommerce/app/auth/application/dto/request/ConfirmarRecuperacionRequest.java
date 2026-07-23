package com.ecommerce.app.auth.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ConfirmarRecuperacionRequest(
        @NotBlank(message = "El correo es requerido")
        String correo,

        @NotBlank(message = "El código es requerido")
        String codigo,

        @NotBlank(message = "La nueva contraseña es requerida")
        @Size(min = 8, max = 256, message = "La contraseña debe tener entre 8 y 256 caracteres")
        String nuevaContrasena,

        @NotBlank(message = "La confirmación es requerida")
        String confirmarContrasena
) {}
