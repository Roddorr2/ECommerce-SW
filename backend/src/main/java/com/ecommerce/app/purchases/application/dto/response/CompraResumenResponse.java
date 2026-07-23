package com.ecommerce.app.purchases.application.dto.response;

import com.ecommerce.app.shared.application.dto.response.EmpleadoBasicoResponse;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CompraResumenResponse(
        Integer id,
        LocalDate fechaCompra,
        String estado,
        BigDecimal total,
        Integer cantidadItem,
        ProveedorBasicoResponse proveedor,
        EmpleadoBasicoResponse empleado
) {}
