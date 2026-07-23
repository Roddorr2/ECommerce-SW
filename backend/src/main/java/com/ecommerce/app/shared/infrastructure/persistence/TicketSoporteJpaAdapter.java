package com.ecommerce.app.shared.infrastructure.persistence;

import com.ecommerce.app.shared.domain.model.EstadoTicket;
import com.ecommerce.app.shared.domain.model.TicketSoporte;
import com.ecommerce.app.shared.domain.port.TicketSoporteRepositoryPort;
import com.ecommerce.app.shared.infrastructure.persistence.entity.TicketSoporteEntity;
import com.ecommerce.app.shared.infrastructure.persistence.mapper.TicketSoporteMapper;
import com.ecommerce.app.shared.infrastructure.persistence.repository.TicketSoporteJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TicketSoporteJpaAdapter implements TicketSoporteRepositoryPort {

    private final TicketSoporteJpaRepository repository;
    private final TicketSoporteMapper mapper;

    public TicketSoporteJpaAdapter(TicketSoporteJpaRepository repository, TicketSoporteMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public TicketSoporte guardar(TicketSoporte ticketSoporte) {
        TicketSoporteEntity entity = mapper.toEntity(ticketSoporte);
        TicketSoporteEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public List<TicketSoporte> guardarTodos(List<TicketSoporte> tickets) {
        List<TicketSoporteEntity> entities = tickets.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList());
        List<TicketSoporteEntity> saved = repository.saveAll(entities);
        return saved.stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<TicketSoporte> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<TicketSoporte> buscarPorCodigo(String codigoTicket) {
        return repository.findByCodigoTicket(codigoTicket).map(mapper::toDomain);
    }

    @Override
    public List<TicketSoporte> listarTodos() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketSoporte> buscarPorEstado(EstadoTicket estado) {
        return repository.findByEstado(estado).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketSoporte> buscarInactivosPendientesAntesDe(EstadoTicket estado, LocalDateTime fechaLimite) {
        return repository.findInactivosPorEstadoYFechaLimite(estado, fechaLimite).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
