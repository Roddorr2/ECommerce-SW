package com.ecommerce.app.purchases.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearProveedorRequest(
        @NotBlank(message = "El nombre es requerido")
        @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
        String nombre,

        @NotBlank(message = "El teléfono es requerido")
        @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
        String telefono,

        @NotBlank(message = "El email es requerido")
        @Email(message = "El email debe ser válido")
        @Size(max = 100, message = "El email no puede exceder 100 caracteres")
        String correo,

        @Size(max = 100, message = "La dirección no puede exceder 100 caracteres")
        String direccion,

        @NotNull(message = "El tipo de proveedor es requerido")
        Integer tipoProveedorId
) {}
