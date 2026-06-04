package com.ecommerce.app.sales.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrdenResumenResponse(
        Integer id,
        LocalDateTime fechaOrden,
        String estado,
        BigDecimal total,
        Integer cantidadItems,
        ClienteBasicoResponse cliente
) {}
