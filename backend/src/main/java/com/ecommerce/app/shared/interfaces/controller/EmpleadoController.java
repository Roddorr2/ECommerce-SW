package com.ecommerce.app.shared.interfaces.controller;

import com.ecommerce.app.shared.application.dto.request.ActualizarEmpleadoRequest;
import com.ecommerce.app.shared.application.dto.request.CrearEmpleadoRequest;
import com.ecommerce.app.shared.application.dto.response.EmpleadoResponse;
import com.ecommerce.app.shared.application.service.EmpleadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/empleados")
@Tag(name = "Empleados", description = "Operaciones relacionadas con empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Obtener lista de empleados", description = "Devuelve la lista de todos los empleados registrados")
    public ResponseEntity<List<EmpleadoResponse>> listarEmpleados() {
        List<EmpleadoResponse> empleados = empleadoService.listarEmpleados();
        return ResponseEntity.ok(empleados);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Obtener empleado por ID", description = "Devuelve un empleado según su ID")
    public ResponseEntity<EmpleadoResponse> obtenerPorId(@PathVariable int id) {
        EmpleadoResponse empleado = empleadoService.obtenerPorId(id);
        return ResponseEntity.ok(empleado);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Registrar un nuevo empleado", description = "Registra un nuevo empleado en la base de datos")
    public ResponseEntity<EmpleadoResponse> registrarEmpleado(@RequestBody CrearEmpleadoRequest request) {
        EmpleadoResponse empleado = empleadoService.registrarEmpleado(request);
        return ResponseEntity.ok(empleado);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Editar empleado", description = "Actualiza los datos de un empleado existente")
    public ResponseEntity<EmpleadoResponse> editarEmpleado(@RequestBody ActualizarEmpleadoRequest request) {
        EmpleadoResponse empleado = empleadoService.editarEmpleado(request);
        return ResponseEntity.ok(empleado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Eliminar empleado", description = "Elimina un empleado no relacionado con usuarios")
    public ResponseEntity<Void> eliminarEmpleado(@PathVariable int id) {
        empleadoService.eliminarEmpleado(id);
        return ResponseEntity.noContent().build();
    }
}
