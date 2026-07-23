package com.ecommerce.app.purchases.infrastructure.persistence;

import com.ecommerce.app.purchases.domain.model.Proveedor;
import com.ecommerce.app.purchases.domain.port.ProveedorPort;
import com.ecommerce.app.purchases.infrastructure.persistence.entity.ProveedorEntity;
import com.ecommerce.app.purchases.infrastructure.persistence.mapper.ProveedorDbMapper;
import com.ecommerce.app.purchases.infrastructure.persistence.repository.ProveedorJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProveedorJpaAdapter implements ProveedorPort {

    private final ProveedorJpaRepository proveedorRepository;
    private final ProveedorDbMapper proveedorDbMapper;

    public ProveedorJpaAdapter(ProveedorJpaRepository proveedorRepository, ProveedorDbMapper proveedorDbMapper) {
        this.proveedorRepository = proveedorRepository;
        this.proveedorDbMapper = proveedorDbMapper;
    }

    @Override
    public List<Proveedor> listarTodos() {
        return proveedorRepository.findAll().stream()
                .map(proveedorDbMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Proveedor> buscarPorId(int id) {
        return proveedorRepository.findById(id)
                .map(proveedorDbMapper::toDomain);
    }

    @Override
    public Optional<Proveedor> buscarPorCorreo(String correo) {
        return proveedorRepository.findByCorreo(correo)
                .map(proveedorDbMapper::toDomain);
    }

    @Override
    public Proveedor guardar(Proveedor proveedor) {
        ProveedorEntity entity = proveedorDbMapper.toEntity(proveedor);
        ProveedorEntity saved = proveedorRepository.save(entity);
        return proveedorDbMapper.toDomain(saved);
    }

    @Override
    public void eliminar(Proveedor proveedor) {
        ProveedorEntity entity = proveedorDbMapper.toEntity(proveedor);
        proveedorRepository.delete(entity);
    }

    @Override
    public List<Proveedor> buscarPorTipoProveedor(String tipoNombre) {
        return proveedorRepository.buscarPorTipoProveedor(tipoNombre).stream()
                .map(proveedorDbMapper::toDomain)
                .collect(Collectors.toList());
    }
}
