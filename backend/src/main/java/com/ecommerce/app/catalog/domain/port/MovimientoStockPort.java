package com.ecommerce.app.catalog.domain.port;

import com.ecommerce.app.catalog.domain.model.MovimientoStock;
import com.ecommerce.app.catalog.domain.model.Producto;
import java.util.List;

public interface MovimientoStockPort {
    List<MovimientoStock> buscarPorProductoOrdenado(Producto producto);
    MovimientoStock guardar(MovimientoStock movimiento);
}
