package com.ecommerce.app.sales.infrastructure.persistence;

import com.ecommerce.app.sales.domain.enums.EstadoCarritoCodigo;
import com.ecommerce.app.sales.domain.model.Carrito;
import com.ecommerce.app.sales.domain.model.Cliente;
import com.ecommerce.app.sales.domain.port.CarritoPort;
import com.ecommerce.app.sales.infrastructure.persistence.entity.CarritoEntity;
import com.ecommerce.app.sales.infrastructure.persistence.entity.ClienteEntity;
import com.ecommerce.app.sales.infrastructure.persistence.mapper.SalesDbMapper;
import com.ecommerce.app.sales.infrastructure.persistence.repository.CarritoJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CarritoJpaAdapter implements CarritoPort {
    private final CarritoJpaRepository carritoJpaRepository;
    private final SalesDbMapper salesDbMapper;

    public CarritoJpaAdapter(CarritoJpaRepository carritoJpaRepository, SalesDbMapper salesDbMapper) {
        this.carritoJpaRepository = carritoJpaRepository;
        this.salesDbMapper = salesDbMapper;
    }

    @Override
    public Optional<Carrito> buscarPorId(Integer id) {
        return carritoJpaRepository.findById(id)
                .map(salesDbMapper::toDomain);
    }

    @Override
    public Optional<Carrito> buscarPorClienteYEstado(Cliente cliente, EstadoCarritoCodigo estado) {
        ClienteEntity clienteEntity = salesDbMapper.toEntity(cliente);
        return carritoJpaRepository.findByClienteAndEstadoCarrito(clienteEntity, estado)
                .map(salesDbMapper::toDomain);
    }

    @Override
    public List<Carrito> buscarExpirados(EstadoCarritoCodigo estado, LocalDateTime fechaExpiracion) {
        return carritoJpaRepository.findByEstadoCarritoAndFechaActualizacionBefore(estado, fechaExpiracion)
                .stream()
                .map(salesDbMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Carrito guardar(Carrito carrito) {
        CarritoEntity entity = salesDbMapper.toEntity(carrito);
        CarritoEntity guardado = carritoJpaRepository.save(entity);
        return salesDbMapper.toDomain(guardado);
    }

    @Override
    public List<Carrito> guardarTodos(List<Carrito> carritos) {
        List<CarritoEntity> entities = carritos.stream()
                .map(salesDbMapper::toEntity)
                .collect(Collectors.toList());
        List<CarritoEntity> guardados = carritoJpaRepository.saveAll(entities);
        return guardados.stream()
                .map(salesDbMapper::toDomain)
                .collect(Collectors.toList());
    }
}
