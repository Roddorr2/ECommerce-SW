package com.ecommerce.app.catalog.application.mapper;

import com.ecommerce.app.catalog.application.dto.request.ActualizarCategoriaRequest;
import com.ecommerce.app.catalog.application.dto.request.CrearCategoriaRequest;
import com.ecommerce.app.catalog.application.dto.response.CategoriaResponse;
import com.ecommerce.app.catalog.domain.model.Categoria;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {

    public Categoria toEntity(CrearCategoriaRequest request) {
        if (request == null) {
            return null;
        }

        Categoria categoria = new Categoria();
        categoria.setNombre(request.nombre());
        return categoria;
    }

    public void updateEntity(Categoria categoria, ActualizarCategoriaRequest request) {
        if (categoria == null || request == null) {
            return;
        }

        categoria.setNombre(request.nombre());
    }

    public CategoriaResponse toResponse(Categoria categoria) {
        if (categoria == null) {
            return null;
        }

        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNombre()
        );
    }
}
