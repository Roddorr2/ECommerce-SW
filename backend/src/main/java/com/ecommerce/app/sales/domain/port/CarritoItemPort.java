package com.ecommerce.app.sales.domain.port;

import com.ecommerce.app.catalog.domain.model.Producto;
import com.ecommerce.app.sales.domain.model.Carrito;
import com.ecommerce.app.sales.domain.model.CarritoItem;
import java.util.Optional;
import java.util.List;

public interface CarritoItemPort {
    Optional<CarritoItem> buscarPorId(Integer id);
    Optional<CarritoItem> buscarPorCarritoYProducto(Carrito carrito, Producto producto);
    CarritoItem guardar(CarritoItem item);
    void eliminar(CarritoItem item);
    void eliminarTodos(List<CarritoItem> items);
}
