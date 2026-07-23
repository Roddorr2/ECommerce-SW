package com.ecommerce.app.purchases.application.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ecommerce.app.purchases.application.dto.request.ActualizarTipoProveedorRequest;
import org.springframework.stereotype.Service;

import com.ecommerce.app.purchases.application.dto.request.CrearTipoProveedorRequest;
import com.ecommerce.app.purchases.application.dto.response.TipoProveedorResponse;
import com.ecommerce.app.purchases.application.mapper.TipoProveedorMapper;
import com.ecommerce.app.purchases.domain.model.Proveedor;
import com.ecommerce.app.purchases.domain.model.TipoProveedor;
import com.ecommerce.app.purchases.domain.port.ProveedorPort;
import com.ecommerce.app.purchases.domain.port.TipoProveedorPort;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class TipoProveedorService {

    private final TipoProveedorPort tipoProveedorPort;
    private final ProveedorPort proveedorPort;
    private final TipoProveedorMapper tipoProveedorMapper;

    public TipoProveedorService(TipoProveedorPort tipoProveedorPort, ProveedorPort proveedorPort, TipoProveedorMapper tipoProveedorMapper) {
        this.tipoProveedorPort = tipoProveedorPort;
        this.proveedorPort = proveedorPort;
        this.tipoProveedorMapper = tipoProveedorMapper;
    }

    public List<TipoProveedorResponse> listarTipoProveedores() {
        return tipoProveedorPort.listarTodos()
                .stream()
                .map(tipoProveedorMapper::toResponse)
                .collect(Collectors.toList());
    }

    public TipoProveedorResponse obtenerPorId(int id) {
        TipoProveedor tipoProveedor = tipoProveedorPort.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de proveedor no encontrado con ID: " + id));

        return tipoProveedorMapper.toResponse(tipoProveedor);
    }

    @Transactional
    public TipoProveedorResponse registrarTipoProveedor(CrearTipoProveedorRequest request) {
        Optional<TipoProveedor> tipoProveedorExistente = tipoProveedorPort.buscarPorNombre(request.nombre());

        if (tipoProveedorExistente.isPresent()) {
            throw new IllegalArgumentException("Ya existe un tipo de proveedor con el nombre: " + request.nombre());
        }

        TipoProveedor tipoProveedor = tipoProveedorMapper.toEntity(request);
        TipoProveedor tipoProveedorGuardado = tipoProveedorPort.guardar(tipoProveedor);

        return tipoProveedorMapper.toResponse(tipoProveedorGuardado);
    }

    @Transactional
    public TipoProveedorResponse editarTipoProveedor(ActualizarTipoProveedorRequest request) {
        TipoProveedor tipoProveedor = tipoProveedorPort.buscarPorId(request.id())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de proveedor no encontrado con ID: " + request.id()));

        tipoProveedorPort.buscarPorNombre(request.nombre()).ifPresent(tp -> {
            if (!tp.getId().equals(request.id())) {
                throw new IllegalArgumentException("Ya existe otro tipo de proveedor con el nombre: " + request.nombre());
            }
        });

        tipoProveedorMapper.updateEntity(tipoProveedor, request);
        TipoProveedor tipoProveedorActualizado = tipoProveedorPort.guardar(tipoProveedor);

        return tipoProveedorMapper.toResponse(tipoProveedorActualizado);
    }

     @Transactional
    public void eliminarTipoProveedor(int id) {
        TipoProveedor tipoProveedor = tipoProveedorPort.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de proveedor no encontrado"));

        List<Proveedor> proveedores = proveedorPort.buscarPorTipoProveedor(tipoProveedor.getNombre());
        if (proveedores != null && !proveedores.isEmpty()) {
            throw new IllegalStateException("No se puede eliminar un tipo de proveedor con proveedores asociados.");
        }

        tipoProveedorPort.eliminar(tipoProveedor);
    }

    public List<TipoProveedorResponse> buscarPorNombre(String nombre) {
        return tipoProveedorPort.buscarPorNombreConteniendo(nombre)
        		.stream()
        		.map(tipoProveedorMapper::toResponse)
        		.toList();
    }
}
