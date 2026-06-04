package com.ecommerce.app.sales.domain.port;

import com.ecommerce.app.sales.domain.enums.EstadoCarritoCodigo;
import com.ecommerce.app.sales.domain.model.Carrito;
import com.ecommerce.app.sales.domain.model.Cliente;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CarritoPort {
    Optional<Carrito> buscarPorId(Integer id);
    Optional<Carrito> buscarPorClienteYEstado(Cliente cliente, EstadoCarritoCodigo estado);
    List<Carrito> buscarExpirados(EstadoCarritoCodigo estado, LocalDateTime fechaExpiracion);
    Carrito guardar(Carrito carrito);
    List<Carrito> guardarTodos(List<Carrito> carritos);
}
