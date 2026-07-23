package com.ecommerce.app.purchases.application.dto.request;

import com.ecommerce.app.purchases.domain.enums.EstadoCompraCodigo;
import jakarta.validation.constraints.NotNull;

public record CambiarEstadoCompraRequest(
        @NotNull(message = "El ID del estado es obligatorio.")
        EstadoCompraCodigo codigo
) {}
