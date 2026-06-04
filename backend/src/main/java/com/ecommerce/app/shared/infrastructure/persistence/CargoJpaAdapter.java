package com.ecommerce.app.shared.infrastructure.persistence;

import com.ecommerce.app.shared.domain.model.Cargo;
import com.ecommerce.app.shared.domain.port.CargoPort;
import com.ecommerce.app.shared.infrastructure.persistence.entity.CargoEntity;
import com.ecommerce.app.shared.infrastructure.persistence.mapper.SharedDbMapper;
import com.ecommerce.app.shared.infrastructure.persistence.repository.CargoJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CargoJpaAdapter implements CargoPort {

    private final CargoJpaRepository repository;
    private final SharedDbMapper mapper;

    public CargoJpaAdapter(CargoJpaRepository repository, SharedDbMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<Cargo> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Cargo> findById(Integer id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Cargo save(Cargo cargo) {
        CargoEntity entity = mapper.toEntity(cargo);
        CargoEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public void delete(Cargo cargo) {
        repository.delete(mapper.toEntity(cargo));
    }
}
