package com.ecommerce.app.sales.application.dto.request;

import jakarta.validation.constraints.NotNull;

public record CrearOrdenDesdeCarritoRequest(

        @NotNull(message = "El ID del método de pago es obligatorio")
        Integer metodoPagoId,

        String direccionEnvio
) {}
