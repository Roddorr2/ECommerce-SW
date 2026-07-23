package com.ecommerce.app.purchases.application.dto.response;

public record ProveedorResponse (
        Integer id,
        String nombre,
        String telefono,
        String email,
        String direccion,
        String tipoProveedor
) {}
