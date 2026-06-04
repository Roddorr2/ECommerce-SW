package com.ecommerce.app.shared.application.dto.response;

public record UsuarioResponse (
        Integer id,
        String nombre,
        String email,
        Boolean estado,
        String rol,
        Boolean dobleFactorActivo
) {}
