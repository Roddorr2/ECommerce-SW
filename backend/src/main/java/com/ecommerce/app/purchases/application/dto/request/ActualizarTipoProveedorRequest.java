package com.ecommerce.app.purchases.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ActualizarTipoProveedorRequest(
        @NotNull(message = "El ID es requerido")
        Integer id,

        @NotBlank(message = "El nombre es requerido")
        @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
        String nombre
) {}
