package com.ecommerce.app.catalog.application.dto.response;

import java.math.BigDecimal;

public record ProductoBasicoResponse(
        Integer id,
        String nombre,
        String sku,
        BigDecimal precio,
        Integer stock,
        String imagenNombre
) {}
