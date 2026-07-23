package com.ecommerce.app.shared.domain.event;

import com.ecommerce.app.shared.domain.model.Usuario;
import java.util.List;

public record OrdenCreadaEvent(
    String ordenId,
    Usuario usuario,
    List<OrdenItemData> items
) {
    public record OrdenItemData(
        Integer productoId,
        Integer cantidad
    ) {}
}
