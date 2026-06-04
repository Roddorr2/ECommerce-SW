package com.ecommerce.app.sales.interfaces.controller;

import com.ecommerce.app.sales.application.dto.request.ActualizarClienteRequest;
import com.ecommerce.app.sales.application.dto.request.CrearClienteRequest;
import com.ecommerce.app.sales.application.dto.response.ClienteResponse;
import com.ecommerce.app.sales.application.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/clientes")
@Tag(name = "Clientes", description = "Operaciones relacionadas con clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Obtener lista de clientes", description = "Devuelve la lista de todos los clientes registrados")
    public ResponseEntity<List<ClienteResponse>> listarClientes() {
        List<ClienteResponse> clientes = clienteService.listarClientes();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Obtener cliente por ID", description = "Devuelve un cliente según su ID")
    public ResponseEntity<ClienteResponse> obtenerPorId(@PathVariable int id) {
        ClienteResponse cliente = clienteService.obtenerPorId(id);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Obtener cliente por usuario ID",
            description = "Devuelve el cliente asociado a un usuario específico. Solo para administradores.")
    public ResponseEntity<ClienteResponse> obtenerPorUsuarioId(@PathVariable int usuarioId) {
        ClienteResponse cliente = clienteService.obtenerPorUsuarioId(usuarioId);
        return ResponseEntity.ok(cliente);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Registrar un nuevo cliente", description = "Registra un nuevo cliente en la base de datos")
    public ResponseEntity<ClienteResponse> registrarCliente(@RequestBody CrearClienteRequest request) {
        ClienteResponse cliente = clienteService.registrarCliente(request);
        return ResponseEntity.ok(cliente);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Editar cliente", description = "Actualiza los datos de un cliente existente")
    public ResponseEntity<ClienteResponse> editarCliente(@RequestBody ActualizarClienteRequest request) {
        ClienteResponse cliente = clienteService.editarCliente(request);
        return ResponseEntity.ok(cliente);
    }

    @PutMapping("/perfil")
    @PreAuthorize("hasAuthority('Cliente')")
    @Operation(summary = "Actualizar perfil del cliente autenticado", description = "Permite a un cliente actualizar únicamente su propio perfil")
    public ResponseEntity<ClienteResponse> actualizarPerfil(@AuthenticationPrincipal(expression = "id") Integer usuarioId, @RequestBody ActualizarClienteRequest request) {
        ClienteResponse cliente = clienteService.actualizarPerfil(usuarioId, request);
        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Eliminar cliente", description = "Elimina un cliente no relacionado con usuarios")
    public ResponseEntity<Void> eliminarCliente(@PathVariable int id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }
}
