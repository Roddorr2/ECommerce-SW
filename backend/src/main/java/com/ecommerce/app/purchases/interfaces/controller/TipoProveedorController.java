package com.ecommerce.app.purchases.interfaces.controller;

import java.util.List;

import com.ecommerce.app.purchases.application.dto.request.ActualizarTipoProveedorRequest;
import com.ecommerce.app.purchases.application.dto.request.CrearTipoProveedorRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.app.purchases.application.dto.response.TipoProveedorResponse;
import com.ecommerce.app.purchases.application.service.TipoProveedorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/tipos-proveedor")
@Tag(name = "TipoProveedor", description = "Operaciones relacionadas con los tipos de proveedor")
public class TipoProveedorController {

    private final TipoProveedorService tipoProveedorService;

    public TipoProveedorController(TipoProveedorService tipoProveedorService) {
        this.tipoProveedorService = tipoProveedorService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener lista de tipos de proveedor", description = "Devuelve todos los tipos de proveedor disponibles")
    public ResponseEntity<List<TipoProveedorResponse>> listarTiposProveedor() {
        List<TipoProveedorResponse> tiposProveedor = tipoProveedorService.listarTipoProveedores();
        return ResponseEntity.ok(tiposProveedor);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener tipo de proveedor por ID", description = "Devuelve un tipo de proveedor específico según su ID")
    public ResponseEntity<TipoProveedorResponse> obtenerPorId(@PathVariable int id) {
        TipoProveedorResponse tipoProveedor = tipoProveedorService.obtenerPorId(id);
        return ResponseEntity.ok(tipoProveedor);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Registrar un nuevo tipo de proveedor", description = "Registra un tipo de proveedor en la base de datos")
    public ResponseEntity<TipoProveedorResponse> registrarTipoProveedor(@RequestBody CrearTipoProveedorRequest request) {
        TipoProveedorResponse tipoProveedor = tipoProveedorService.registrarTipoProveedor(request);
        return ResponseEntity.ok(tipoProveedor);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Editar tipo de proveedor", description = "Actualiza los datos de un tipo de proveedor existente")
    public ResponseEntity<TipoProveedorResponse> editarTipoProveedor(@RequestBody ActualizarTipoProveedorRequest request) {
        TipoProveedorResponse tipoProveedor = tipoProveedorService.editarTipoProveedor(request);
        return ResponseEntity.ok(tipoProveedor);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Eliminar un tipo de proveedor", description = "Elimina un tipo de proveedor no relacionado con proveedores")
    public ResponseEntity<Void> eliminarTipoProveedor(@PathVariable int id) {
        tipoProveedorService.eliminarTipoProveedor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/{name}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Buscar un tipo de proveedor por nombre", description = "Busca un tipo de proveedor por su nombre")
    public ResponseEntity<List<TipoProveedorResponse>> buscarTipoProveedorPorNombre(@PathVariable String name) {
        List<TipoProveedorResponse> tipoProveedor = tipoProveedorService.buscarPorNombre(name);
        return ResponseEntity.ok(tipoProveedor);
    }
}