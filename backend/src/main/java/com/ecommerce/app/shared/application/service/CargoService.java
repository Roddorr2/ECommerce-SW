package com.ecommerce.app.shared.application.service;

import com.ecommerce.app.shared.application.dto.request.ActualizarCargoRequest;
import com.ecommerce.app.shared.application.dto.request.CrearCargoRequest;
import com.ecommerce.app.shared.application.dto.response.CargoResponse;
import com.ecommerce.app.shared.application.mapper.CargoMapper;
import com.ecommerce.app.shared.domain.model.Cargo;
import com.ecommerce.app.shared.domain.port.CargoPort;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CargoService {

    private final CargoPort cargoPort;
    private final CargoMapper cargoMapper;

    public CargoService(CargoPort cargoPort, CargoMapper cargoMapper) {
        this.cargoPort = cargoPort;
        this.cargoMapper = cargoMapper;
    }

    public List<CargoResponse> listarCargos() {
        return cargoPort.findAll().stream()
                .map(cargoMapper::toResponse)
                .toList();
    }

    public CargoResponse obtenerPorId(int id) {
        Cargo cargo = cargoPort.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cargo no encontrado con ID: " + id));
        return cargoMapper.toResponse(cargo);
    }

    @Transactional
    public CargoResponse registrarCargo(CrearCargoRequest request) {
        Cargo cargo = cargoMapper.toEntity(request);
        Cargo savedCargo = cargoPort.save(cargo);
        return cargoMapper.toResponse(savedCargo);
    }

    @Transactional
    public CargoResponse editarCargo(ActualizarCargoRequest request) {
        Cargo cargo = cargoPort.findById(request.id())
                .orElseThrow(() -> new EntityNotFoundException("Cargo no encontrado con ID: " + request.id()));
        cargoMapper.updateEntity(cargo, request);
        Cargo updatedCargo = cargoPort.save(cargo);
        return cargoMapper.toResponse(updatedCargo);
    }

    @Transactional
    public void eliminarCargo(int id) {
        Cargo cargo = cargoPort.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cargo no encontrado con ID: " + id));
        cargoPort.delete(cargo);
    }
}
