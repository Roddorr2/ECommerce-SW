package com.ecommerce.app.shared.domain.port;

import com.ecommerce.app.shared.domain.model.Rol;
import java.util.List;
import java.util.Optional;

public interface RolPort {
    List<Rol> findAll();
    Optional<Rol> findById(Integer id);
    Optional<Rol> findByNombre(String nombre);
    List<Rol> findByNombreContaining(String nombre);
    Rol save(Rol rol);
    void delete(Rol rol);
}
