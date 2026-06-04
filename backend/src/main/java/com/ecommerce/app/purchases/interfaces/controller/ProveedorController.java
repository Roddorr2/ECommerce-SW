package com.ecommerce.app.purchases.interfaces.controller;

import java.util.List;

import com.ecommerce.app.purchases.application.dto.request.ActualizarProveedorRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.app.purchases.application.dto.request.CrearProveedorRequest;
import com.ecommerce.app.purchases.application.dto.response.ProveedorResponse;
import com.ecommerce.app.purchases.application.service.ProveedorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/proveedores")
@Tag(name = "Proveedor", description = "Operaciones relacionadas con proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener lista de proveedores", description = "Devuelve todos los proveedores registrados en el sistema")
    public ResponseEntity<List<ProveedorResponse>> listarProveedoress() {
        List<ProveedorResponse> proveedores = proveedorService.listarProveedoress();
        return ResponseEntity.ok(proveedores);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener proveedor por ID", description = "Devuelve un producto según su ID")
    public ResponseEntity<ProveedorResponse> obtenerPorId(@PathVariable int id) {
        ProveedorResponse proveedor = proveedorService.obtenerPorId(id);
        return ResponseEntity.ok(proveedor);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Registrar un nuevo proveedor", description = "Registra un nuevo proveedor en la base de datos")
    public ResponseEntity<ProveedorResponse> registrarProveedor(@RequestBody CrearProveedorRequest request) {
        ProveedorResponse proveedor = proveedorService.registrarProveedor(request);
        return ResponseEntity.ok(proveedor);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Editar proveedor", description = "Actualiza los datos de un proveedor existente")
    public ResponseEntity<ProveedorResponse> editarProveedor(@RequestBody ActualizarProveedorRequest request) {
        ProveedorResponse proveedor = proveedorService.editarProveedor(request);
        return ResponseEntity.ok(proveedor);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Eliminar un proveedor", description = "Elimina un proveedor no relacionado con compras")
    public ResponseEntity<Void> eliminarProveedor(@PathVariable int id) {
        proveedorService.eliminarProveedor(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search/{tipoProveedor}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Busca un tipo de proveedor", description = "Busca el tipo de proveedor")
    public ResponseEntity<List<ProveedorResponse>> buscarPorTipoProveedos(@PathVariable String tipoProveedor) {
    	List<ProveedorResponse> proveedores = proveedorService.buscarPorTipoProveedor(tipoProveedor);
    	return ResponseEntity.ok(proveedores);
    }
}