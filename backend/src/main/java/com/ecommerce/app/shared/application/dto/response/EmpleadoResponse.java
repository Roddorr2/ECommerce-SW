package com.ecommerce.app.shared.application.dto.response;

public record EmpleadoResponse (
        Integer id,
        Integer usuarioId,
        String nombreUsuario,
        String emailUsuario,
        String area,
        String cargo
) {}