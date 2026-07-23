package com.ecommerce.app.purchases.domain.port;

import com.ecommerce.app.purchases.domain.model.TipoProveedor;
import java.util.List;
import java.util.Optional;

public interface TipoProveedorPort {
    List<TipoProveedor> listarTodos();
    Optional<TipoProveedor> buscarPorId(int id);
    Optional<TipoProveedor> buscarPorNombre(String nombre);
    List<TipoProveedor> buscarPorNombreConteniendo(String nombre);
    TipoProveedor guardar(TipoProveedor tipoProveedor);
    void eliminar(TipoProveedor tipoProveedor);
}
