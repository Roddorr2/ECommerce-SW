package com.ecommerce.app.sales.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CarritoResponse(
        Integer id,
        ClienteBasicoResponse cliente,
        List<CarritoItemResponse> items,
        BigDecimal total,
        Integer cantidadTotalItems,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion,
        EstadoCarritoResponse estado
) {}
