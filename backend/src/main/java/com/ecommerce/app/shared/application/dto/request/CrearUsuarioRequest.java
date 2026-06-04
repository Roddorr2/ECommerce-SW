package com.ecommerce.app.shared.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearUsuarioRequest (
		@NotBlank(message = "El nombre es requerido")
        @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
        String nombre,
        
        @NotBlank(message = "El correo es requerido")
        @Email(message = "El correo debe ser válido")
        @Size(max = 100, message = "El correo no puede exceder 100 caracteres")
        String correo,
        
        @NotBlank(message = "La contraseña es requerida")
        @Size(min = 8, max = 256, message = "La contraseña debe tener entre 8 y 256 caracteres")
        String contrasena,
        
        @NotNull(message = "El estado es requerido")
        Boolean activo,
        
        @NotNull(message = "El rol es requerido")
        Integer rolId
) {}
