package com.ecommerce.app.shared.application.dto.response;

public record EmpleadoBasicoResponse(
		Integer id,
		String nombreUsuario,
		String correoUsuario,
		String areaNombre,
		String cargoNombre
) {}
