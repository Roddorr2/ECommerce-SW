package com.ecommerce.app.sales.infrastructure.persistence;

import com.ecommerce.app.sales.domain.enums.EstadoOrdenCodigo;
import com.ecommerce.app.sales.domain.model.Cliente;
import com.ecommerce.app.sales.domain.model.Orden;
import com.ecommerce.app.sales.domain.port.OrdenPort;
import com.ecommerce.app.sales.infrastructure.persistence.entity.ClienteEntity;
import com.ecommerce.app.sales.infrastructure.persistence.entity.OrdenEntity;
import com.ecommerce.app.sales.infrastructure.persistence.mapper.SalesDbMapper;
import com.ecommerce.app.sales.infrastructure.persistence.repository.OrdenJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OrdenJpaAdapter implements OrdenPort {
    private final OrdenJpaRepository ordenRepository;
    private final SalesDbMapper salesDbMapper;

    public OrdenJpaAdapter(OrdenJpaRepository ordenRepository, SalesDbMapper salesDbMapper) {
        this.ordenRepository = ordenRepository;
        this.salesDbMapper = salesDbMapper;
    }

    @Override
    public Optional<Orden> buscarPorId(Integer id) {
        return ordenRepository.findById(id)
                .map(salesDbMapper::toDomain);
    }

    @Override
    public List<Orden> buscarPorClienteOrdenado(Cliente cliente) {
        ClienteEntity clienteEntity = salesDbMapper.toEntity(cliente);
        return ordenRepository.findByClienteOrderByFechaOrdenDesc(clienteEntity)
                .stream()
                .map(salesDbMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Orden> buscarPorEstadoOrdenado(EstadoOrdenCodigo estadoOrden) {
        return ordenRepository.findByEstadoOrdenOrderByFechaOrdenDesc(estadoOrden)
                .stream()
                .map(salesDbMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Orden> listarTodas() {
        return ordenRepository.findAll()
                .stream()
                .map(salesDbMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Orden guardar(Orden orden) {
        OrdenEntity entity = salesDbMapper.toEntity(orden);
        OrdenEntity guardado = ordenRepository.save(entity);
        return salesDbMapper.toDomain(guardado);
    }
}
