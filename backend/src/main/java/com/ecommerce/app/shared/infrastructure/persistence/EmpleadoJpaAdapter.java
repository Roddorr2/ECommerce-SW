package com.ecommerce.app.shared.infrastructure.persistence;

import com.ecommerce.app.shared.domain.model.Empleado;
import com.ecommerce.app.shared.domain.port.EmpleadoPort;
import com.ecommerce.app.shared.infrastructure.persistence.entity.EmpleadoEntity;
import com.ecommerce.app.shared.infrastructure.persistence.mapper.SharedDbMapper;
import com.ecommerce.app.shared.infrastructure.persistence.repository.EmpleadoJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EmpleadoJpaAdapter implements EmpleadoPort {

    private final EmpleadoJpaRepository repository;
    private final SharedDbMapper mapper;

    public EmpleadoJpaAdapter(EmpleadoJpaRepository repository, SharedDbMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<Empleado> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Empleado> findById(Integer id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Empleado> findByUsuarioId(Integer usuarioId) {
        return repository.findByUsuarioId(usuarioId)
                .map(mapper::toDomain);
    }

    @Override
    public Empleado save(Empleado empleado) {
        EmpleadoEntity entity = mapper.toEntity(empleado);
        EmpleadoEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public void delete(Empleado empleado) {
        repository.delete(mapper.toEntity(empleado));
    }
}
