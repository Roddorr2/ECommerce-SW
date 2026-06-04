package com.ecommerce.app.auth.infrastructure.persistence;

import com.ecommerce.app.auth.domain.enums.EstadoCodigoVerificacionCodigo;
import com.ecommerce.app.auth.domain.model.CodigoVerificacion;
import com.ecommerce.app.auth.domain.port.CodigoVerificacionPort;
import com.ecommerce.app.auth.infrastructure.persistence.entity.CodigoVerificacionEntity;
import com.ecommerce.app.auth.infrastructure.persistence.mapper.AuthDbMapper;
import com.ecommerce.app.auth.infrastructure.persistence.repository.CodigoVerificacionJpaRepository;
import com.ecommerce.app.shared.domain.model.Usuario;
import com.ecommerce.app.shared.infrastructure.persistence.mapper.SharedDbMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CodigoVerificacionJpaAdapter implements CodigoVerificacionPort {

    private final CodigoVerificacionJpaRepository repository;
    private final AuthDbMapper mapper;
    private final SharedDbMapper sharedDbMapper;

    public CodigoVerificacionJpaAdapter(CodigoVerificacionJpaRepository repository, AuthDbMapper mapper, SharedDbMapper sharedDbMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.sharedDbMapper = sharedDbMapper;
    }

    @Override
    public Optional<CodigoVerificacion> buscarPorUsuarioYCodigoYEstado(Usuario usuario, String codigo, EstadoCodigoVerificacionCodigo estado) {
        return repository.findByUsuarioAndCodigoAndEstado(sharedDbMapper.toEntity(usuario), codigo, estado)
                .map(mapper::toDomain);
    }

    @Override
    public long contarPorUsuarioYFechaGeneracionDespues(Usuario usuario, LocalDateTime desde) {
        return repository.countByUsuarioAndFechaGeneracionAfter(sharedDbMapper.toEntity(usuario), desde);
    }

    @Override
    public List<CodigoVerificacion> buscarPorUsuarioYEstado(Usuario usuario, EstadoCodigoVerificacionCodigo estado) {
        return repository.findByUsuarioAndEstado(sharedDbMapper.toEntity(usuario), estado)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<CodigoVerificacion> buscarCodigosExpirados(EstadoCodigoVerificacionCodigo estado, LocalDateTime fecha) {
        return repository.findCodigosExpirados(estado, fecha)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public CodigoVerificacion guardar(CodigoVerificacion codigo) {
        CodigoVerificacionEntity entity = mapper.toEntity(codigo);
        CodigoVerificacionEntity guardado = repository.save(entity);
        return mapper.toDomain(guardado);
    }

    @Override
    public List<CodigoVerificacion> guardarTodos(List<CodigoVerificacion> codigos) {
        List<CodigoVerificacionEntity> entities = codigos.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList());
        List<CodigoVerificacionEntity> guardados = repository.saveAll(entities);
        return guardados.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
