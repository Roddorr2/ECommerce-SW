package com.ecommerce.app.sales.domain.port;

import com.ecommerce.app.sales.domain.enums.EstadoOrdenCodigo;
import com.ecommerce.app.sales.domain.model.Cliente;
import com.ecommerce.app.sales.domain.model.Orden;
import java.util.List;
import java.util.Optional;

public interface OrdenPort {
    Optional<Orden> buscarPorId(Integer id);
    List<Orden> buscarPorClienteOrdenado(Cliente cliente);
    List<Orden> buscarPorEstadoOrdenado(EstadoOrdenCodigo estadoOrden);
    List<Orden> listarTodas();
    Orden guardar(Orden orden);
}
