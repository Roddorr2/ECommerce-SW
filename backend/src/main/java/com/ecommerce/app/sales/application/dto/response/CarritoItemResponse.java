package com.ecommerce.app.sales.application.dto.response;

import com.ecommerce.app.catalog.application.dto.response.ProductoBasicoResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CarritoItemResponse(
        Integer id,
        ProductoBasicoResponse producto,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal,
        LocalDateTime fechaAgregado
) {}
