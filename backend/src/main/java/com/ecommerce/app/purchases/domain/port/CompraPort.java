package com.ecommerce.app.purchases.domain.port;

import com.ecommerce.app.purchases.domain.enums.EstadoCompraCodigo;
import com.ecommerce.app.purchases.domain.model.Compra;
import com.ecommerce.app.purchases.domain.model.Proveedor;
import java.util.List;
import java.util.Optional;

public interface CompraPort {
    List<Compra> listarTodas();
    Optional<Compra> buscarPorId(int id);
    Compra guardar(Compra compra);
    void eliminar(Compra compra);
    List<Compra> buscarPorProveedorOrdenado(Proveedor proveedor);
    List<Compra> buscarPorEstadoOrdenado(EstadoCompraCodigo estadoCompra);
}
