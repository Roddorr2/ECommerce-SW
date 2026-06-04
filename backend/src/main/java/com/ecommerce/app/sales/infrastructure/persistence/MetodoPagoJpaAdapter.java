package com.ecommerce.app.sales.infrastructure.persistence;

import com.ecommerce.app.sales.domain.model.MetodoPago;
import com.ecommerce.app.sales.domain.port.MetodoPagoPort;
import com.ecommerce.app.sales.infrastructure.persistence.entity.MetodoPagoEntity;
import com.ecommerce.app.sales.infrastructure.persistence.mapper.SalesDbMapper;
import com.ecommerce.app.sales.infrastructure.persistence.repository.MetodoPagoJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MetodoPagoJpaAdapter implements MetodoPagoPort {
    private final MetodoPagoJpaRepository metodoPagoRepository;
    private final SalesDbMapper salesDbMapper;

    public MetodoPagoJpaAdapter(MetodoPagoJpaRepository metodoPagoRepository, SalesDbMapper salesDbMapper) {
        this.metodoPagoRepository = metodoPagoRepository;
        this.salesDbMapper = salesDbMapper;
    }

    @Override
    public Optional<MetodoPago> buscarPorId(Integer id) {
        return metodoPagoRepository.findById(id)
                .map(salesDbMapper::toDomain);
    }

    @Override
    public Optional<MetodoPago> buscarPorNombre(String nombre) {
        return metodoPagoRepository.findByNombre(nombre)
                .map(salesDbMapper::toDomain);
    }

    @Override
    public List<MetodoPago> buscarPorNombreConteniendo(String nombre) {
        return metodoPagoRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(salesDbMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<MetodoPago> listarTodos() {
        return metodoPagoRepository.findAll()
                .stream()
                .map(salesDbMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public MetodoPago guardar(MetodoPago metodoPago) {
        MetodoPagoEntity entity = salesDbMapper.toEntity(metodoPago);
        MetodoPagoEntity guardado = metodoPagoRepository.save(entity);
        return salesDbMapper.toDomain(guardado);
    }

    @Override
    public void eliminar(MetodoPago metodoPago) {
        MetodoPagoEntity entity = salesDbMapper.toEntity(metodoPago);
        metodoPagoRepository.delete(entity);
    }
}
