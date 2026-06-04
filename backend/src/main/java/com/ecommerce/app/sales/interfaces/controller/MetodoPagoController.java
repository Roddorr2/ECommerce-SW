package com.ecommerce.app.sales.interfaces.controller;

import com.ecommerce.app.sales.application.dto.request.ActualizarMetodoPagoRequest;
import com.ecommerce.app.sales.application.dto.request.CrearMetodoPagoRequest;
import com.ecommerce.app.sales.application.dto.response.MetodoPagoResponse;
import com.ecommerce.app.sales.application.service.MetodoPagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/metodos-pago")
@Tag(name = "Método de pago", description = "Operaciones relacionadas con los métodos de pago")
public class MetodoPagoController {

    private final MetodoPagoService metodoPagoService;

    public MetodoPagoController(MetodoPagoService metodoPagoService) {
        this.metodoPagoService = metodoPagoService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener lista de métodos de pago", description = "Devuelve todas los métodos de pago disponibles")
    public ResponseEntity<List<MetodoPagoResponse>> listarMetodosPago() {
        List<MetodoPagoResponse> metodosPago = metodoPagoService.listarMetodosPago();
        return ResponseEntity.ok(metodosPago);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener método de pago por ID", description = "Devuelve un método de pago específico según su ID")
    public ResponseEntity<MetodoPagoResponse> obtenerPorId(@PathVariable int id) {
        MetodoPagoResponse metodoPago = metodoPagoService.obtenerPorId(id);
        return ResponseEntity.ok(metodoPago);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Registrar un nuevo método de pago", description = "Registra un método de pago en la base de datos")
    public ResponseEntity<MetodoPagoResponse> registrarNetodoPago(@Valid @RequestBody CrearMetodoPagoRequest request) {
        MetodoPagoResponse metodoPago = metodoPagoService.registrarNetodoPago(request);
        return ResponseEntity.ok(metodoPago);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Editar método de pago", description = "Actualiza los datos de un método de pago existente")
    public ResponseEntity<MetodoPagoResponse> editarMetodoPago(@Valid @RequestBody ActualizarMetodoPagoRequest request) {
        MetodoPagoResponse metodoPago = metodoPagoService.editarMetodoPago(request);
        return ResponseEntity.ok(metodoPago);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Eliminar un método de pago", description = "Elimina un método de pago no relacionada con compras u órdenes")
    public ResponseEntity<Void> eliminarMetodoPago(@PathVariable int id) {
        metodoPagoService.eliminarMetodoPago(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/{nombre}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Buscar métodos de pago por nombre", description = "Busca métodos de pago por su nombre")
    public ResponseEntity<List<MetodoPagoResponse>> buscarPorNombre(@PathVariable String nombre) {
        List<MetodoPagoResponse> metodosPago = metodoPagoService.buscarPorNombre(nombre);
        return ResponseEntity.ok(metodosPago);
    }
}
