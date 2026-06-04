package com.ecommerce.app.sales.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CrearMetodoPagoRequest(
        @NotBlank(message = "El nombre es obligatorio.")
        @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres.")
        String nombre
) {}