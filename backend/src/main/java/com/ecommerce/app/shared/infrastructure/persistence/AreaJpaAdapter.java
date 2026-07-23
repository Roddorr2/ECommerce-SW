package com.ecommerce.app.shared.infrastructure.persistence;

import com.ecommerce.app.shared.domain.model.Area;
import com.ecommerce.app.shared.domain.port.AreaPort;
import com.ecommerce.app.shared.infrastructure.persistence.entity.AreaEntity;
import com.ecommerce.app.shared.infrastructure.persistence.mapper.SharedDbMapper;
import com.ecommerce.app.shared.infrastructure.persistence.repository.AreaJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AreaJpaAdapter implements AreaPort {

    private final AreaJpaRepository repository;
    private final SharedDbMapper mapper;

    public AreaJpaAdapter(AreaJpaRepository repository, SharedDbMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<Area> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Area> findById(Integer id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Area save(Area area) {
        AreaEntity entity = mapper.toEntity(area);
        AreaEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public void delete(Area area) {
        repository.delete(mapper.toEntity(area));
    }
}
