package com.ecommerce.app.shared.domain.event;

import com.ecommerce.app.shared.domain.model.Usuario;
import java.util.List;

public record CompraRecibidaEvent(
    String compraId,
    Usuario usuario,
    List<CompraItemData> items
) {
    public record CompraItemData(
        Integer productoId,
        Integer cantidad
    ) {}
}
