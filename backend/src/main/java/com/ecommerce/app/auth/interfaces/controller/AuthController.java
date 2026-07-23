package com.ecommerce.app.auth.interfaces.controller;

import com.ecommerce.app.auth.application.dto.request.*;
import com.ecommerce.app.auth.application.dto.response.AuthResponse;
import com.ecommerce.app.auth.application.dto.response.CambioContrasenaResponse;
import com.ecommerce.app.auth.application.dto.response.SolicitudCambioContrasenaResponse;
import com.ecommerce.app.auth.application.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Autenticación", description = "Endpoints para autenticación con JWT")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario y devuelve un JWT válido")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("permitAll()")
    @PostMapping("login/verify")
    @Operation(summary = "Verificación de código para 2FA", description = "Valida que el código ingresado sea el correcto para continuar con el inicio de sesión")
    public ResponseEntity<AuthResponse> verificarCodigo2FA(@Valid @RequestBody VerificarCodigoRequest request) {
        AuthResponse response = authService.verificarCodigo(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/resend-code")
    @Operation(summary = "Reenvío de código", description = "Reenvía el código en caso de expiración o si no llegó")
    public ResponseEntity<AuthResponse> reenviarCodigo(@Valid @RequestBody ReenviarCodigoRequest request) {
        AuthResponse response = authService.reenviarCodigo(request.correo());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/register")
    @Operation(summary = "Registrar un nuevo cliente", description = "Registra un cliente en el sistema creando su usuario y datos")
    public ResponseEntity<AuthResponse>  registrarCliente(@Valid @RequestBody RegistrarClienteRequest request) {
        AuthResponse response = authService.registrarCliente(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/forgot-password")
    @Operation(summary = "Confirmar recuperación de contraseña", description = "Valida el token de recuperación y establece la nueva contraseña del usuario")
    public ResponseEntity<SolicitudCambioContrasenaResponse> solicitarRecuperacion(@Valid @RequestBody SolicitarRecuperacionRequest request) {
        return ResponseEntity.ok(authService.solicitarRecuperacion(request));
    }

    @PostMapping("/reset-password")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Confirmar recuperación de contraseña", description = "Valida el token y establece la nueva contraseña del usuario")
    public ResponseEntity<CambioContrasenaResponse> confirmarRecuperacion(@Valid @RequestBody ConfirmarRecuperacionRequest request) {
        return ResponseEntity.ok(authService.confirmarRecuperacion(request));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/change-password")
    @Operation(summary = "Cambiar contraseña — usuario autenticado", description = "Permite al usuario autenticado cambiar su contraseña actual")
    public ResponseEntity<CambioContrasenaResponse> cambiarContrasena(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody CambiarContrasenaRequest request) {
        return ResponseEntity.ok(authService.cambiarContrasena(userDetails.getUsername(), request));
    }
}
