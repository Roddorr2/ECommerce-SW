package com.ecommerce.app.sales.application.dto.response;

public record ClienteResponse (
        Integer id,
        Integer usuarioId,
        String usuario,
        String correo,
        String telefono,
        String direccion
) {}