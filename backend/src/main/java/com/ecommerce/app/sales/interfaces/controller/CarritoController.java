package com.ecommerce.app.sales.interfaces.controller;

import com.ecommerce.app.sales.application.dto.request.ActualizarItemCarritoRequest;
import com.ecommerce.app.sales.application.dto.request.AgregarItemCarritoRequest;
import com.ecommerce.app.sales.application.dto.response.CarritoResponse;
import com.ecommerce.app.sales.application.service.CarritoService;
import com.ecommerce.app.sales.domain.port.ClientePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Carrito de Compras", description = "Endpoints para gestionar el carrito de compras del cliente")
public class CarritoController {

    private final CarritoService carritoService;
    private final ClientePort clientePort;

    public CarritoController(CarritoService carritoService, ClientePort clientePort) {
        this.carritoService = carritoService;
        this.clientePort = clientePort;
    }

    private Integer obtenerClienteId(UserDetails userDetails) {
        return clientePort.buscarPorUsuarioCorreo(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado.")).getId();
    }

    @GetMapping
    @PreAuthorize("hasRole('Cliente')")
    @Operation(summary = "Obtener carrito activo", description = "Retorna el carrito activo del cliente o crea uno nuevo si no existe")
    public ResponseEntity<CarritoResponse> obtenerCarritoActivo(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(carritoService.obtenerCarritoActivo(obtenerClienteId(userDetails)));
    }

    @PostMapping("/{clienteId}/items")
    @PreAuthorize("hasRole('Cliente')")
    @Operation(summary = "Agregar item al carrito", description = "Agrega un producto al carrito o incrementa la cantidad si ya existe")
    public ResponseEntity<CarritoResponse> agregarItem(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody AgregarItemCarritoRequest request) {
        return ResponseEntity.ok(carritoService.agregarItem(obtenerClienteId(userDetails), request));
    }

    @PreAuthorize("hasRole('Cliente')")
    @PutMapping("/items/{itemId}")
    @Operation(summary = "Actualizar cantidad de item", description = "Modifica la cantidad de un item específico del carrito")
    public ResponseEntity<CarritoResponse> actualizarItem(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer itemId, @Valid @RequestBody ActualizarItemCarritoRequest request) {
        return ResponseEntity.ok(carritoService.actualizarItem(obtenerClienteId(userDetails), itemId, request));
    }

    @DeleteMapping("/items/{itemId}")
    @PreAuthorize("hasRole('Cliente')")
    @Operation(summary = "Eliminar item del carrito", description = "Elimina un producto específico del carrito")
    public ResponseEntity<CarritoResponse> eliminarItem(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Integer itemId) {
        return ResponseEntity.ok(carritoService.eliminarItem(obtenerClienteId(userDetails), itemId));
    }

    @DeleteMapping("/vaciar")
    @PreAuthorize("hasRole('Cliente')")
    @Operation(summary = "Vaciar carrito", description = "Elimina todos los items del carrito activo")
    public ResponseEntity<Void> vaciarCarrito(@AuthenticationPrincipal UserDetails userDetails) {
        carritoService.vaciarCarrito(obtenerClienteId(userDetails));
        return ResponseEntity.noContent().build();
    }
}
