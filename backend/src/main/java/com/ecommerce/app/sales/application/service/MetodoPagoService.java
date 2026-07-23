package com.ecommerce.app.sales.application.service;

import java.util.List;
import java.util.Optional;

import com.ecommerce.app.sales.application.dto.request.ActualizarMetodoPagoRequest;
import com.ecommerce.app.sales.application.dto.request.CrearMetodoPagoRequest;
import com.ecommerce.app.sales.application.mapper.MetodoPagoMapper;
import com.ecommerce.app.sales.domain.model.MetodoPago;
import com.ecommerce.app.sales.domain.port.MetodoPagoPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.ecommerce.app.sales.application.dto.response.MetodoPagoResponse;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MetodoPagoService {

    private final MetodoPagoPort metodoPagoPort;
    private final MetodoPagoMapper metodoPagoMapper;

    public MetodoPagoService(MetodoPagoPort metodoPagoPort, MetodoPagoMapper metodoPagoMapper) {
        this.metodoPagoPort = metodoPagoPort;
        this.metodoPagoMapper = metodoPagoMapper;
    }

    public List<MetodoPagoResponse> listarMetodosPago() {
        return metodoPagoPort.listarTodos()
                .stream()
                .map(metodoPagoMapper::toResponse)
                .toList();
    }

    public MetodoPagoResponse obtenerPorId(int id) {
        MetodoPago metodoPago = metodoPagoPort.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Método de pago no encontrado con ID: " + id));

        return metodoPagoMapper.toResponse(metodoPago);
    }
    
    @Transactional
    public MetodoPagoResponse registrarNetodoPago(CrearMetodoPagoRequest request) {
        Optional<MetodoPago> metodoPagoExistente = metodoPagoPort.buscarPorNombre(request.nombre());

        if (metodoPagoExistente.isPresent()) {
            throw new IllegalArgumentException("El método de pago con el nombre especificado ya existe: " + request.nombre());
        }

        MetodoPago metodoPago = metodoPagoMapper.toEntity(request);
        MetodoPago metodoPagoGuardada = metodoPagoPort.guardar(metodoPago);

        return metodoPagoMapper.toResponse(metodoPagoGuardada);
    }

    @Transactional
    public MetodoPagoResponse editarMetodoPago(ActualizarMetodoPagoRequest request) {
        MetodoPago metodoPago = metodoPagoPort.buscarPorId(request.id())
                .orElseThrow(() -> new EntityNotFoundException("Método de pago no encontrado con ID: " + request.id()));

        metodoPagoPort.buscarPorNombre(request.nombre()).ifPresent(mp -> {
            if (!mp.getId().equals(request.id())) {
                throw new IllegalArgumentException("Ya existe otro método de pago con el nombre: " + request.nombre());
            }
        });

        metodoPagoMapper.updateEntity(metodoPago, request);
        MetodoPago metodoPagoActualizado = metodoPagoPort.guardar(metodoPago);

        return metodoPagoMapper.toResponse(metodoPagoActualizado);
    }

    @Transactional
    public void eliminarMetodoPago(int id) {
        MetodoPago metodoPago = metodoPagoPort.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Método de pago no encontrado"));

        if (metodoPago.getOrdenes() != null && !metodoPago.getOrdenes().isEmpty()) {
            throw new IllegalStateException("No se puede eliminar un método de pago asociado a órdenes.");
        }

        metodoPagoPort.eliminar(metodoPago);
    }

    public List<MetodoPagoResponse> buscarPorNombre(String nombre) {
        return metodoPagoPort.buscarPorNombreConteniendo(nombre)
        		.stream()
                .map(metodoPagoMapper::toResponse)
                .toList();
    }
}
