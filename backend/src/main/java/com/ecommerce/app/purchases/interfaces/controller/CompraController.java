package com.ecommerce.app.purchases.interfaces.controller;

import com.ecommerce.app.purchases.application.dto.request.CambiarEstadoCompraRequest;
import com.ecommerce.app.purchases.application.dto.request.CrearCompraRequest;
import com.ecommerce.app.purchases.application.dto.response.CompraResponse;
import com.ecommerce.app.purchases.application.dto.response.CompraResumenResponse;
import com.ecommerce.app.purchases.application.service.CompraService;
import com.ecommerce.app.purchases.domain.enums.EstadoCompraCodigo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/compras")
@Tag(name = "Compra", description = "Operaciones relacionadas con compras")
public class CompraController {

    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('Empleado', 'Administrador')")
    @Operation(summary = "Listar todas las compras", description = "Retorna el listado completo de compras registradas")
    public ResponseEntity<List<CompraResumenResponse>> listarCompras() {
        return ResponseEntity.ok(compraService.listarCompras());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('Empleado', 'Administrador')")
    @Operation(summary = "Obtener compra por ID", description = "Retorna los detalles de una compra específica")
    public ResponseEntity<CompraResponse> obtenerPorId(@PathVariable int id) {
        return ResponseEntity.ok(compraService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('Empleado', 'Administrador')")
    @Operation(summary = "Crear nueva compra", description = "Registra una nueva compra a proveedor")
    public ResponseEntity<CompraResponse> crearCompra(@Valid @RequestBody CrearCompraRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(compraService.crearCompra(request));
    }

    @PostMapping("/{compraId}/recibir")
    @PreAuthorize("hasAnyAuthority('Empleado', 'Administrador')")
    @Operation(summary = "Recibir compra", description = "Marca la compra como recibida e incrementa el stock de productos")
    public ResponseEntity<CompraResponse> recibirCompra(@PathVariable Integer compraId) {
        return ResponseEntity.ok(compraService.recibirCompra(compraId));
    }

    @PostMapping("/{compraId}/cancelar")
    @PreAuthorize("hasAnyAuthority('Empleado', 'Administrador')")
    @Operation(summary = "Cancelar compra", description = "Cancela una compra pendiente")
    public ResponseEntity<CompraResponse> cancelarCompra(@PathVariable Integer compraId, @RequestParam(required = false) String motivo) {
        return ResponseEntity.ok(compraService.cancelarCompra(compraId, motivo));
    }

    @PutMapping("/{compraId}/estado")
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Cambiar estado de compra", description = "Actualiza el estado de una compra")
    public ResponseEntity<CompraResponse> cambiarEstado(@PathVariable Integer compraId, @Valid @RequestBody CambiarEstadoCompraRequest request) {
        return ResponseEntity.ok(compraService.cambiarEstado(compraId, request));
    }

    @GetMapping("/proveedor/{proveedorId}")
    @PreAuthorize("hasAnyAuthority('Empleado', 'Administrador')")
    @Operation(summary = "Obtener compras por proveedor", description = "Retorna todas las compras de un proveedor específico")
    public ResponseEntity<List<CompraResumenResponse>> obtenerComprasPorProveedor(@PathVariable Integer proveedorId) {
        return ResponseEntity.ok(compraService.obtenerComprasPorProveedor(proveedorId));
    }

    @GetMapping("/estado/{codigoEstado}")
    @PreAuthorize("hasAnyAuthority('Empleado', 'Administrador')")
    @Operation(summary = "Obtener compras por estado", description = "Retorna todas las compras con un estado específico")
    public ResponseEntity<List<CompraResumenResponse>> obtenerComprasPorEstado(@PathVariable EstadoCompraCodigo codigoEstado) {
        return ResponseEntity.ok(compraService.obtenerComprasPorEstado(codigoEstado));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Eliminar compra", description = "Elimina una compra que no haya sido recibida")
    public ResponseEntity<Void> eliminarCompra(@PathVariable int id) {
        compraService.eliminarCompra(id);
        return ResponseEntity.noContent().build();
    }
}