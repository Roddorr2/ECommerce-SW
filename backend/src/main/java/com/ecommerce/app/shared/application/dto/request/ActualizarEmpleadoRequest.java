package com.ecommerce.app.shared.application.dto.request;

import jakarta.validation.constraints.NotNull;

public record ActualizarEmpleadoRequest(
        @NotNull(message = "El ID es requerido")
        Integer id,

        @NotNull(message = "El usuario es requerido")
        Integer usuarioId,

        @NotNull(message = "El área es requerida")
        Integer areaId,

        @NotNull(message = "El cargo es requerido")
        Integer cargoId
) {}
