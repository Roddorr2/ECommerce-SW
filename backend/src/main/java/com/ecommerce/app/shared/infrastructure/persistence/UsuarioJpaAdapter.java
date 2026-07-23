package com.ecommerce.app.shared.infrastructure.persistence;

import com.ecommerce.app.shared.domain.model.Usuario;
import com.ecommerce.app.shared.domain.port.UsuarioPort;
import com.ecommerce.app.shared.infrastructure.persistence.entity.UsuarioEntity;
import com.ecommerce.app.shared.infrastructure.persistence.mapper.SharedDbMapper;
import com.ecommerce.app.shared.infrastructure.persistence.repository.UsuarioJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UsuarioJpaAdapter implements UsuarioPort {

    private final UsuarioJpaRepository repository;
    private final SharedDbMapper mapper;

    public UsuarioJpaAdapter(UsuarioJpaRepository repository, SharedDbMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<Usuario> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Usuario> findById(Integer id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Usuario> findByCorreo(String correo) {
        return repository.findByCorreo(correo)
                .map(mapper::toDomain);
    }

    @Override
    public Usuario save(Usuario usuario) {
        UsuarioEntity entity = mapper.toEntity(usuario);
        UsuarioEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public void delete(Usuario usuario) {
        repository.delete(mapper.toEntity(usuario));
    }
}
