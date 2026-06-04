package com.ecommerce.app.auth.application.dto.response;

public record SolicitudCambioContrasenaResponse(
        String mensaje,
        boolean requiere2FA
) {}
