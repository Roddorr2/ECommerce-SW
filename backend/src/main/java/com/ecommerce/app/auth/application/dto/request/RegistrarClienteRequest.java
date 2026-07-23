package com.ecommerce.app.auth.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegistrarClienteRequest (
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no debe exceder los 100 caracteres")
        String nombre,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo debe tener un formato válido")
        @Size(max = 100, message = "El correo no debe exceder los 100 caracteres")
        String correo,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, max = 256, message = "La contraseña debe tener entre 8 y 256 caracteres")
        String contrasena,

        @NotBlank(message = "El teléfono es obligatorio")
        @Size(max = 20, message = "El teléfono no debe exceder los 20 caracteres")
        @Pattern(regexp = "^[0-9+\\-\\s]{6,20}$", message = "El teléfono contiene caracteres no válidos")
        String telefono,

        @NotBlank(message = "La dirección es obligatoria")
        @Size(max = 200, message = "La dirección no debe exceder los 200 caracteres")
        String direccion
) {}