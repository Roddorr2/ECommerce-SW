package com.ecommerce.app.shared.domain.port;

import com.ecommerce.app.shared.domain.model.Empleado;
import java.util.List;
import java.util.Optional;

public interface EmpleadoPort {
    List<Empleado> findAll();
    Optional<Empleado> findById(Integer id);
    Optional<Empleado> findByUsuarioId(Integer usuarioId);
    Empleado save(Empleado empleado);
    void delete(Empleado empleado);
}
