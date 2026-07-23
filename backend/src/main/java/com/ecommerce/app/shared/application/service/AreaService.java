package com.ecommerce.app.shared.application.service;

import com.ecommerce.app.shared.application.dto.request.ActualizarAreaRequest;
import com.ecommerce.app.shared.application.dto.request.CrearAreaRequest;
import com.ecommerce.app.shared.application.dto.response.AreaResponse;
import com.ecommerce.app.shared.application.mapper.AreaMapper;
import com.ecommerce.app.shared.domain.model.Area;
import com.ecommerce.app.shared.domain.port.AreaPort;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaService {

    private final AreaPort areaPort;
    private final AreaMapper areaMapper;

    public AreaService(AreaPort areaPort, AreaMapper areaMapper) {
        this.areaPort = areaPort;
        this.areaMapper = areaMapper;
    }

    public List<AreaResponse> listarAreas() {
        return areaPort.findAll().stream()
                .map(areaMapper::toResponse)
                .toList();
    }

    public AreaResponse obtenerPorId(int id) {
        Area area = areaPort.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Área no encontrada con ID: " + id));
        return areaMapper.toResponse(area);
    }

    @Transactional
    public AreaResponse registrarArea(CrearAreaRequest request) {
        Area area = areaMapper.toEntity(request);
        Area savedArea = areaPort.save(area);
        return areaMapper.toResponse(savedArea);
    }

    @Transactional
    public AreaResponse editarArea(ActualizarAreaRequest request) {
        Area area = areaPort.findById(request.id())
                .orElseThrow(() -> new EntityNotFoundException("Área no encontrada con ID: " + request.id()));
        areaMapper.updateEntity(area, request);
        Area updatedArea = areaPort.save(area);
        return areaMapper.toResponse(updatedArea);
    }

    @Transactional
    public void eliminarArea(int id) {
        Area area = areaPort.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Área no encontrada con ID: " + id));
        areaPort.delete(area);
    }
}
