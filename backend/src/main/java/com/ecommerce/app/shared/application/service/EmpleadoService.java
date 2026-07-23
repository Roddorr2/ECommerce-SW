package com.ecommerce.app.shared.application.service;

import com.ecommerce.app.shared.application.dto.request.ActualizarEmpleadoRequest;
import com.ecommerce.app.shared.application.dto.request.CrearEmpleadoRequest;
import com.ecommerce.app.shared.application.dto.response.EmpleadoResponse;
import com.ecommerce.app.shared.application.mapper.EmpleadoMapper;
import com.ecommerce.app.shared.domain.model.Area;
import com.ecommerce.app.shared.domain.model.Cargo;
import com.ecommerce.app.shared.domain.model.Empleado;
import com.ecommerce.app.shared.domain.model.Usuario;
import com.ecommerce.app.shared.domain.port.AreaPort;
import com.ecommerce.app.shared.domain.port.CargoPort;
import com.ecommerce.app.shared.domain.port.EmpleadoPort;
import com.ecommerce.app.shared.domain.port.UsuarioPort;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpleadoService {

    private final EmpleadoPort empleadoPort;
    private final UsuarioPort usuarioPort;
    private final AreaPort areaPort;
    private final CargoPort cargoPort;
    private final EmpleadoMapper empleadoMapper;

    public EmpleadoService(EmpleadoPort empleadoPort, UsuarioPort usuarioPort, AreaPort areaPort, CargoPort cargoPort, EmpleadoMapper empleadoMapper) {
        this.empleadoPort = empleadoPort;
        this.usuarioPort = usuarioPort;
        this.areaPort = areaPort;
        this.cargoPort = cargoPort;
        this.empleadoMapper = empleadoMapper;
    }

    public List<EmpleadoResponse> listarEmpleados() {
        return empleadoPort.findAll().stream()
                .map(empleadoMapper::toResponse)
                .toList();
    }

    public EmpleadoResponse obtenerPorId(int id) {
        Empleado empleado = empleadoPort.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado con ID: " + id));
        return empleadoMapper.toResponse(empleado);
    }

    @Transactional
    public EmpleadoResponse registrarEmpleado(CrearEmpleadoRequest request) {
        empleadoPort.findByUsuarioId(request.usuarioId()).ifPresent(e -> {
            throw new IllegalArgumentException("El usuario ya está asignado a otro empleado.");
        });

        Usuario usuario = usuarioPort.findById(request.usuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + request.usuarioId()));

        Area area = areaPort.findById(request.areaId())
                .orElseThrow(() -> new EntityNotFoundException("Área no encontrada con ID: " + request.areaId()));

        Cargo cargo = cargoPort.findById(request.cargoId())
                .orElseThrow(() -> new EntityNotFoundException("Cargo no encontrado con ID: " + request.cargoId()));

        Empleado empleado = empleadoMapper.toEntity(request, usuario, area, cargo);
        Empleado savedEmpleado = empleadoPort.save(empleado);
        return empleadoMapper.toResponse(savedEmpleado);
    }

    @Transactional
    public EmpleadoResponse editarEmpleado(ActualizarEmpleadoRequest request) {
        Empleado empleado = empleadoPort.findById(request.id())
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado con ID: " + request.id()));

        empleadoPort.findByUsuarioId(request.usuarioId()).ifPresent(e -> {
            if (!e.getId().equals(request.id())) {
                throw new IllegalArgumentException("El usuario ya está asignado a otro empleado.");
            }
        });

        Usuario usuario = usuarioPort.findById(request.usuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + request.usuarioId()));

        Area area = areaPort.findById(request.areaId())
                .orElseThrow(() -> new EntityNotFoundException("Área no encontrada con ID: " + request.areaId()));

        Cargo cargo = cargoPort.findById(request.cargoId())
                .orElseThrow(() -> new EntityNotFoundException("Cargo no encontrado con ID: " + request.cargoId()));

        empleadoMapper.updateEntity(empleado, request, usuario, area, cargo);
        Empleado updatedEmpleado = empleadoPort.save(empleado);
        return empleadoMapper.toResponse(updatedEmpleado);
    }

    @Transactional
    public void eliminarEmpleado(int id) {
        Empleado empleado = empleadoPort.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado con ID: " + id));
        empleadoPort.delete(empleado);
    }
}
