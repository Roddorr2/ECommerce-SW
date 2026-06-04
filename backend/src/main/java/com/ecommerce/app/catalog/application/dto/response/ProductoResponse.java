package com.ecommerce.app.catalog.application.dto.response;

import java.math.BigDecimal;

public record ProductoResponse (
        Integer id,
        String nombre,
        String sku,
        String descripcion,
        BigDecimal precio,
        Integer stock,
        String imagenNombre,
        Boolean activo,
        Integer categoriaId,
        String categoria
) {}