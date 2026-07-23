package com.ecommerce.app.shared.interfaces.controller;

import java.util.List;

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

import com.ecommerce.app.shared.application.dto.request.ActualizarAreaRequest;
import com.ecommerce.app.shared.application.dto.request.CrearAreaRequest;
import com.ecommerce.app.shared.application.dto.response.AreaResponse;
import com.ecommerce.app.shared.application.service.AreaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/areas")
@Tag(name = "Áreas", description = "Operaciones relacionadas con áreas de trabajo de empleados")
public class AreaController {
	
	private final AreaService areaService;

	public AreaController(AreaService areaService) {
		this.areaService = areaService;
	}
	
	@GetMapping
	@PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener lista de áreas", description = "Devuelve todas las áreas de empleados disponibles")
    public ResponseEntity<List<AreaResponse>> listarAreas() {
        List<AreaResponse> areas = areaService.listarAreas();
        return ResponseEntity.ok(areas);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener área por ID", description = "Devuelve un área específica según su ID")
    public ResponseEntity<AreaResponse> obtenerPorId(@PathVariable int id) {
    	AreaResponse cargo = areaService.obtenerPorId(id);
        return ResponseEntity.ok(cargo);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Registrar una nueva área", description = "Registra un área en la base de datos")
    public ResponseEntity<AreaResponse> registrarArea(@RequestBody CrearAreaRequest request) {
        AreaResponse cargo = areaService.registrarArea(request);
        return ResponseEntity.ok(cargo);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Editar área", description = "Actualiza los datos de un área existente")
    public ResponseEntity<AreaResponse> editarArea(@RequestBody ActualizarAreaRequest request) {
        AreaResponse cargo = areaService.editarArea(request);
        return ResponseEntity.ok(cargo);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Eliminar un área", description = "Elimina un área no relacionada con empleados")
    public ResponseEntity<Void> eliminarArea(@PathVariable int id) {
    	areaService.eliminarArea(id);
        return ResponseEntity.noContent().build();
    }
	
}