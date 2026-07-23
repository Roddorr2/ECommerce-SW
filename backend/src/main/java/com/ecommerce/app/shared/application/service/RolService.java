package com.ecommerce.app.shared.application.service;

import com.ecommerce.app.shared.application.dto.request.ActualizarRolRequest;
import com.ecommerce.app.shared.application.dto.request.CrearRolRequest;
import com.ecommerce.app.shared.application.dto.response.RolResponse;
import com.ecommerce.app.shared.application.mapper.RolMapper;
import com.ecommerce.app.shared.domain.model.Rol;
import com.ecommerce.app.shared.domain.port.RolPort;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolService {

    private final RolPort rolPort;
    private final RolMapper rolMapper;

    public RolService(RolPort rolPort, RolMapper rolMapper) {
        this.rolPort = rolPort;
        this.rolMapper = rolMapper;
    }

    public List<RolResponse> listarRoles() {
        return rolPort.findAll().stream()
                .map(rolMapper::toResponse)
                .toList();
    }

    public RolResponse obtenerPorId(int id) {
        Rol rol = rolPort.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con ID: " + id));
        return rolMapper.toResponse(rol);
    }

    @Transactional
    public RolResponse registrarRol(CrearRolRequest request) {
        rolPort.findByNombre(request.nombre()).ifPresent(r -> {
            throw new IllegalArgumentException("Ya existe un rol con el nombre: " + request.nombre());
        });

        Rol rol = rolMapper.toEntity(request);
        Rol savedRol = rolPort.save(rol);
        return rolMapper.toResponse(savedRol);
    }

    @Transactional
    public RolResponse editarRol(ActualizarRolRequest request) {
        Rol rol = rolPort.findById(request.id())
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con ID: " + request.id()));

        rolPort.findByNombre(request.nombre()).ifPresent(r -> {
            if (!r.getId().equals(request.id())) {
                throw new IllegalArgumentException("Ya existe otro rol con el nombre: " + request.nombre());
            }
        });

        rolMapper.updateEntity(rol, request);
        Rol updatedRol = rolPort.save(rol);
        return rolMapper.toResponse(updatedRol);
    }

    @Transactional
    public void eliminarRol(int id) {
        Rol rol = rolPort.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con ID: " + id));
        rolPort.delete(rol);
    }

    public List<RolResponse> buscarPorNombre(String name) {
        return rolPort.findByNombreContaining(name).stream()
                .map(rolMapper::toResponse)
                .toList();
    }
}
