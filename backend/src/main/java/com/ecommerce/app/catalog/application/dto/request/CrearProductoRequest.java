package com.ecommerce.app.catalog.application.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CrearProductoRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 150, message = "El nombre no puede exceder 150 caracteres")
        String nombre,

        @NotBlank(message = "El SKU es obligatorio")
        @Size(max = 50, message = "El SKU no puede exceder 50 caracteres")
        String sku,

        @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
        String descripcion,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
        @Digits(integer = 7, fraction = 2, message = "El precio debe tener máximo 7 dígitos enteros y 2 decimales")
        BigDecimal precio,

        @Size(max = 255, message = "El nombre de imagen no puede exceder 255 caracteres")
        String imagenNombre,

        @NotNull(message = "La categoría es obligatoria")
        Integer categoriaId
) {}
