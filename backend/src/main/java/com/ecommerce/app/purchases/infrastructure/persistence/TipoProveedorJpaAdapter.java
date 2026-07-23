package com.ecommerce.app.purchases.infrastructure.persistence;

import com.ecommerce.app.purchases.domain.model.TipoProveedor;
import com.ecommerce.app.purchases.domain.port.TipoProveedorPort;
import com.ecommerce.app.purchases.infrastructure.persistence.entity.TipoProveedorEntity;
import com.ecommerce.app.purchases.infrastructure.persistence.mapper.ProveedorDbMapper;
import com.ecommerce.app.purchases.infrastructure.persistence.repository.TipoProveedorJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TipoProveedorJpaAdapter implements TipoProveedorPort {

    private final TipoProveedorJpaRepository tipoProveedorRepository;
    private final ProveedorDbMapper proveedorDbMapper;

    public TipoProveedorJpaAdapter(TipoProveedorJpaRepository tipoProveedorRepository, ProveedorDbMapper proveedorDbMapper) {
        this.tipoProveedorRepository = tipoProveedorRepository;
        this.proveedorDbMapper = proveedorDbMapper;
    }

    @Override
    public List<TipoProveedor> listarTodos() {
        return tipoProveedorRepository.findAll().stream()
                .map(proveedorDbMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TipoProveedor> buscarPorId(int id) {
        return tipoProveedorRepository.findById(id)
                .map(proveedorDbMapper::toDomain);
    }

    @Override
    public Optional<TipoProveedor> buscarPorNombre(String nombre) {
        return tipoProveedorRepository.findByNombre(nombre)
                .map(proveedorDbMapper::toDomain);
    }

    @Override
    public List<TipoProveedor> buscarPorNombreConteniendo(String nombre) {
        return tipoProveedorRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(proveedorDbMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public TipoProveedor guardar(TipoProveedor tipoProveedor) {
        TipoProveedorEntity entity = proveedorDbMapper.toEntity(tipoProveedor);
        TipoProveedorEntity saved = tipoProveedorRepository.save(entity);
        return proveedorDbMapper.toDomain(saved);
    }

    @Override
    public void eliminar(TipoProveedor tipoProveedor) {
        TipoProveedorEntity entity = proveedorDbMapper.toEntity(tipoProveedor);
        tipoProveedorRepository.delete(entity);
    }
}
