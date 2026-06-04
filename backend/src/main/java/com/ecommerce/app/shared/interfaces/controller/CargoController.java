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

import com.ecommerce.app.shared.application.dto.request.ActualizarCargoRequest;
import com.ecommerce.app.shared.application.dto.request.CrearCargoRequest;
import com.ecommerce.app.shared.application.dto.response.CargoResponse;
import com.ecommerce.app.shared.application.service.CargoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/cargos")
@Tag(name = "Cargos", description = "Operaciones relacionadas con cargos de empleados")
public class CargoController {
	
	private final CargoService cargoService;

	public CargoController(CargoService cargoService) {
		this.cargoService = cargoService;
	}
	
	@GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener lista de cargos", description = "Devuelve todos los cargos de empleados disponibles")
    public ResponseEntity<List<CargoResponse>> listarCargos() {
        List<CargoResponse> cargos = cargoService.listarCargos();
        return ResponseEntity.ok(cargos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener cargo por ID", description = "Devuelve un cargo específico según su ID")
    public ResponseEntity<CargoResponse> obtenerPorId(@PathVariable int id) {
    	CargoResponse cargo = cargoService.obtenerPorId(id);
        return ResponseEntity.ok(cargo);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Registrar un nuevo cargo", description = "Registra un cargo en la base de datos")
    public ResponseEntity<CargoResponse> registrarCargo(@RequestBody CrearCargoRequest request) {
        CargoResponse cargo = cargoService.registrarCargo(request);
        return ResponseEntity.ok(cargo);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Editar cargo", description = "Actualiza los datos de un cargo existente")
    public ResponseEntity<CargoResponse> editarCargo(@RequestBody ActualizarCargoRequest request) {
        CargoResponse cargo = cargoService.editarCargo(request);
        return ResponseEntity.ok(cargo);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Eliminar un cargo", description = "Elimina un cargo no relacionado con empleados")
    public ResponseEntity<Void> eliminarCargo(@PathVariable int id) {
    	cargoService.eliminarCargo(id);
        return ResponseEntity.noContent().build();
    }
	
}
