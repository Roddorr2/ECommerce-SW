package com.ecommerce.app.purchases.domain.port;

import com.ecommerce.app.purchases.domain.model.Proveedor;
import java.util.List;
import java.util.Optional;

public interface ProveedorPort {
    List<Proveedor> listarTodos();
    Optional<Proveedor> buscarPorId(int id);
    Optional<Proveedor> buscarPorCorreo(String correo);
    Proveedor guardar(Proveedor proveedor);
    void eliminar(Proveedor proveedor);
    List<Proveedor> buscarPorTipoProveedor(String tipoNombre);
}
