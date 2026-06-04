package com.ecommerce.app.auth.application.dto.response;

public record AuthResponse (
        String token,
        String rol,
        String nombre,
        boolean requiere2FA,
        String mensaje
) {
    public static AuthResponse requiere2FA(String mensaje) {
        return new AuthResponse(null, null, null,  true, mensaje);
    }

    public static AuthResponse conToken(String token, String rol, String nombre) {
        return new AuthResponse(token, rol, nombre, false, "Autenticación completada");
    }
}