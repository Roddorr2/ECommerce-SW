package com.ecommerce.app.catalog.domain.port;

import com.ecommerce.app.catalog.domain.model.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoPort {
    Optional<Producto> buscarPorId(Integer id);
    Optional<Producto> buscarPorSku(String sku);
    List<Producto> buscarProductosDisponibles(String nombre);
    Producto guardar(Producto producto);
    void eliminar(Producto producto);
    List<Producto> listarTodos();
    boolean existeEnCompras(Integer productoId);
    boolean existeEnOrdenes(Integer productoId);
    boolean existeProductosConCategoria(Integer categoriaId);
}
