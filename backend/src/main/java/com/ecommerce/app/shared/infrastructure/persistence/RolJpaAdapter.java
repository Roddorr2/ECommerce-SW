package com.ecommerce.app.shared.infrastructure.persistence;

import com.ecommerce.app.shared.domain.model.Rol;
import com.ecommerce.app.shared.domain.port.RolPort;
import com.ecommerce.app.shared.infrastructure.persistence.entity.RolEntity;
import com.ecommerce.app.shared.infrastructure.persistence.mapper.SharedDbMapper;
import com.ecommerce.app.shared.infrastructure.persistence.repository.RolJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RolJpaAdapter implements RolPort {

    private final RolJpaRepository repository;
    private final SharedDbMapper mapper;

    public RolJpaAdapter(RolJpaRepository repository, SharedDbMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<Rol> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Rol> findById(Integer id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Rol> findByNombre(String nombre) {
        return repository.findByNombre(nombre)
                .map(mapper::toDomain);
    }

    @Override
    public List<Rol> findByNombreContaining(String nombre) {
        return repository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Rol save(Rol rol) {
        RolEntity entity = mapper.toEntity(rol);
        RolEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public void delete(Rol rol) {
        repository.delete(mapper.toEntity(rol));
    }
}
