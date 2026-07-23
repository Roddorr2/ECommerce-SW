package com.ecommerce.app.sales.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AgregarItemCarritoRequest (
    @NotNull(message = "El ID del producto es obligatorio.")
    Integer productoId,

    @NotNull(message = "La cantidad es obligatoria.")
    @Min(value = 1, message = "La cantidad debe ser al menos 1.")
    Integer cantidad
) {}
