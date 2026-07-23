package com.ecommerce.app.catalog.application.dto.response;

import java.time.LocalDateTime;

public record MovimientoStockResponse(
        Integer id,
        String producto,
        String sku,
        Integer cantidadAnterior,
        Integer cantidadNueva,
        Integer diferencia,
        String tipoMovimiento,
        String tipoReferencia,
        String codigoReferencia,
        String usuario,
        LocalDateTime fechaMovimiento,
        String observacion
) {}
