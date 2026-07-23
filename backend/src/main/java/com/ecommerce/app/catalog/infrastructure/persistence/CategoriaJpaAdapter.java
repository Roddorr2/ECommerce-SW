package com.ecommerce.app.catalog.infrastructure.persistence;

import com.ecommerce.app.catalog.domain.model.Categoria;
import com.ecommerce.app.catalog.domain.port.CategoriaPort;
import com.ecommerce.app.catalog.infrastructure.persistence.entity.CategoriaEntity;
import com.ecommerce.app.catalog.infrastructure.persistence.mapper.ProductoDbMapper;
import com.ecommerce.app.catalog.infrastructure.persistence.repository.CategoriaJpaRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class CategoriaJpaAdapter implements CategoriaPort {

    private final CategoriaJpaRepository categoriaJpaRepository;
    private final ProductoDbMapper productoDbMapper;

    public CategoriaJpaAdapter(CategoriaJpaRepository categoriaJpaRepository, ProductoDbMapper productoDbMapper) {
        this.categoriaJpaRepository = categoriaJpaRepository;
        this.productoDbMapper = productoDbMapper;
    }

    @Override
    public Optional<Categoria> buscarPorId(Integer id) {
        return categoriaJpaRepository.findById(id)
                .map(productoDbMapper::toDomain);
    }

    @Override
    public Optional<Categoria> buscarPorNombre(String nombre) {
        return categoriaJpaRepository.findByNombre(nombre)
                .map(productoDbMapper::toDomain);
    }

    @Override
    public List<Categoria> buscarPorNombreConteniendo(String nombre) {
        return categoriaJpaRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(productoDbMapper::toDomain)
                .toList();
    }

    @Override
    public Categoria guardar(Categoria categoria) {
        CategoriaEntity entity = productoDbMapper.toEntity(categoria);
        CategoriaEntity entityGuardada = categoriaJpaRepository.save(entity);
        return productoDbMapper.toDomain(entityGuardada);
    }

    @Override
    public void eliminar(Categoria categoria) {
        CategoriaEntity entity = productoDbMapper.toEntity(categoria);
        categoriaJpaRepository.delete(entity);
    }

    @Override
    public List<Categoria> listarTodas() {
        return categoriaJpaRepository.findAll()
                .stream()
                .map(productoDbMapper::toDomain)
                .toList();
    }
}
