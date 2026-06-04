package com.ecommerce.app.purchases.application.dto.response;

import java.math.BigDecimal;

import com.ecommerce.app.catalog.application.dto.response.ProductoBasicoResponse;

public record CompraDetalleResponse(
        Integer id,
        ProductoBasicoResponse producto,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) {}