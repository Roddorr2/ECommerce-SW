package com.ecommerce.app.shared.interfaces.controller;

import com.ecommerce.app.shared.application.dto.request.ActualizarRolRequest;
import com.ecommerce.app.shared.application.dto.request.CrearRolRequest;
import com.ecommerce.app.shared.application.dto.response.RolResponse;
import com.ecommerce.app.shared.application.service.RolService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/roles")
@Tag(name = "Rol", description = "Operaciones relacionadas con los roles de usuarios")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Obtener lista de roles", description = "Devuelve todos los roles de usuarios disponibles")
    public ResponseEntity<List<RolResponse>> listarRoles() {
        List<RolResponse> roles = rolService.listarRoles();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener rol por ID", description = "Devuelve un rol específico según su ID")
    public ResponseEntity<RolResponse> obtenerPorId(@PathVariable int id) {
        RolResponse rol = rolService.obtenerPorId(id);
        return ResponseEntity.ok(rol);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Registrar un nuevo rol", description = "Registra un rol en la base de datos")
    public ResponseEntity<RolResponse> registrarRol(@RequestBody CrearRolRequest request) {
        RolResponse rol = rolService.registrarRol(request);
        return ResponseEntity.ok(rol);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Editar rol", description = "Actualiza los datos de un rol existente")
    public ResponseEntity<RolResponse> editarRol(@RequestBody ActualizarRolRequest request) {
        RolResponse rol = rolService.editarRol(request);
        return ResponseEntity.ok(rol);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Eliminar un rol", description = "Elimina un rol no relacionado con usuarios")
    public ResponseEntity<Void> eliminarRol(@PathVariable int id) {
        rolService.eliminarRol(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/{name}")
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Buscar un rol por nombre", description = "Busca un rol por su nombre")
    public ResponseEntity<List<RolResponse>> buscarPorNombre(@PathVariable String name) {
        List<RolResponse> roles = rolService.buscarPorNombre(name);
        return ResponseEntity.ok(roles);
    }
}