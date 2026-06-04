package com.ecommerce.app.sales.domain.port;

import com.ecommerce.app.sales.domain.model.MetodoPago;
import java.util.List;
import java.util.Optional;

public interface MetodoPagoPort {
    Optional<MetodoPago> buscarPorId(Integer id);
    Optional<MetodoPago> buscarPorNombre(String nombre);
    List<MetodoPago> buscarPorNombreConteniendo(String nombre);
    List<MetodoPago> listarTodos();
    MetodoPago guardar(MetodoPago metodoPago);
    void eliminar(MetodoPago metodoPago);
}

