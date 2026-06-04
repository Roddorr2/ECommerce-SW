package com.ecommerce.app.sales.application.dto.response;

public record ClienteBasicoResponse(
        Integer id,
        String nombre,
        String correo,
        String telefono
) {}
