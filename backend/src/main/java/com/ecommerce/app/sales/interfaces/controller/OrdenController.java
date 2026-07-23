package com.ecommerce.app.sales.interfaces.controller;

import com.ecommerce.app.sales.application.dto.request.CambiarEstadoOrdenRequest;
import com.ecommerce.app.sales.application.dto.request.CrearOrdenDesdeCarritoRequest;
import com.ecommerce.app.sales.application.dto.response.OrdenResponse;
import com.ecommerce.app.sales.application.dto.response.OrdenResumenResponse;
import com.ecommerce.app.sales.application.service.OrdenService;
import com.ecommerce.app.sales.domain.enums.EstadoOrdenCodigo;
import com.ecommerce.app.sales.domain.port.ClientePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Órdenes de Venta", description = "Endpoints para gestionar órdenes de venta")
public class OrdenController {

    private final OrdenService ordenService;
    private final ClientePort clientePort;

    public OrdenController(OrdenService ordenService, ClientePort clientePort) {
        this.ordenService = ordenService;
        this.clientePort = clientePort;
    }

    private Integer obtenerClienteId(UserDetails userDetails) {
        return clientePort.buscarPorUsuarioCorreo(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado."))
                .getId();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Cliente')")
    @Operation(summary = "Crear orden desde carrito", description = "Convierte el carrito activo en una orden de venta")
    public ResponseEntity<OrdenResponse> crearOrdenDesdeCarrito(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody CrearOrdenDesdeCarritoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ordenService.crearOrdenDesdeCarrito(obtenerClienteId(userDetails), request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('Cliente', 'Empleado', 'Administrador')")
    @Operation(summary = "Obtener orden por ID", description = "Retorna los detalles de una orden específica")
    public ResponseEntity<OrdenResponse> obtenerPorId(@PathVariable Integer id, @AuthenticationPrincipal UserDetails userDetails) {
        OrdenResponse orden = ordenService.obtenerPorId(id);
        
        // Si el usuario es Cliente, solo puede ver sus propias órdenes
        boolean esCliente = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("Cliente"));
        
        if (esCliente) {
            Integer clienteId = obtenerClienteId(userDetails);
            if (!orden.cliente().id().equals(clienteId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        
        return ResponseEntity.ok(orden);
    }

    @GetMapping("/ordenes")
    @PreAuthorize("hasAnyAuthority('Empleado', 'Administrador')")
    @Operation(summary = "Listar todas las órdenes", description = "Retorna todas las órdenes existentes.")
    public ResponseEntity<List<OrdenResumenResponse>> listarOrdenes() {
        return ResponseEntity.ok(ordenService.listarOrdenes());
    }

    @GetMapping("/mis-ordenes")
    @PreAuthorize("hasAuthority('Cliente')")
    @Operation(summary = "Obtener mis órdenes", description = "Retorna todas las órdenes del cliente autenticado")
    public ResponseEntity<List<OrdenResumenResponse>> obtenerMisOrdenes(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ordenService.obtenerOrdenesPorCliente(obtenerClienteId(userDetails)));
    }

    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasAnyAuthority('Empleado', 'Administrador')")
    @Operation(summary = "Obtener órdenes por cliente", description = "Retorna todas las órdenes de un cliente específico. Solo accesible para empleados y administradores")
    public ResponseEntity<List<OrdenResumenResponse>> obtenerOrdenesPorCliente(@PathVariable Integer clienteId) {
        return ResponseEntity.ok(ordenService.obtenerOrdenesPorCliente(clienteId));
    }

    @GetMapping("/estado/{codigoEstado}")
    @PreAuthorize("hasAnyAuthority('Empleado', 'Administrador')")
    @Operation(summary = "Obtener órdenes por estado", description = "Retorna todas las órdenes con un estado específico")
    public ResponseEntity<List<OrdenResumenResponse>> obtenerOrdenesPorEstado(@PathVariable EstadoOrdenCodigo codigoEstado) {
        return ResponseEntity.ok(ordenService.obtenerOrdenesPorEstado(codigoEstado));
    }

    @PutMapping("/{ordenId}/estado")
    @PreAuthorize("hasAnyAuthority('Empleado', 'Administrador')")
    @Operation(summary = "Cambiar estado de orden", description = "Actualiza el estado de una orden")
    public ResponseEntity<OrdenResponse> cambiarEstado(@PathVariable Integer ordenId, @Valid @RequestBody CambiarEstadoOrdenRequest request) {
        return ResponseEntity.ok(ordenService.cambiarEstado(ordenId, request));
    }

    @PostMapping("/{ordenId}/cancelar")
    @PreAuthorize("hasAnyAuthority('Cliente', 'Empleado', 'Administrador')")
    @Operation(summary = "Cancelar orden", description = "Cancela una orden y devuelve el stock al inventario")
    public ResponseEntity<OrdenResponse> cancelarOrden(@PathVariable Integer ordenId, @RequestParam(required = false) String motivo) {
        return ResponseEntity.ok(ordenService.cancelarOrden(ordenId, motivo));
    }
}
