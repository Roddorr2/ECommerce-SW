package com.ecommerce.app.shared.interfaces.controller;

import com.ecommerce.app.shared.application.dto.request.ActualizarUsuarioRequest;
import com.ecommerce.app.shared.application.dto.request.CrearUsuarioRequest;
import com.ecommerce.app.shared.application.dto.response.UsuarioResponse;
import com.ecommerce.app.shared.application.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/usuarios")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Obtener lista de usuarios", description = "Devuelve todos los usuarios registrados en el sistema")
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        List<UsuarioResponse> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Obtener usuario por ID", description = "Devuelve un usuario según su ID")
    public ResponseEntity<UsuarioResponse> obtenerPorId(@PathVariable int id) {
        UsuarioResponse usuario = usuarioService.obtenerPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Registrar un nuevo usuario", description = "Registra un nuevo usuario en la base de datos")
    public ResponseEntity<UsuarioResponse> registrarUsuario(@RequestBody CrearUsuarioRequest request) {
        UsuarioResponse usuario = usuarioService.registrarUsuario(request);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Editar usuario", description = "Actualiza los datos de un usuario existente")
    public ResponseEntity<UsuarioResponse> editarUsuario(@RequestBody ActualizarUsuarioRequest request) {
        UsuarioResponse usuario = usuarioService.editarUsuario(request);
        return ResponseEntity.ok(usuario);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Eliminar un usuario", description = "Elimina un usuario no relacionado con clientes")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable int id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}

