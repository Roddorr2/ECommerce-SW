package com.ecommerce.app.shared.domain.port;

import com.ecommerce.app.shared.domain.model.Cargo;
import java.util.List;
import java.util.Optional;

public interface CargoPort {
    List<Cargo> findAll();
    Optional<Cargo> findById(Integer id);
    Cargo save(Cargo cargo);
    void delete(Cargo cargo);
}
