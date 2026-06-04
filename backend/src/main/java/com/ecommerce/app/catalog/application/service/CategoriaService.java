package com.ecommerce.app.catalog.application.service;

import java.util.List;
import java.util.Optional;

import com.ecommerce.app.catalog.application.dto.request.ActualizarCategoriaRequest;
import org.springframework.stereotype.Service;

import com.ecommerce.app.catalog.application.dto.request.CrearCategoriaRequest;
import com.ecommerce.app.catalog.application.dto.response.CategoriaResponse;
import com.ecommerce.app.catalog.application.mapper.CategoriaMapper;
import com.ecommerce.app.catalog.domain.model.Categoria;
import com.ecommerce.app.catalog.domain.port.CategoriaPort;
import com.ecommerce.app.catalog.domain.port.ProductoPort;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CategoriaService {

    private final CategoriaPort categoriaPort;
    private final ProductoPort productoPort;
    private final CategoriaMapper categoriaMapper;

    public CategoriaService(CategoriaPort categoriaPort, ProductoPort productoPort, CategoriaMapper categoriaMapper) {
        this.categoriaPort = categoriaPort;
        this.productoPort = productoPort;
        this.categoriaMapper = categoriaMapper;
    }

    public List<CategoriaResponse> listarCategorias() {
        return categoriaPort.listarTodas()
            .stream()
            .map(categoriaMapper::toResponse)
            .toList();
    }

    public CategoriaResponse obtenerPorId(int id) {
        Categoria categoria = categoriaPort.buscarPorId(id)
        		.orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID " +id));
    	
    	return categoriaMapper.toResponse(categoria);
    }

    @Transactional
    public CategoriaResponse registrarCategoria(CrearCategoriaRequest request) {
        Optional<Categoria> categoriaExistente = categoriaPort.buscarPorNombre(request.nombre());

        if (categoriaExistente.isPresent()) {
            throw new IllegalArgumentException("La categoría con el nombre especificado ya existe: " + request.nombre());
        }

        Categoria categoria = categoriaMapper.toEntity(request);
        Categoria categoriaGuardada = categoriaPort.guardar(categoria);

        return categoriaMapper.toResponse(categoriaGuardada);
    }
    
    @Transactional
    public CategoriaResponse editarCategoria(ActualizarCategoriaRequest request) {
        Categoria categoria = categoriaPort.buscarPorId(request.id())
            .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + request.id()));

        categoriaPort.buscarPorNombre(request.nombre()).ifPresent(c -> {
            if (!c.getId().equals(request.id())) {
                throw new IllegalArgumentException("Ya existe otra categoría con el nombre: " + request.nombre());
            }
        });

        categoriaMapper.updateEntity(categoria, request);
        Categoria categoriaActualizada = categoriaPort.guardar(categoria);

        return categoriaMapper.toResponse(categoriaActualizada);
    }

    @Transactional
    public void eliminarCategoria(int id) {
        Categoria categoria = categoriaPort.buscarPorId(id)
            .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));

        if (productoPort.existeProductosConCategoria(id)) {
            throw new IllegalStateException("No se puede eliminar una categoría asociada a un producto.");
        }

        categoriaPort.eliminar(categoria);
    }

    public List<CategoriaResponse> buscarPorNombre(String nombre) {
        return categoriaPort.buscarPorNombreConteniendo(nombre)
        		.stream()
        		.map(categoriaMapper::toResponse)
        		.toList();
    }
}
