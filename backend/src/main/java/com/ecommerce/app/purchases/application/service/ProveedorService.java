package com.ecommerce.app.purchases.application.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ecommerce.app.purchases.application.dto.request.ActualizarProveedorRequest;
import org.springframework.stereotype.Service;

import com.ecommerce.app.purchases.application.dto.request.CrearProveedorRequest;
import com.ecommerce.app.purchases.application.dto.response.ProveedorResponse;
import com.ecommerce.app.purchases.application.mapper.ProveedorMapper;
import com.ecommerce.app.purchases.domain.model.Compra;
import com.ecommerce.app.purchases.domain.model.Proveedor;
import com.ecommerce.app.purchases.domain.model.TipoProveedor;
import com.ecommerce.app.purchases.domain.port.CompraPort;
import com.ecommerce.app.purchases.domain.port.ProveedorPort;
import com.ecommerce.app.purchases.domain.port.TipoProveedorPort;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ProveedorService {

    private final ProveedorPort proveedorPort;
    private final TipoProveedorPort tipoProveedorPort;
    private final CompraPort compraPort;
    private final ProveedorMapper proveedorMapper;

    public ProveedorService(ProveedorPort proveedorPort, TipoProveedorPort tipoProveedorPort, CompraPort compraPort, ProveedorMapper proveedorMapper) {
        this.proveedorPort = proveedorPort;
        this.tipoProveedorPort = tipoProveedorPort;
        this.compraPort = compraPort;
        this.proveedorMapper = proveedorMapper;
    }

    public List<ProveedorResponse> listarProveedoress() {
        return proveedorPort.listarTodos()
                .stream()
                .map(proveedorMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ProveedorResponse obtenerPorId(int id) {
        Proveedor proveedor = proveedorPort.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("proveedor no encontrado con id: " + id));

        return proveedorMapper.toResponse(proveedor);
    }

    @Transactional
    public ProveedorResponse registrarProveedor(CrearProveedorRequest request) {
        Optional<Proveedor> proveedorExistente = proveedorPort.buscarPorCorreo(request.correo());

        if (proveedorExistente.isPresent()) {
            throw new IllegalArgumentException("Ya existe un proveedor con el email: " + request.correo());
        }

        TipoProveedor tipoProveedor = tipoProveedorPort.buscarPorId(request.tipoProveedorId())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de proveedor no válido con ID: " + request.tipoProveedorId()));

        Proveedor proveedor = proveedorMapper.toEntity(request, tipoProveedor);

        Proveedor proveedorGuardado = proveedorPort.guardar(proveedor);
        return proveedorMapper.toResponse(proveedorGuardado);
    }

    @Transactional
    public ProveedorResponse editarProveedor(ActualizarProveedorRequest request) {
        Proveedor proveedor = proveedorPort.buscarPorId(request.id())
                .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado con ID: " + request.id()));

        proveedorPort.buscarPorCorreo(request.correo()).ifPresent(p -> {
            if (!p.getId().equals(request.id())) {
                throw new IllegalArgumentException("Ya existe otro proveedor con el email: " + request.correo());
            }
        });

        TipoProveedor tipoProveedor = tipoProveedorPort.buscarPorId(request.tipoProveedorId())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de proveedor no válido con ID: " + request.tipoProveedorId()));

        proveedorMapper.updateEntity(proveedor, request, tipoProveedor);
        Proveedor proveedorActualizado = proveedorPort.guardar(proveedor);

        return proveedorMapper.toResponse(proveedorActualizado);
    }

    @Transactional
    public void eliminarProveedor(int id) {
        Proveedor proveedor = proveedorPort.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado"));

        List<Compra> compras = compraPort.buscarPorProveedorOrdenado(proveedor);
        if (compras != null && !compras.isEmpty()) {
            throw new IllegalStateException("No se puede eliminar un proveedor asociado a una compra.");
        }

        proveedorPort.eliminar(proveedor);
    }
    
    public List<ProveedorResponse> buscarPorTipoProveedor(String tipoNombre) {
    	return proveedorPort.buscarPorTipoProveedor(tipoNombre)
    			.stream()
    			.map(proveedorMapper::toResponse)
    			.toList();
    }
}
