package com.ecommerce.app.purchases.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.ecommerce.app.shared.application.dto.response.EmpleadoBasicoResponse;

public record CompraResponse (
    Integer id,
    LocalDate fechaCompra,
    BigDecimal total,
    EstadoCompraResponse estado,
    ProveedorBasicoResponse proveedor,
    EmpleadoBasicoResponse empleado,
    List<CompraDetalleResponse> detalles
) {}