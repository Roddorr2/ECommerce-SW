package com.ecommerce.app.sales.application.dto.request;

import com.ecommerce.app.sales.domain.enums.EstadoOrdenCodigo;
import jakarta.validation.constraints.NotNull;

public record CambiarEstadoOrdenRequest(
        @NotNull(message = "El código del estado es obligatorio.")
        EstadoOrdenCodigo codigo
) {}
