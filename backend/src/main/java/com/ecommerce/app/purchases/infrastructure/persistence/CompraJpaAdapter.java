package com.ecommerce.app.purchases.infrastructure.persistence;

import com.ecommerce.app.purchases.domain.enums.EstadoCompraCodigo;
import com.ecommerce.app.purchases.domain.model.Compra;
import com.ecommerce.app.purchases.domain.model.Proveedor;
import com.ecommerce.app.purchases.domain.port.CompraPort;
import com.ecommerce.app.purchases.infrastructure.persistence.entity.CompraEntity;
import com.ecommerce.app.purchases.infrastructure.persistence.entity.ProveedorEntity;
import com.ecommerce.app.purchases.infrastructure.persistence.mapper.CompraDbMapper;
import com.ecommerce.app.purchases.infrastructure.persistence.mapper.ProveedorDbMapper;
import com.ecommerce.app.purchases.infrastructure.persistence.repository.CompraJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CompraJpaAdapter implements CompraPort {

    private final CompraJpaRepository compraRepository;
    private final CompraDbMapper compraDbMapper;
    private final ProveedorDbMapper proveedorDbMapper;

    public CompraJpaAdapter(CompraJpaRepository compraRepository,
                            CompraDbMapper compraDbMapper,
                            ProveedorDbMapper proveedorDbMapper) {
        this.compraRepository = compraRepository;
        this.compraDbMapper = compraDbMapper;
        this.proveedorDbMapper = proveedorDbMapper;
    }

    @Override
    public List<Compra> listarTodas() {
        return compraRepository.findAll().stream()
                .map(compraDbMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Compra> buscarPorId(int id) {
        return compraRepository.findById(id)
                .map(compraDbMapper::toDomain);
    }

    @Override
    public Compra guardar(Compra compra) {
        CompraEntity entity = compraDbMapper.toEntity(compra);
        CompraEntity saved = compraRepository.save(entity);
        return compraDbMapper.toDomain(saved);
    }

    @Override
    public void eliminar(Compra compra) {
        CompraEntity entity = compraDbMapper.toEntity(compra);
        compraRepository.delete(entity);
    }

    @Override
    public List<Compra> buscarPorProveedorOrdenado(Proveedor proveedor) {
        ProveedorEntity proveedorEntity = proveedorDbMapper.toEntity(proveedor);
        return compraRepository.findByProveedorEntityOrderByFechaCompraDesc(proveedorEntity).stream()
                .map(compraDbMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Compra> buscarPorEstadoOrdenado(EstadoCompraCodigo estadoCompra) {
        return compraRepository.findByEstadoCompraOrderByFechaCompraDesc(estadoCompra).stream()
                .map(compraDbMapper::toDomain)
                .collect(Collectors.toList());
    }
}
