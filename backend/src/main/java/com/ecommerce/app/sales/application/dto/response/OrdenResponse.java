package com.ecommerce.app.sales.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrdenResponse (
        Integer id,
        LocalDateTime fechaOrden,
        String direccionEnvio,
        BigDecimal total,
        EstadoOrdenResponse estado,
        ClienteBasicoResponse cliente,
        MetodoPagoResponse metodoPago,
        List<OrdenDetalleResponse> detalles
) {}