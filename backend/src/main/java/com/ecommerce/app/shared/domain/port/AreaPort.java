package com.ecommerce.app.shared.domain.port;

import com.ecommerce.app.shared.domain.model.Area;
import java.util.List;
import java.util.Optional;

public interface AreaPort {
    List<Area> findAll();
    Optional<Area> findById(Integer id);
    Area save(Area area);
    void delete(Area area);
}
