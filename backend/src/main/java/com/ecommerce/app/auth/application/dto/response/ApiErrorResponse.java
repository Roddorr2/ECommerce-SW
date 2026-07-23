package com.ecommerce.app.auth.application.dto.response;

import java.time.LocalDateTime;

public record ApiErrorResponse (
        int status,
        String mensaje,
        LocalDateTime timestamp
) {
    public ApiErrorResponse(int status, String mensaje) {
        this(status, mensaje, LocalDateTime.now());
    }
}
